package ru.job4j.cars.model;

public enum Transmission {
    MANUAL("Механическая"),
    AUTOMATIC("Автоматическая"),
    ROBOT("Робот"),
    CVT("Вариатор");

    private final String title;

    Transmission(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }
}
