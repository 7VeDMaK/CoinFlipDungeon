package com.coinflip.dungeon.Domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "character_stats")
public class CharacterStats {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "character_id")
    private Character character;

    @ManyToOne
    @JoinColumn(name = "stat_id")
    private Stat stat;

    @Column(name = "value", nullable = false)
    private int value;


    CharacterStats(Stat stat, int value){
        this.stat = stat;
        this.value = value;
    }

    public CharacterStats() {

    }
}
