package com.h_s.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "metric")
public class Metric {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "metric")
    private String metric;
    @Column(name = "endpoint")
    private String endpoint;
    @Column(name = "timestamp")
    private long timestamp;
    @Column(name = "step")
    private int step;
    @Column(name = "value")
    private double value;
}
