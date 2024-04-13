package com.coinflip.dungeon.Repository;

import com.coinflip.dungeon.Domain.Character;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;

public interface CharacterRepository extends JpaRepository<Character, Integer> {

    @Modifying
    @Transactional
    void deleteAllByNameAndUserId(String name, Integer userId);

    Optional<Character> findByNameAndUserId(String name, Integer userId);
}