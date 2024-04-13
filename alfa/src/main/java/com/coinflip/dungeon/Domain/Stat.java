package com.coinflip.dungeon.Domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "stats",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = "stat_name")}
)
public class Stat {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "stat_name", nullable = false, length = 50)
    private String name;

    @Column(name = "stat_description")
    private String description;

    public Stat(String name, String description){
        this.name = name;
        this.description = description;
    }

    public Stat() {

    }
}
