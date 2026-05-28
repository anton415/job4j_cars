package ru.job4j.cars.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "cars")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String model;

    @Column(name = "production_year", nullable = false)
    private int year;

    @Column(name = "body_type", nullable = false)
    private String bodyType;

    @Column(name = "engine_type", nullable = false)
    private String engineType;

    @Column(nullable = false)
    private String transmission;

    @Column(nullable = false)
    private int mileage;
}
