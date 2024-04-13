package com.coinflip.dungeon.Domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@Table (name = "campaigns")

public class Campaign {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 5000)
    private String description;

    @Column
    private String coverImage;

    @ManyToMany(mappedBy = "campaigns")
    private Set<User> members;

    @ManyToMany
    @JoinTable(
            name = "campaign_characters",
            joinColumns = @JoinColumn(name = "campaign_id"),
            inverseJoinColumns = @JoinColumn(name = "character_id")
    )
    private Set<Character> characters;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "campaign")
    private Set<CampaignUsers> campaignUsers;
}
