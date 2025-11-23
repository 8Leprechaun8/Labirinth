package org.game.repository;

import org.game.entity.Landscape;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LandscapeRepository extends CrudRepository<Landscape, UUID> {

    void deleteByUserId(UUID userId);

    List<Landscape> findAllByUserId(UUID userId);

    Optional<Landscape>  findByUserIdAndIAndJ(UUID userId, int i, int j);
}
