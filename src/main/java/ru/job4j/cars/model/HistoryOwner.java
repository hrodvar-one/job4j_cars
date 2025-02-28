package ru.job4j.cars.model;

import javax.persistence.*;

@Entity
@Table(name = "history_owners")
public class HistoryOwner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "car_id", referencedColumnName = "id", nullable = false)
    private Car car;

    @OneToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id", nullable = false)
    private Owner owner;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "history_id", referencedColumnName = "id", nullable = false)
    private History history;
}
