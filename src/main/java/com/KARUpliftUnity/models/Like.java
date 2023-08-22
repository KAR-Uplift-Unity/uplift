package com.KARUpliftUnity.models;

import jakarta.persistence.*;


@Entity
@Table(name = "hearts")
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private int heart;
    @ManyToOne
    @JoinColumn (name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn (name = "post_id")
    private Post post;

    public Like() {
    }

    public Like(long id, int heart, User user, Post post) {
        this.id = id;
        this.heart = heart;
        this.user = user;
        this.post = post;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getHeart() {
        return heart;
    }

    public void setHeart(int heart) {
        this.heart = heart;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
