package com.catanio.chromie.dm.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "karma")
public class Karma {

    public Karma() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",
            unique = true,
            nullable = false)
    private Long id;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "recipient",
            nullable = false,
            updatable = false)
    private User recipient;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "donor",
            nullable = false,
            updatable = false)
    private User donor;

    @Column(name = "points",
            nullable = false)
    private Integer points;

    @Column(name = "created_time",
            columnDefinition = "DATETIME",
            nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTime;

    public Long getId() {
        return id;
    }

    public Karma setId(Long id) {
        this.id = id;
        return this;
    }

    public User getRecipient() {
        return recipient;
    }

    public Karma setRecipient(User recipient) {
        this.recipient = recipient;
        return this;
    }

    public User getDonor() {
        return donor;
    }

    public Karma setDonor(User donor) {
        this.donor = donor;
        return this;
    }

    public Integer getPoints() {
        return points;
    }

    public Karma setPoints(Integer points) {
        this.points = points;
        return this;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public Karma setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
        return this;
    }

    @Override
    public String toString() {
        return "Karma{" +
                "id=" + id +
                ", recipient=" + recipient +
                ", donor=" + donor +
                ", points=" + points +
                ", createdTime=" + createdTime +
                '}';
    }
}
