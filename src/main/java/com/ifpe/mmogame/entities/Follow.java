package com.ifpe.mmogame.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    @ManyToOne
    private Character follower;
    @ManyToOne
    private Character following;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    //TODO: add Javadoc
    public Character getFollower() {
        return follower;
    }
    public void setFollower(Character follower) {
        this.follower = follower;
    }
    public Character getFollowing() {
        return following;
    }
    public void setFollowing(Character following) {
        this.following = following;
    }

}
