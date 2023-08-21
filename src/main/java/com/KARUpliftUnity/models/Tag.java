package com.KARUpliftUnity.models;

import jakarta.persistence.*;

@Entity
@Table(name = "tags")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false, length = 45)
    private String tag;
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    public Tag() {
    }

    public Tag(long id, String tag, Post post) {
        this.id = id;
        this.tag = tag;
        this.post = post;
    }

    public Tag(String tag, Post post) {
        this.tag = tag;
        this.post = post;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", tag='" + tag + '\'' +
                ", post=" + post +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
