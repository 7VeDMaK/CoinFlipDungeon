package com.coinflip.dungeon.Repository;

import com.coinflip.dungeon.Domain.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampaignRepository extends JpaRepository<Campaign, Integer> {
}
