package com.KARUpliftUnity.models;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name="posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private Date date;
    @Column(nullable = false, length = 45)
    private String title;
    @Column(nullable = false)
    private String story;
    @Column(nullable = false)
    private String solution;
    @Column
    private boolean flagged;
    @Column
    private boolean archive;
    @ManyToOne
    @JoinColumn (name = "user_id")
    private User user;
    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "post")
    private List<Like> likes;
    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "post")
    private List<Comment> comment;
    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "post")
    private List<Update> updates;
    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "post")
    private List<Image> images;
    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "post")
    private List<Tag> tags;

    @Transient
    private String tagString;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name="post_categories",
            joinColumns={@JoinColumn(name="post_id")},
            inverseJoinColumns={@JoinColumn(name="cat_id")}
    )
    private List<Category> categories;


    public Post(long id, Date date, String title, String story, String solution, boolean archive, User user) {
        this.id = id;
        this.date = date;
        this.title = title;
        this.story = story;
        this.solution = solution;
        this.archive = archive;
        this.user = user;
    }

    public Post() {
    }

    public Post(Post post) {
        this.title = post.title;
        this.story = post.story;
        this.solution = post.solution;
    }


    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public boolean getArchive() {
        return archive;
    }

    public void setArchive(boolean archive) {
        this.archive = archive;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isFlagged() {
        return flagged;
    }

    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }

    public long getIdByTitle(String title){
        return id;
    }

    public String getTagString() {
        return tagString;
    }

    public void setTagString(String tagString) {
        this.tagString = tagString;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }
}

