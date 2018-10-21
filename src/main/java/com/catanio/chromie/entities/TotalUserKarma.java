package com.catanio.chromie.entities;

/**
 * Represents the total karma for a user.
 */
public class TotalUserKarma {
    private User user;
    private Long points;

    public TotalUserKarma(User user, Long points) {
        this.user = user;
        this.points = points;
    }

    public User getUser() {
        return user;
    }

    public TotalUserKarma setUser(User user) {
        this.user = user;
        return this;
    }

    public Long getPoints() {
        return points;
    }

    public TotalUserKarma setPoints(Long points) {
        this.points = points;
        return this;
    }
}
