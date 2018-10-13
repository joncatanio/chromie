package com.catanio.chromie.dm.entities;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "users")
public class User {

    public User() {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",
            unique = true,
            nullable = false)
    private Long id;

    @Column(name = "slack_id",
            unique = true,
            nullable = false)
    private String slackId;

    @Column(name = "deleted",
            nullable = false)
    private Boolean deleted = false;

    @Column(name = "created_time",
            nullable = false)
    private Instant createdTime;

    @Column(name = "modified_time")
    private Instant modifiedTime;

    public Long getId() {
        return id;
    }

    public User setId(Long id) {
        this.id = id;
        return this;
    }

    public String getSlackId() {
        return slackId;
    }

    public User setSlackId(String slackId) {
        this.slackId = slackId;
        return this;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public User setDeleted(Boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public Instant getCreatedTime() {
        return createdTime;
    }

    public User setCreatedTime(Instant createdTime) {
        this.createdTime = createdTime;
        return this;
    }

    public Instant getModifiedTime() {
        return modifiedTime;
    }

    public User setModifiedTime(Instant modifiedTime) {
        this.modifiedTime = modifiedTime;
        return this;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", slackId='" + slackId + '\'' +
                ", deleted=" + deleted +
                ", createdTime=" + createdTime +
                ", modifiedTime=" + modifiedTime +
                '}';
    }
}
