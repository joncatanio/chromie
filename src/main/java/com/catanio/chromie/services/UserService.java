package com.catanio.chromie.services;

import com.catanio.chromie.entities.User;

public interface UserService {
    User addUser(String slackId);
    User getUser(String slackId);
}
