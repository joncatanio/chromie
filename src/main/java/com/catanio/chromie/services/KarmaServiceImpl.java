package com.catanio.chromie.services;

import com.catanio.chromie.dm.entities.Karma;
import com.catanio.chromie.dm.entities.User;
import com.catanio.chromie.dm.repositories.KarmaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
public class KarmaServiceImpl implements KarmaService {

    private ApplicationContext context;

    private UserService userService;

    private KarmaRepository karmaRepository;

    @Autowired
    public KarmaServiceImpl(ApplicationContext context,
                            UserServiceImpl userService,
                            KarmaRepository karmaRepository) {
        this.context = context;
        this.userService = userService;
        this.karmaRepository = karmaRepository;
    }

    /**
     * Updates a user's karma points and returns their total karma.
     *
     * @param donorSlackId     the Slack ID of the karma donor
     * @param recipientSlackId the Slack ID of the karma recipient
     * @param points           the karma points to distribute (positive or negative)
     * @return the karma recipient's updated karma score
     */
    public Long updateKarma(String donorSlackId, String recipientSlackId, Integer points) {
        User donor = userService.getUser(donorSlackId);
        User recipient = userService.getUser(recipientSlackId);

        Karma karma = new Karma()
                .setRecipient(recipient)
                .setDonor(donor)
                .setPoints(points)
                .setCreatedTime(Date.from(Instant.now()));

        karmaRepository.save(karma);

        return karmaRepository.sumKarmaForSlackId(recipientSlackId);
    }
}
