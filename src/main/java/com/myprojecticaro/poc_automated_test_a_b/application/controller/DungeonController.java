package com.myprojecticaro.poc_automated_test_a_b.application.controller;


import com.myprojecticaro.poc_automated_test_a_b.application.dto.DungeonRequest;
import com.myprojecticaro.poc_automated_test_a_b.application.dto.DungeonResponse;
import com.myprojecticaro.poc_automated_test_a_b.application.dto.RunSummaryResponse;
import com.myprojecticaro.poc_automated_test_a_b.domain.model.Variant;
import com.myprojecticaro.poc_automated_test_a_b.infrastructure.service.ExperimentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class DungeonController {

    private final ExperimentService experimentService;

    public DungeonController(ExperimentService experimentService) {
        this.experimentService = experimentService;
    }

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

    @GetMapping("/experiments/summary")
    public RunSummaryResponse summary() {
        return new RunSummaryResponse(
                experimentService.count(),
                experimentService.countA(),
                experimentService.countB(),
                experimentService.avgMicros()
        );
    }

    private static String clientIp(HttpServletRequest r) {
        String xff = r.getHeader("X-Forwarded-For");
        return xff != null ? xff.split(",")[0].trim() : r.getRemoteAddr();
    }
}