package com.KARUpliftUnity.models;

import jakarta.persistence.*;

@Entity
@Table(name = "updates")
public class Update {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false, length = 45)
    private String title;
    @Column(nullable = false)
    private String description;
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    public Update() {
    }

    public Update(long id, String title, String description, Post post) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.post = post;
    }

    @Override
    public String toString() {
        return "Update{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", post=" + post +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
