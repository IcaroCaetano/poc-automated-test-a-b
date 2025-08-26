package com.myprojecticaro.poc_automated_test_a_b.application.dto;

public record RunSummaryResponse(long totalRuns, long variantACount, long variantBCount, double avgMicros) {}
