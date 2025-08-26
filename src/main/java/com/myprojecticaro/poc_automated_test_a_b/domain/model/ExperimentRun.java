package com.myprojecticaro.poc_automated_test_a_b.domain.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;


@Entity
@Table(name = "experiment_run")
public class ExperimentRun {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Variant variant;


    @Column(nullable = false)
    private int rows;


    @Column(nullable = false)
    private int cols;


    @Column(nullable = false)
    private int result;


    @Column(nullable = false)
    private long micros;


    @Column(nullable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();


    private String clientIp;

    protected ExperimentRun() {}


    public ExperimentRun(Variant variant, int rows, int cols, int result, long micros, String clientIp) {
        this.variant = variant;
        this.rows = rows;
        this.cols = cols;
        this.result = result;
        this.micros = micros;
        this.clientIp = clientIp;
    }


    public Long getId() { return id; }
}