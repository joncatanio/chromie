package com.catanio.chromie.dm.repositories;

import com.catanio.chromie.BaseTest;
import com.catanio.chromie.dm.entities.User;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;

public class UserRepositoryTest extends BaseTest {
    @Autowired
    UserRepository userRepository;

    @Test
    public void saveAndFindUser() {
        Instant createdTime = Instant.now();
        User expected = new User()
            .setSlackId("NYY99")
            .setCreatedTime(createdTime);

        userRepository.save(expected);
        User actual = userRepository.findBySlackId("NYY99");

        Assert.assertEquals(expected.getSlackId(), actual.getSlackId());
        Assert.assertEquals(false, actual.getDeleted());
        Assert.assertEquals(expected.getCreatedTime(), actual.getCreatedTime());
        Assert.assertEquals(null, actual.getModifiedTime());
    }
}
