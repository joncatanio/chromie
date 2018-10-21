package com.catanio.chromie.repositories;

import com.catanio.chromie.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findBySlackId(String slackId);
}