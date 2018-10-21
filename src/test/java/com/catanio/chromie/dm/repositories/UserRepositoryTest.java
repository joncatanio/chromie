package com.catanio.chromie.dm.repositories;

import com.catanio.chromie.BaseTest;
import com.catanio.chromie.entities.User;
import com.catanio.chromie.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;

import java.time.Instant;
import java.util.Date;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserRepositoryTest extends BaseTest {

    @Autowired
    UserRepository userRepository;

    @Test
    public void testSaveAndFindUser() {
        Date createdTime = Date.from(Instant.now());
        User expected = new User()
            .setSlackId("NYY99")
            .setCreatedTime(createdTime);

        userRepository.save(expected);
        User actual = userRepository.findBySlackId("NYY99").get();

        Assert.assertEquals(expected.getSlackId(), actual.getSlackId());
        Assert.assertEquals(false, actual.getDeleted());
        Assert.assertEquals(expected.getCreatedTime(), actual.getCreatedTime());
        Assert.assertEquals(null, actual.getModifiedTime());
    }

    @Test
    public void testMarkUserAsDeleted() {
        Date createdTime = Date.from(Instant.now());
        Date modifiedTime = Date.from(Instant.now());
        User expected = new User()
                .setSlackId("NYY99")
                .setCreatedTime(createdTime);

        userRepository.save(expected);
        expected.setDeleted(true)
            .setModifiedTime(modifiedTime);
        userRepository.save(expected);

        User actual = userRepository.findBySlackId("NYY99").get();
        Assert.assertEquals(expected.getSlackId(), actual.getSlackId());
        Assert.assertEquals(true, actual.getDeleted());
        Assert.assertEquals(expected.getCreatedTime(), actual.getCreatedTime());
        Assert.assertEquals(modifiedTime, actual.getModifiedTime());
    }
}
