package ru.job4j.cars.model;

public enum BodyType {
    SEDAN("Седан"),
    HATCHBACK("Хэтчбек"),
    WAGON("Универсал"),
    SUV("Внедорожник"),
    COUPE("Купе");

    private final String title;

    BodyType(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }
}
