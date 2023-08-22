package com.KARUpliftUnity.controllers;

import com.KARUpliftUnity.models.*;
import com.KARUpliftUnity.repositories.PostRepository;
import com.KARUpliftUnity.repositories.TagRepository;
import com.KARUpliftUnity.repositories.UserRepository;
import com.KARUpliftUnity.services.EmailService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PostController {

    private final PostRepository postDao;

    private final UserRepository userDao;

    private final TagRepository tagDao;

    private final EmailService emailService;

    public PostController(PostRepository postDao, UserRepository userDao, TagRepository tagDao, EmailService emailService) {
        this.postDao = postDao;
        this.userDao = userDao;
        this.tagDao = tagDao;
        this.emailService = emailService;
    }

    @GetMapping("/posts")
    public String index(Model model) {
        model.addAttribute("posts", postDao.findAll());
        return "posts/index";
    }

    @GetMapping("/feed")
    public String showFeed(Model model) {
        model.addAttribute("posts", postDao.findAll());
        return "feed";
    }

    @GetMapping("/posts/{id}")
    public String postId(@PathVariable long id, Model model) {
        model.addAttribute("post", postDao.getById(id));
        return "posts/show";
    }

    @GetMapping("/posts/{id}/edit")
    public String postEdit(@PathVariable long id, Model model) {
        model.addAttribute("post", postDao.getById(id));
        return "posts/edit";
    }


    @GetMapping("/posts/create")
    public String postsCreate(Model model) {
        model.addAttribute("post", new Post());
        return "posts/create";
    }

    @PostMapping("/posts/create")
    public String createPost(@ModelAttribute Post post, @RequestParam(name = "tags") String[] tags){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        post.setUser(user);
        postDao.save(post);
        long postId = postDao.getIdByTitle(post.getTitle()).getId();
        Post postInfo = postDao.getById(postId);
        for (String x: tags){
            Tag tag = new Tag(x, postInfo);
            tagDao.save(tag);
        }
        System.out.println("postId = " + postId);
        emailService.prepareAndSend(post, user.getUsername()
                + " thanks for creating a post!", user.getUsername()
                + " thanks for creating the post: " + post.getTitle() + "You can view your post at https://upliftunity.com/posts/" + postId);
        return "redirect:/posts";
    }
}
