package com.catanio.chromie.dm.repositories;

import com.catanio.chromie.BaseTest;
import com.catanio.chromie.entities.Karma;
import com.catanio.chromie.entities.User;
import com.catanio.chromie.repositories.KarmaRepository;
import com.catanio.chromie.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class KarmaRepositoryTest extends BaseTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    KarmaRepository karmaRepository;

    @Test
    public void testGetKarmaForUser() {
        Date createdTime = Date.from(Instant.now());

        // Create at least two users to be able to add karma records.
        User user1 = new User()
                .setSlackId("NYY99")
                .setCreatedTime(createdTime);

        User user2 = new User()
                .setSlackId("OAK24")
                .setCreatedTime(createdTime);

        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        userRepository.save(users);

        // Add karma records with both positive and negative points
        Karma karmaRecord1 = new Karma()
                .setRecipient(user1)
                .setDonor(user2)
                .setPoints(2)
                .setCreatedTime(createdTime);

        Karma karmaRecord2 = new Karma()
                .setRecipient(user1)
                .setDonor(user2)
                .setPoints(4)
                .setCreatedTime(createdTime);

        Karma karmaRecord3 = new Karma()
                .setRecipient(user1)
                .setDonor(user2)
                .setPoints(-1)
                .setCreatedTime(createdTime);

        List<Karma> karmaRecords = new ArrayList<>();
        karmaRecords.add(karmaRecord1);
        karmaRecords.add(karmaRecord2);
        karmaRecords.add(karmaRecord3);
        karmaRepository.save(karmaRecords);

        Long actualKarma = karmaRepository.sumKarmaForSlackId("NYY99");
        Assert.assertEquals(new Long(5), actualKarma);
    }
}
