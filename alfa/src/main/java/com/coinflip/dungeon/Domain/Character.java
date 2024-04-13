package com.coinflip.dungeon.Domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@Table (name = "characters")
public class Character {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "is_public", nullable = false)
    private boolean isPublic;

    @OneToMany(mappedBy = "character")
    private Set<CharacterStats> character_stats;

    @OneToMany(mappedBy = "character")
    private Set<CharacterCharacteristics> character_characteristics;

    @ManyToMany(mappedBy = "characters")
    private Set<Campaign> campaigns;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Character(String name, String description, Set<CharacterStats> character_stats,
                     Set<CharacterCharacteristics> character_characteristics, Set<Campaign> campaigns, User user) {
        this.name = name;
        this.description = description;
        this.character_stats = character_stats;
        this.character_characteristics = character_characteristics;
        this.campaigns = campaigns;
        this.user = user;
        this.isPublic = true;
    }

    public Character(String name, String description, Set<CharacterStats> character_stats,
                     Set<CharacterCharacteristics> character_characteristics, User user) {
        this.name = name;
        this.description = description;
        this.character_stats = character_stats;
        this.character_characteristics = character_characteristics;
        this.user = user;
        this.isPublic = true;
    }

    public Character(String name, String description, boolean isPublic, User user) {
        this.name = name;
        this.description = description;
        this.user = user;
        this.isPublic = isPublic;
    }

    public Character(String name, String description, User user) {
        this.name = name;
        this.description = description;
        this.user = user;
    }

    public Character() {
    }

}
