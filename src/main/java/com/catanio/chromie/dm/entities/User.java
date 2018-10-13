package com.catanio.chromie.dm.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "users")
public class User {

    public User() {
    }

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
            columnDefinition = "TIMESTAMP",
            nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTime;

    @Column(name = "modified_time",
            columnDefinition = "TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedTime;

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

    public Date getCreatedTime() {
        return createdTime;
    }

    public User setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
        return this;
    }

    public Date getModifiedTime() {
        return modifiedTime;
    }

    public User setModifiedTime(Date modifiedTime) {
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
