package com.KARUpliftUnity.controllers;

import com.KARUpliftUnity.models.*;
import com.KARUpliftUnity.repositories.CategoryRepository;
import com.KARUpliftUnity.repositories.PostRepository;
import com.KARUpliftUnity.repositories.TagRepository;
import com.KARUpliftUnity.repositories.UserRepository;
import com.KARUpliftUnity.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class PostController {

    private final PostRepository postDao;

    private final UserRepository userDao;

    private final TagRepository tagDao;

    private final EmailService emailService;

    @Autowired
    private CategoryRepository categoryRepository;


    public PostController(PostRepository postDao, UserRepository userDao, TagRepository tagDao, EmailService emailService) {
        this.postDao = postDao;
        this.userDao = userDao;
        this.tagDao = tagDao;
        this.emailService = emailService;
    }

    @GetMapping("/posts")
    public String index(Model model) {
        model.addAttribute("posts", postDao.findAll());
        return "/feed";
    }


    @GetMapping("/posts/{id}")
    public String postId(@PathVariable long id, Model model) {
        model.addAttribute("post", postDao.getById(id));
        return "posts/show";
    }

    @GetMapping("/posts/{id}/edit")
    public String postEdit(@PathVariable long id, Model model) {
        Post post = postDao.getById(id);
        model.addAttribute("post", post);

        List<Category> allCategories = categoryRepository.findAll();
        model.addAttribute("allCategories", allCategories);

        // get selected categories
        List<Long> selectedCategories = new ArrayList<>();
        for (Category category : post.getCategories()) {
            selectedCategories.add(category.getId());
        }
        model.addAttribute("selectedCategories", selectedCategories);

        // get tags
        String tags = "";
        for (Tag tag : post.getTags()) {
            if (!tags.isEmpty()) {
                tags += ", ";
            }
            tags += tag.getTag();
        }
        model.addAttribute("tagString", tags);

        return "posts/edit";
    }

    @PostMapping("/posts/{id}/edit")
    public String editPost(@PathVariable long id,
                           @ModelAttribute Post editedPost,
                           @RequestParam(name = "selectedCategories") List<Long> selectedCategories) {

        Post existingPost = postDao.getById(id);

        existingPost.setTitle(editedPost.getTitle());
        existingPost.setStory(editedPost.getStory());
        existingPost.setSolution(editedPost.getSolution());

        List<Category> categories = categoryRepository.findAllById(selectedCategories);
        existingPost.setCategories(categories);

        List<Tag> existingTags = existingPost.getTags();

        // Convert tags to lowercase and filter duplicates
        String[] rawTags = editedPost.getTagString().split(",\\s*");
        List<String> newTags = new ArrayList<>();
        for (String rawTag : rawTags) {
            String cleanedTag = rawTag.trim().toLowerCase();
            if (!newTags.contains(cleanedTag)) {
                newTags.add(cleanedTag);
            }
        }

        // Add new tags
        for (String newTag : newTags) {
            boolean tagExists = false;
            for (Tag existingTag : existingTags) {
                if (existingTag.getTag().equals(newTag)) {
                    tagExists = true;
                    break;
                }
            }
            if (!tagExists) {
                Tag tag = new Tag(newTag, existingPost);
                tagDao.save(tag);
            }
        }

        // Remove tags that are no longer associated
        List<Tag> tagsToRemove = new ArrayList<>();
        for (Tag existingTag : existingTags) {
            if (!newTags.contains(existingTag.getTag())) {
                tagsToRemove.add(existingTag);
            }
        }

        for (Tag tag : tagsToRemove) {
            existingTags.remove(tag);
            tagDao.delete(tag);
        }

        postDao.save(existingPost);

        return "redirect:/posts/" + id;
    }


    @GetMapping("/posts/create")
    public String postsCreate(Model model) {
        model.addAttribute("post", new Post());
        List<Category> allCategories = categoryRepository.findAll();
        model.addAttribute("allCategories", allCategories);
        return "posts/create";
    }


    @PostMapping("/posts/create")
    public String createPost(@ModelAttribute Post post,
                             @RequestParam(name = "tagString") String tagString,
                             @RequestParam(name = "selectedCategories") List<Long> selectedCategories){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        post.setUser(user);

        post.setDate(new Date());

        List<Category> categories = categoryRepository.findAllById(selectedCategories);
        post.setCategories(categories);

        postDao.save(post);
        long postId = postDao.getIdByTitle(post.getTitle()).getId();
        Post postInfo = postDao.getById(postId);

        String[] uniqueTags = Arrays.stream(tagString.toLowerCase().split(",\\s*"))
                .distinct()
                .toArray(String[]::new);

        for (String tag : uniqueTags){
            Tag tagEntity = new Tag(tag, postInfo);
            tagDao.save(tagEntity);
        }

        System.out.println("postId = " + postId);
        emailService.prepareAndSend(post, user.getUsername()
                + " thanks for creating a post!", user.getUsername()
                + " thanks for creating the post: " + post.getTitle() + "You can view your post at https://upliftunity.com/posts/" + postId);
        return "redirect:/posts";
    }
}
