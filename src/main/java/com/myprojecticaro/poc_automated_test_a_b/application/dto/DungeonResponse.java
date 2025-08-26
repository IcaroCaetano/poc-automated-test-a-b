package com.myprojecticaro.poc_automated_test_a_b.application.dto;

import com.myprojecticaro.poc_automated_test_a_b.domain.model.Variant;

public record DungeonResponse(Variant variant, int rows, int cols, int result, long micros, Long storedId) {}
