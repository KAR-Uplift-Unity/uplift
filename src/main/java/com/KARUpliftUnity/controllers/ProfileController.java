package com.KARUpliftUnity.controllers;

import com.KARUpliftUnity.models.User;
import com.KARUpliftUnity.repositories.PostRepository;
import com.KARUpliftUnity.repositories.TagRepository;
import com.KARUpliftUnity.repositories.UserRepository;
import com.KARUpliftUnity.services.EmailService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfileController {

    private final PostRepository postDao;

    private final UserRepository userDao;

    public ProfileController(PostRepository postDao, UserRepository userDao) {
        this.postDao = postDao;
        this.userDao = userDao;
    }

    @GetMapping("/profile")
    public String showProfile(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean admin  = user.getAdmin();
        if (admin) {
            model.addAttribute("admin", 1);
        }
        model.addAttribute("posts", postDao.getAllByUserId(user.getId()));
        return "/users/profile";
    }

    @GetMapping("/settings")
    public String showProfileSettings(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean admin  = user.getAdmin();
        if (admin) {
            model.addAttribute("admin", 1);
        }
        return "/users/profile_settings";
    }
}
