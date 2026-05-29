package ru.job4j.cars.model;

public enum CarBrand {
    TOYOTA("Toyota"),
    HONDA("Honda"),
    NISSAN("Nissan"),
    HYUNDAI("Hyundai"),
    KIA("Kia"),
    BMW("BMW"),
    MERCEDES_BENZ("Mercedes-Benz"),
    LADA("Lada");

    private final String title;

    CarBrand(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }
}
