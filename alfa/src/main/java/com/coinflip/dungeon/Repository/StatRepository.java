package com.coinflip.dungeon.Repository;

import com.coinflip.dungeon.Domain.Stat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatRepository extends JpaRepository<Stat, Integer> {
}