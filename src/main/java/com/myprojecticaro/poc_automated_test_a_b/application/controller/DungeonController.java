package com.myprojecticaro.poc_automated_test_a_b.application.controller;


import com.myprojecticaro.poc_automated_test_a_b.application.dto.DungeonRequest;
import com.myprojecticaro.poc_automated_test_a_b.application.dto.DungeonResponse;
import com.myprojecticaro.poc_automated_test_a_b.application.dto.RunSummaryResponse;
import com.myprojecticaro.poc_automated_test_a_b.domain.model.Variant;
import com.myprojecticaro.poc_automated_test_a_b.infrastructure.service.ExperimentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller that exposes endpoints for running dungeon-based experiments
 * and retrieving aggregated experiment results.
 * <p>
 * This controller provides two main functionalities:
 * <ul>
 *   <li>Run a dungeon test with a given dungeon grid and optional A/B test variant.</li>
 *   <li>Retrieve a summary of all executed experiments, including counts and performance statistics.</li>
 * </ul>
 */
@RestController
@RequestMapping(path = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Dungeon Experiments", description = "Endpoints for running dungeon A/B experiments and retrieving summary statistics")
public class DungeonController {

    private final ExperimentService experimentService;

    /**
     * Constructs a new {@code DungeonController} with the given experiment service.
     *
     * @param experimentService the service responsible for executing and tracking experiments
     */
    public DungeonController(ExperimentService experimentService) {
        this.experimentService = experimentService;
    }

    /**
     * Executes the minimum initial health calculation for a dungeon grid.
     * <p>
     * This endpoint is the core of the A/B testing functionality:
     * a request may specify a {@link Variant} (A or B). If not provided,
     * the service may choose a variant automatically.
     * </p>
     *
     * @param request  the dungeon grid input wrapped in a {@link DungeonRequest}
     * @param variant  the optional variant (A/B) to run the experiment with
     * @param httpReq  the HTTP request, used to extract the client IP for tracking
     * @return a {@link DungeonResponse} containing the result, execution time, and variant information
     */
    @Operation(
            summary = "Run dungeon minimum initial health experiment",
            description = "Calculates the minimum initial health required for a dungeon grid. " +
                    "Supports optional A/B test variant selection. " +
                    "If variant is not provided, the system selects one automatically.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Experiment executed successfully",
                            content = @Content(schema = @Schema(implementation = DungeonResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request payload")
            }
    )
    @PostMapping(path = "/dungeon/min-initial-health", consumes = MediaType.APPLICATION_JSON_VALUE)
    public DungeonResponse minInitial(
            @Valid @RequestBody DungeonRequest request,
            @RequestParam(value = "variant", required = false) Variant variant,
            HttpServletRequest httpReq
    ) {
        var result = experimentService.run(request.dungeon(), variant, clientIp(httpReq));
        return new DungeonResponse(
                result.variant(),
                result.rows(),
                result.cols(),
                result.result(),
                result.micros(),
                result.storedId()
        );
    }

    /**
     * Retrieves a summary of all experiment runs.
     * <p>
     * The response includes:
     * <ul>
     *   <li>Total number of experiments</li>
     *   <li>Number of runs for variant A</li>
     *   <li>Number of runs for variant B</li>
     *   <li>Average execution time in microseconds</li>
     * </ul>
     * </p>
     *
     * @return a {@link RunSummaryResponse} containing aggregated experiment statistics
     */
    @Operation(
            summary = "Get summary of all dungeon experiments",
            description = "Returns aggregated statistics of executed experiments, including counts for each variant and average execution time.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Summary retrieved successfully",
                            content = @Content(schema = @Schema(implementation = RunSummaryResponse.class)))
            }
    )
    @GetMapping("/experiments/summary")
    public RunSummaryResponse summary() {
        return new RunSummaryResponse(
                experimentService.count(),
                experimentService.countA(),
                experimentService.countB(),
                experimentService.avgMicros()
        );
    }

    /**
     * Extracts the client IP address from the HTTP request.
     * <p>
     * If the "X-Forwarded-For" header is present (e.g., when behind a proxy),
     * the first IP in the header is returned. Otherwise, the remote address
     * from the request is used.
     * </p>
     *
     * @param r the HTTP request
     * @return the resolved client IP address
     */
    private static String clientIp(HttpServletRequest r) {
        String xff = r.getHeader("X-Forwarded-For");
        return xff != null ? xff.split(",")[0].trim() : r.getRemoteAddr();
    }
}