package com.KARUpliftUnity.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private boolean admin;
    @Column(nullable = false, length = 100, unique = true)
    private String username;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column
    private boolean archive;
    @Column
    private String profileImageUrl;
    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "user")
    private List<Post> posts;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "user")
    private List<Like> likes;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "user")
    private List<Comment> comments;

    public User() {
    }

    public User(long id, boolean admin, String username, String email, String password, boolean archive,String profileImageUrl, List<Post> posts, List<Like> likes, List<Comment> comments) {
        this.id = id;
        this.admin = admin;
        this.username = username;
        this.email = email;
        this.password = password;
        this.archive = archive;
        this.profileImageUrl = profileImageUrl;
        this.posts = posts;
        this.likes = likes;
        this.comments = comments;
    }

    public User(User copy) {
        id = copy.id;
        admin = copy.admin;
        username = copy.username;
        email = copy.email;
        password = copy.password;
        archive = copy.archive;
        profileImageUrl = copy.profileImageUrl;
        posts = copy.posts;
        likes = copy.likes;
        comments = copy.comments;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getArchive() {
        return archive;
    }

    public void setArchive(boolean archive) {
        this.archive = archive;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public boolean getAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public List<Like> getLikes() {
        return likes;
    }

    public void setLikes(List<Like> likes) {
        this.likes = likes;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}

