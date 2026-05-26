package ru.job4j.cars.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "auto_post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String description;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "auto_post_photo", joinColumns = @JoinColumn(name = "post_id"))
    @Column(name = "path", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<String> photos = new HashSet<>();

    @Column(nullable = false)
    private LocalDateTime created = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auto_user_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Car car;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<PriceHistory> priceHistory = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "participates",
            joinColumns = @JoinColumn(name = "post_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "user_id", nullable = false)
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<User> participates = new ArrayList<>();

    public void addPriceHistory(PriceHistory history) {
        priceHistory.add(history);
        history.setPost(this);
    }

    public void removePriceHistory(PriceHistory history) {
        priceHistory.remove(history);
        history.setPost(null);
    }

    public void addPhoto(String photo) {
        photos.add(photo);
    }

    public void removePhoto(String photo) {
        photos.remove(photo);
    }

    public void addParticipate(User user) {
        participates.add(user);
    }

    public void removeParticipate(User user) {
        participates.remove(user);
    }
}
