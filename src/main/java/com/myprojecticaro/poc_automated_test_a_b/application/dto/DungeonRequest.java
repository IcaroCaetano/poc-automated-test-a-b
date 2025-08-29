package com.myprojecticaro.poc_automated_test_a_b.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record DungeonRequest(
        @Schema(
                description = "Dungeon grid for the experiment",
                example = "[[0,-2,-3],[-1,-2,-1],[10,30,-5]]"
        )
        @NotNull int[][] dungeon) {}
