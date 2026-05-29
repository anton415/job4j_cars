package ru.job4j.cars.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PostDto {
    private String title;

    private String description;

    private BigDecimal price;

    private String brand;

    private String model;

    private int year;

    private String bodyType;

    private String engineType;

    private String transmission;

    private int mileage;
}
