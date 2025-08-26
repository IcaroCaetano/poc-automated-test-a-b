package com.myprojecticaro.poc_automated_test_a_b.application.dto;

import jakarta.validation.constraints.NotNull;

public record DungeonRequest(@NotNull int[][] dungeon) {}
