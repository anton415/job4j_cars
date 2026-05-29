package ru.job4j.cars.model;

public enum CarModel {
    CAMRY("Camry"),
    COROLLA("Corolla"),
    ACCORD("Accord"),
    CIVIC("Civic"),
    QASHQAI("Qashqai"),
    SOLARIS("Solaris"),
    RIO("Rio"),
    X5("X5"),
    E_CLASS("E-Class"),
    VESTA("Vesta"),
    GRANTA("Granta");

    private final String title;

    CarModel(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }
}
