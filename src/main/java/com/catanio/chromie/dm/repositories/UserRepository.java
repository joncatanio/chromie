package com.catanio.chromie.dm.repositories;

import com.catanio.chromie.dm.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findBySlackId(String slackId);
}