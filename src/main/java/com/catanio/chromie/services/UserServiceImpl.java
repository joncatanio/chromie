package com.catanio.chromie.services;

import com.catanio.chromie.entities.User;
import com.catanio.chromie.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Adds a new user to the user table in the database
     *
     * @param slackId the Slack ID of the user to be inserted
     * @throws DataAccessException
     */
    public User addUser(String slackId) throws DataAccessException {
        logger.debug("Adding user with Slack ID: " + slackId);

        User user = new User()
            .setSlackId(slackId)
            .setCreatedTime(Date.from(Instant.now()));

        try {
            return userRepository.save(user);
        } catch (DataAccessException e) {
            logger.warn("Failed to create user with Slack ID: " + slackId +
                        " message: \"" + e.getMessage() + "\"");
            throw e;
        }
    }

    public User getUser(String slackId) {
        logger.debug("Fetching user with Slack ID: " + slackId);
        Optional<User> user = userRepository.findBySlackId(slackId);

        return user.orElseGet(() -> addUser(slackId));
    }
}
