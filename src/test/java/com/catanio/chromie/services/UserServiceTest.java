package com.catanio.chromie.services;

import com.catanio.chromie.BaseTest;
import com.catanio.chromie.entities.User;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserServiceTest extends BaseTest {

    @Autowired
    private UserService userService;

    @Test
    public void testAddGetUser() {
        User newUser = userService.addUser("NYY99");
        User fetchedUser = userService.getUser("NYY99");

        Assert.assertEquals(newUser.getId(), fetchedUser.getId());
        Assert.assertEquals(newUser.getSlackId(), fetchedUser.getSlackId());
        Assert.assertEquals(newUser.getDeleted(), fetchedUser.getDeleted());
        Assert.assertEquals(newUser.getCreatedTime(), fetchedUser.getCreatedTime());
        Assert.assertEquals(newUser.getModifiedTime(), fetchedUser.getModifiedTime());
    }
}
