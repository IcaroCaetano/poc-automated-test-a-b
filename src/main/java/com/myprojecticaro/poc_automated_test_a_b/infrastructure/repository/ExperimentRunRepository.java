package com.myprojecticaro.poc_automated_test_a_b.infrastructure.repository;

import com.myprojecticaro.poc_automated_test_a_b.domain.model.ExperimentRun;
import com.myprojecticaro.poc_automated_test_a_b.domain.model.Variant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ExperimentRunRepository extends JpaRepository<ExperimentRun, Long> {
    long countByVariant(Variant v);

    @Query("select avg(e.micros) from ExperimentRun e")
    Double avgMicros();
}
