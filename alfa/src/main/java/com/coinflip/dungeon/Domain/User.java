package com.coinflip.dungeon.Domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "username", nullable = false, length = 50)
    private String username;

    @Email(message = "Invalid email format")
    @Column(nullable = false, length = 75)
    private String email;

    @JsonIgnore
    @Column(nullable = false, length = 120)
    private String password;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    private boolean isEmailVerified;

    @ManyToMany
    @JoinTable(
            name = "user_campaigns",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "campaign_id")
    )
    private Set<Campaign> campaigns;

    @OneToMany(mappedBy = "user")
    private Set<Character> characters;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private Set<CampaignUsers> campaignUsers;

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public User() {

    }
}
