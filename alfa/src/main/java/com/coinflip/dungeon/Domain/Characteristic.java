package com.coinflip.dungeon.Domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "characteristics",
        uniqueConstraints = {
        @UniqueConstraint(columnNames = "characteristic_name")
})
public class Characteristic {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "characteristic_name", nullable = false, length = 50)
    private String name;

    @Column(name = "characteristic_description", nullable = false)
    private String description;
}
