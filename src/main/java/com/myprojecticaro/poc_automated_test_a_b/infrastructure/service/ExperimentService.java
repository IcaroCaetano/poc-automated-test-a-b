package com.myprojecticaro.poc_automated_test_a_b.infrastructure.service;

import com.myprojecticaro.poc_automated_test_a_b.domain.DungeonService;
import com.myprojecticaro.poc_automated_test_a_b.domain.model.ExperimentRun;
import com.myprojecticaro.poc_automated_test_a_b.domain.model.Variant;
import com.myprojecticaro.poc_automated_test_a_b.domain.util.Stopwatch;
import com.myprojecticaro.poc_automated_test_a_b.infrastructure.config.AbConfig;
import com.myprojecticaro.poc_automated_test_a_b.infrastructure.repository.ExperimentRunRepository;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;

/**
 * Service class responsible for managing dungeon experiments and A/B testing logic.
 * <p>
 * This service runs experiments using different algorithms (variants A and B) to
 * calculate the minimum initial health required in a dungeon. It stores the results
 * in the database and provides summary statistics such as counts and average execution time.
 * </p>
 *
 * Dependencies:
 * - {@link DungeonService} to compute minimum health using different strategies.
 * - {@link ExperimentRunRepository} to persist experiment results.
 * - {@link AbConfig} to configure the percentage split for variant A in A/B testing.
 */
@Service
public class ExperimentService {

    private final DungeonService dungeonService;
    private final ExperimentRunRepository repo;
    private final SecureRandom rng = new SecureRandom();
    private final int splitA;

    /**
     * Constructs an ExperimentService with required dependencies.
     *
     * @param dungeonService the service responsible for dungeon calculations
     * @param repo the repository to persist experiment runs
     * @param cfg configuration object containing A/B split percentages
     */
    public ExperimentService(DungeonService dungeonService,
                             ExperimentRunRepository repo,
                             AbConfig cfg) {
        this.dungeonService = dungeonService;
        this.repo = repo;
        this.splitA = cfg.splitA;
    }

    /**
     * Immutable record representing the result of an experiment run.
     *
     * @param variant the algorithm variant used (A or B)
     * @param rows number of rows in the dungeon grid
     * @param cols number of columns in the dungeon grid
     * @param result the computed minimum initial health
     * @param micros execution time in microseconds
     * @param storedId database ID of the persisted experiment run
     */
    public record Result(Variant variant, int rows, int cols, int result, long micros, Long storedId) {}

    /**
     * Executes an experiment on a dungeon grid, choosing a variant
     * based on A/B testing rules or a forced variant.
     * <p>
     * The chosen algorithm computes the minimum health required to survive the dungeon,
     * measures the execution time, and saves the result in the database.
     * </p>
     *
     * @param dungeon the 2D dungeon grid
     * @param forced optional forced variant (A or B); if null, variant is chosen randomly based on splitA
     * @param clientIp the IP of the client requesting the experiment
     * @return a {@link Result} object containing variant, dungeon size, result, execution time, and stored ID
     */
    public Result run(int[][] dungeon, Variant forced, String clientIp) {
        Variant variant = forced != null
                ? forced
                : (rng.nextInt(100) < splitA ? Variant.A : Variant.B);

        Stopwatch sw = new Stopwatch();
        int res = switch (variant) {
            case A -> dungeonService.minHealthBottomUp(dungeon);
            case B -> dungeonService.minHealthTopDown(dungeon);
        };
        long micros = sw.micros();

        var saved = repo.save(new ExperimentRun(
                variant,
                dungeon.length,
                dungeon[0].length,
                res,
                micros,
                clientIp
        ));

        return new Result(variant, dungeon.length, dungeon[0].length, res, micros, saved.getId());
    }

    /**
     * Returns the total number of experiments stored in the repository.
     *
     * @return the count of all experiment runs
     */
    public long count() {
        return repo.count();
    }



    /**
     * Returns the number of experiments that used variant A.
     *
     * @return the count of variant A runs
     */
    public long countA() {
        return repo.countByVariant(Variant.A);
    }

    /**
     * Returns the number of experiments that used variant B.
     *
     * @return the count of variant B runs
     */
    public long countB() {
        return repo.countByVariant(Variant.B);
    }

    /**
     * Calculates the average execution time (in microseconds) of all experiment runs.
     *
     * @return the average execution time, or 0 if no runs exist
     */
    public double avgMicros() {
        List<ExperimentRun> runs = repo.findAll();
        if (runs.isEmpty()) return 0;
        return runs.stream().mapToLong(ExperimentRun::getMicros).average().orElse(0);
    }
}