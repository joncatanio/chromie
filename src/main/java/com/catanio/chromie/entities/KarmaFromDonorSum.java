package com.catanio.chromie.entities;

/**
 * Represents a record for the total points that a donor has granted the recipient
 */
public class KarmaFromDonorSum {
    private User recipient;
    private User donor;
    private Long totalPoints;

    public KarmaFromDonorSum(User recipient,
                             User donor,
                             Long totalPoints) {
        this.recipient = recipient;
        this.donor = donor;
        this.totalPoints = totalPoints;
    }

    public User getRecipient() {
        return recipient;
    }

    public KarmaFromDonorSum setRecipient(User recipient) {
        this.recipient = recipient;
        return this;
    }

    public User getDonor() {
        return donor;
    }

    public KarmaFromDonorSum setDonor(User donor) {
        this.donor = donor;
        return this;
    }

    public Long getTotalPoints() {
        return totalPoints;
    }

    public KarmaFromDonorSum setTotalPoints(Long totalPoints) {
        this.totalPoints = totalPoints;
        return this;
    }
}