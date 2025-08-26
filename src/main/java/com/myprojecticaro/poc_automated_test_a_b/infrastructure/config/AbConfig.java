package com.myprojecticaro.poc_automated_test_a_b.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Configuration
public class AbConfig {
    @Value("${ab.split.a:50}")
    public int splitA;
}