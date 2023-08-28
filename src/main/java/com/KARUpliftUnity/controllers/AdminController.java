package com.KARUpliftUnity.controllers;

import com.KARUpliftUnity.models.*;
import com.KARUpliftUnity.repositories.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class AdminController {

    private final PostRepository postDao;

    private final UserRepository userDao;

    private final UpdateRepository updateDao;

    private final TagRepository tagDao;

    private final LikeRepository likeDao;

    private final ImageRepository imgDao;

    private final CommentRepository comDao;

    private final CategoryRepository catDao;

    public AdminController(PostRepository postDao, UserRepository userDao, UpdateRepository updateDao, TagRepository tagDao, LikeRepository likeDao, ImageRepository imgDao, CommentRepository comDao, CategoryRepository catDao) {
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
            List<Post> flagged = postDao.findAllByFlaggedIsTrue();
            model.addAttribute("users", allUsers);
            model.addAttribute("flagged",flagged);
            return "admin/index";
        }
        return "redirect:/";
    }

    @PostMapping ("/admin-panel/{id}")
    public String dropUser(@PathVariable long id){
        System.out.println("id = " + id);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean admin  = user.getAdmin();
        if (admin) {
            User userToDel = userDao.findUserById(id);
            List<Like> likes = likeDao.findAllByUser(userToDel);
            if (likes != null){
                likeDao.deleteAllInBatch(likes);
            }
            List<Comment> comments = comDao.findAllByUser(userToDel);
            if (comments != null){
                comDao.deleteAllInBatch(comments);
            }
            List<Post> usersPosts = postDao.findAllByUser(userToDel);
            if (usersPosts != null){
                for (Post post : usersPosts){
                    List<Image> images = imgDao.findAllByPost(post);
                    if (images != null){
                        imgDao.deleteAllInBatch(images);
                    }
                    List<Update> updates = updateDao.findAllByPost(post);
                    if (updates != null){
                        updateDao.deleteAllInBatch(updates);
                    }
                    List<Category> categories = catDao.findAllByPost(post);
                    if (categories != null){
                        catDao.deleteAllInBatch(categories);
                    }
                }
                postDao.deleteAllInBatch(usersPosts);
            }
            userDao.deleteById(id);
        }
        return "redirect:/admin-panel";
    }

    @PostMapping ("/admin-panel/posts/{id}")
    public String dropPost(@PathVariable long id){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean admin  = user.getAdmin();
        if (admin) {
            Post post = postDao.findPostById(id);

            List<Image> images = imgDao.findAllByPost(post);
            if (images != null){
                imgDao.deleteAllInBatch(images);
            }

            List<Update> updates = updateDao.findAllByPost(post);
            if (updates != null){
                updateDao.deleteAllInBatch(updates);
            }

            List<Category> categories = catDao.findAllByPost(post);
            if (categories != null){
                catDao.deleteAllInBatch(categories);
            }

            postDao.delete(post);
        }
        return "redirect:/admin-panel";
    }
}
