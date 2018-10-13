package com.catanio.chromie.dm.repositories;

import com.catanio.chromie.BaseTest;
import com.catanio.chromie.dm.entities.User;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;

import java.time.Instant;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
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

    @Test
    public void markUserAsDeleted() {
        Instant createdTime = Instant.now();
        Instant modifiedTime = Instant.now();
        User expected = new User()
                .setSlackId("NYY99")
                .setCreatedTime(createdTime);

        userRepository.save(expected);
        expected.setDeleted(true)
            .setModifiedTime(modifiedTime);
        userRepository.save(expected);

        User actual = userRepository.findBySlackId("NYY99");
        Assert.assertEquals(expected.getSlackId(), actual.getSlackId());
        Assert.assertEquals(true, actual.getDeleted());
        Assert.assertEquals(expected.getCreatedTime(), actual.getCreatedTime());
        Assert.assertEquals(modifiedTime, actual.getModifiedTime());
    }
}
