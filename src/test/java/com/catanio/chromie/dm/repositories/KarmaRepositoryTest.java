package com.catanio.chromie.dm.repositories;

import com.catanio.chromie.BaseTest;
import com.catanio.chromie.entities.Karma;
import com.catanio.chromie.entities.KarmaFromDonorSum;
import com.catanio.chromie.entities.User;
import com.catanio.chromie.repositories.KarmaRepository;
import com.catanio.chromie.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Before;
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

    @Before
    public void init() {
        Date createdTime = Date.from(Instant.now());

        // Create at least two users to be able to add karma records.
        User user1 = new User()
            .setSlackId("NYY99")
            .setCreatedTime(createdTime);

        User user2 = new User()
            .setSlackId("OAK24")
            .setCreatedTime(createdTime);

        User user3 = new User()
            .setSlackId("SFG25")
            .setCreatedTime(createdTime);

        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        users.add(user3);
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

        Karma karmaRecord4 = new Karma()
            .setRecipient(user1)
            .setDonor(user3)
            .setPoints(9)
            .setCreatedTime(createdTime);

        Karma karmaRecord5 = new Karma()
            .setRecipient(user1)
            .setDonor(user3)
            .setPoints(-2)
            .setCreatedTime(createdTime);

        List<Karma> karmaRecords = new ArrayList<>();
        karmaRecords.add(karmaRecord1);
        karmaRecords.add(karmaRecord2);
        karmaRecords.add(karmaRecord3);
        karmaRecords.add(karmaRecord4);
        karmaRecords.add(karmaRecord5);
        karmaRepository.save(karmaRecords);

    }

    @Test
    public void testGetKarmaForUser() {
        Long actualKarma = karmaRepository.sumKarmaForSlackId("NYY99");
        Assert.assertEquals(new Long(12), actualKarma);
    }

    @Test
    public void testGetDonorsSumForUser() {
        List<KarmaFromDonorSum> donorsSum = karmaRepository.sumKarmaFromDonorsFor("NYY99");
        KarmaFromDonorSum donor1 = donorsSum.get(0).getDonor().getSlackId().equals("OAK24")
            ? donorsSum.remove(0)
            : donorsSum.remove(1);
        KarmaFromDonorSum donor2 = donorsSum.remove(0);

        Assert.assertEquals("NYY99", donor1.getRecipient().getSlackId());
        Assert.assertEquals("OAK24", donor1.getDonor().getSlackId());
        Assert.assertEquals(new Long(5), donor1.getTotalPoints());

        Assert.assertEquals("NYY99", donor2.getRecipient().getSlackId());
        Assert.assertEquals("SFG25", donor2.getDonor().getSlackId());
        Assert.assertEquals(new Long(7), donor2.getTotalPoints());
    }
}
