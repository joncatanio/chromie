package com.catanio.chromie.dm.repositories;

import com.catanio.chromie.dm.entities.Karma;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KarmaRepository extends CrudRepository<Karma, Long> {
}
