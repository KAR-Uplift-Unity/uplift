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
    @Column(nullable = false, length = 75)
    private String title;
    @Column(nullable = false, length = 5000)
    private String story;
    @Column(nullable = false, length = 2500)
    private String solution;
    @Column(columnDefinition = "false")
//    @Column
    private boolean flagged;
    @Column
    private String reportReason;
    @Column(columnDefinition = "false")
//    @Column
    private boolean archive;
    @Column
    private Date archiveDate;
    @Column(columnDefinition = "integer default 0")
//    @Column
    private Integer days;
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

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(
            name="post_categories",
            joinColumns={@JoinColumn(name="post_id")},
            inverseJoinColumns={@JoinColumn(name="cat_id")}
    )
    private List<Category> categories;


    public Post(long id, Date date, String title, String story, String solution, boolean flagged, String reportReason, boolean archive, Date archiveDate, int days, User user, List<Like> likes, List<Comment> comment, List<Update> updates, List<Image> images, List<Tag> tags, String tagString, List<Category> categories) {
        this.id = id;
        this.date = date;
        this.title = title;
        this.story = story;
        this.solution = solution;
        this.flagged = flagged;
        this.reportReason = reportReason;
        this.archive = archive;
        this.archiveDate = archiveDate;
        this.days = days;
        this.user = user;
        this.likes = likes;
        this.comment = comment;
        this.updates = updates;
        this.images = images;
        this.tags = tags;
        this.tagString = tagString;
        this.categories = categories;
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

    public String getReportReason() {
        return reportReason;
    }

    public void setReportReason(String reportReason) {
        this.reportReason = reportReason;
    }

    public boolean isArchive() {
        return archive;
    }

    public Date getArchiveDate() {
        return archiveDate;
    }

    public void setArchiveDate(Date archiveDate) {
        this.archiveDate = archiveDate;
    }

    public List<Like> getLikes() {
        return likes;
    }

    public void setLikes(List<Like> likes) {
        this.likes = likes;
    }

    public List<Comment> getComment() {
        return comment;
    }

    public void setComment(List<Comment> comment) {
        this.comment = comment;
    }

    public List<Update> getUpdates() {
        return updates;
    }

    public void setUpdates(List<Update> updates) {
        this.updates = updates;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }
}

