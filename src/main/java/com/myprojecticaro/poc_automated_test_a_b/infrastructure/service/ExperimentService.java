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

@Service
public class ExperimentService {

    private final DungeonService dungeonService;
    private final ExperimentRunRepository repo;
    private final SecureRandom rng = new SecureRandom();
    private final int splitA;

    public ExperimentService(DungeonService dungeonService,
                             ExperimentRunRepository repo,
                             AbConfig cfg) {
        this.dungeonService = dungeonService;
        this.repo = repo;
        this.splitA = cfg.splitA;
    }

    public record Result(Variant variant, int rows, int cols, int result, long micros, Long storedId) {}

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

    public long count() {
        return repo.count();
    }


    public long countA() {
        return repo.countByVariant(Variant.A);
    }

    public long countB() {
        return repo.countByVariant(Variant.B);
    }

    public double avgMicros() {
        List<ExperimentRun> runs = repo.findAll();
        if (runs.isEmpty()) return 0;
        return runs.stream().mapToLong(ExperimentRun::getMicros).average().orElse(0);
    }
}