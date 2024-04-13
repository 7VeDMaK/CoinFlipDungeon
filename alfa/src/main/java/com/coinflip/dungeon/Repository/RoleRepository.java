package com.coinflip.dungeon.Repository;

import com.coinflip.dungeon.Domain.ERole;
import com.coinflip.dungeon.Domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(ERole role);
}
