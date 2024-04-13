package com.coinflip.dungeon.Domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "campaign_secondary_roles")
public class CampaignSecondaryRoles {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    // ???
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_users_id")
    private CampaignUsers campaignUsers;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;
}
