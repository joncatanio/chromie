package com.catanio.chromie.dm.repositories;

import com.catanio.chromie.dm.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findBySlackId(String slackId);
}