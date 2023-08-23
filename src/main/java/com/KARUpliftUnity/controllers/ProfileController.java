package com.KARUpliftUnity.controllers;

import com.KARUpliftUnity.models.Post;
import com.KARUpliftUnity.models.User;
import com.KARUpliftUnity.repositories.PostRepository;
import com.KARUpliftUnity.repositories.TagRepository;
import com.KARUpliftUnity.repositories.UserRepository;
import com.KARUpliftUnity.services.EmailService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.regex.Pattern;

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
        model.addAttribute("username", user.getUsername());
        model.addAttribute("activePosts", postDao.findByUserIdAndArchiveFalse(user.getId()));
        model.addAttribute("archivedPosts", postDao.findByUserIdAndArchiveTrue(user.getId()));
        return "/users/profile";
    }

    @PostMapping("/posts/{id}/archive")
    public String archivePost(@PathVariable long id) {
        Post post = postDao.findById(id).orElse(null);
        if (post != null) {
            post.setArchive(true);
            postDao.save(post);
        }
        return "redirect:/profile";
    }

    @PostMapping("/posts/{id}/unarchive")
    public String unarchivePost(@PathVariable long id) {
        Post post = postDao.findById(id).orElse(null);
        if (post != null) {
            post.setArchive(false);
            postDao.save(post);
        }
        return "redirect:/profile";
    }

    @GetMapping("/settings")
    public String showProfileSettings(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean admin  = user.getAdmin();
        if (admin) {
            model.addAttribute("admin", 1);
        }
        model.addAttribute("username", user.getUsername());
        model.addAttribute("email", user.getEmail());
        return "/users/profile_settings";
    }
}
