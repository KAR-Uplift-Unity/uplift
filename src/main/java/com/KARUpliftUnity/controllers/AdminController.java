package com.KARUpliftUnity.controllers;

import com.KARUpliftUnity.models.Post;
import com.KARUpliftUnity.models.User;
import com.KARUpliftUnity.repositories.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class AdminController {

    private final PostRepository postDao;

    private final UserRepository userDao;

    private final UserRepository updateDao;

    private final TagRepository tagDao;

    private final LikeRepository likeDao;

    private final ImageRepository imgDao;

    private final CommentRepository comDao;

    private final CategoryRepository catDao;

    public AdminController(PostRepository postDao, UserRepository userDao, UserRepository updateDao, TagRepository tagDao, LikeRepository likeDao, ImageRepository imgDao, CommentRepository comDao, CategoryRepository catDao) {
        this.postDao = postDao;
        this.userDao = userDao;
        this.updateDao = updateDao;
        this.tagDao = tagDao;
        this.likeDao = likeDao;
        this.imgDao = imgDao;
        this.comDao = comDao;
        this.catDao = catDao;
    }

    @GetMapping("/admin-panel")
    public String showAdminPanel(Model model){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean admin  = user.getAdmin();
        if (admin) {
            List<User> allUsers = userDao.findAll();
            model.addAttribute("users", allUsers);
            return "admin/index";
        }
        return "redirect:/";
    }

    @PostMapping("/admin-panel")
    public String dropUser(long id){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean admin  = user.getAdmin();
        if (admin) {
            userDao.deleteUserById(id);
            return "admin/index";
        }
        return "admin/index";
    }
}
