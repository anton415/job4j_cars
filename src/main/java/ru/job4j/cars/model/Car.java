package ru.job4j.cars.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "car")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "engine_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Engine engine;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "history_owners",
            joinColumns = @JoinColumn(name = "car_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "owner_id", nullable = false)
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Owner> owners = new HashSet<>();

    public void addOwner(Owner owner) {
        owners.add(owner);
    }

    public void removeOwner(Owner owner) {
        owners.remove(owner);
    }
}
