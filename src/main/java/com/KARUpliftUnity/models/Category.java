package com.KARUpliftUnity.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false, length = 45)
    private String cat;
    @ManyToMany(cascade = CascadeType.PERSIST, mappedBy = "categories")
    private List<Post> post;

    public Category() {
    }

    public Category(long id, String cat, List<Post> post) {
        this.id = id;
        this.cat = cat;
        this.post = post;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", cat='" + cat + '\'' +
                ", postCat=" + post +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public List<Post> getPostCat() {
        return post;
    }

    public void setPostCat(List<Post> post) {
        this.post = post;
    }
}
