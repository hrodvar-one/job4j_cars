package ru.job4j.cars.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "price_history")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PriceHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "before", nullable = false)
    private Long before;

    @Column(name = "after", nullable = false)
    private Long after;

    @Column(name = "created", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT NOW()")
    private LocalDateTime created = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "auto_post_id", nullable = false)
    private Post post;
}
