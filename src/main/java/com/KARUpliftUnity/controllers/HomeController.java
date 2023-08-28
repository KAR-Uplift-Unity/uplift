package com.KARUpliftUnity.controllers;

import com.KARUpliftUnity.models.ContactForm;
import com.KARUpliftUnity.models.Post;
import com.KARUpliftUnity.repositories.*;
import com.KARUpliftUnity.models.User;
import com.KARUpliftUnity.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


import java.util.regex.Pattern;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Controller
public class HomeController {
    private final PostRepository postDao;

    private final UserRepository userDao;

    private final TagRepository tagDao;


    public HomeController(PostRepository postDao, UserRepository userDao, TagRepository tagDao, EmailService emailService) {
        this.postDao = postDao;
        this.userDao = userDao;
        this.tagDao = tagDao;
        this.emailService = emailService;
    }
    @Autowired
    private EmailService emailService;


    @GetMapping("/")
    public String returnHomePage(Model model) {
        List<Post> allPosts = postDao.findByArchiveFalse();
        Collections.shuffle(allPosts);
        int maxPosts = 6;
        int numPosts = Math.min(maxPosts, allPosts.size());
        List<Integer> randomIndices = new ArrayList<>();
        for (int i = 0; i < numPosts; i++) {
            int randomIndex = new Random().nextInt(allPosts.size());
            while (randomIndices.contains(randomIndex)) {
                randomIndex = new Random().nextInt(allPosts.size());
            }
            randomIndices.add(randomIndex);
        }
        List<Post> randomPosts = new ArrayList<>();
        for (int index : randomIndices) {
            randomPosts.add(allPosts.get(index));
        }

        List<Post> posts = new ArrayList<>();
        for (Post post: randomPosts){
            if (!post.isFlagged()){
                posts.add(post);
            }
        }
        model.addAttribute("posts", posts);
        return "index";
    }

    @GetMapping("/mission")
    public String showMissionPage(Model model) {
        return "mission";
    }

    @GetMapping("/contact_us")
    public String showContactUsPage(Model model) {
        model.addAttribute("contactForm", new ContactForm());
        return "contact_us";
    }

    @PostMapping("/contact_us")
    public String handleContactUsForm(@ModelAttribute ContactForm form, Model model) {
        // Email validation
        Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
        if (!emailPattern.matcher(form.getEmail()).matches()) {
            model.addAttribute("emailError", "Please provide a valid email address");
            return "contact_us";
        }

        emailService.sendContactFormEmail(form);
        return "redirect:/contact_us?messageSent=true";
    }
}
