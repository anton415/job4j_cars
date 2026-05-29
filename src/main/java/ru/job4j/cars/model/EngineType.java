package ru.job4j.cars.model;

public enum EngineType {
    PETROL("Бензиновый"),
    DIESEL("Дизельный"),
    HYBRID("Гибридный"),
    ELECTRIC("Электрический");

    private final String title;

    EngineType(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }
}
