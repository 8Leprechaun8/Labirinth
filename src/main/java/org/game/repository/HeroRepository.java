package org.game.repository;

import org.game.entity.Hero;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface HeroRepository extends CrudRepository<Hero, UUID> {

    void deleteByUserId(UUID userId);

    Optional<Hero> getByUserIdAndPlayerTrue(UUID userId);

    List<Hero> getByUserIdAndPlayerFalse(UUID userId);
}
