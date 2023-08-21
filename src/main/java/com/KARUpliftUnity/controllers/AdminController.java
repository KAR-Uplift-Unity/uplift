package com.KARUpliftUnity.controllers;

import com.KARUpliftUnity.models.User;
import com.KARUpliftUnity.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {


    @GetMapping("/admin-panel")
    public String showAdminPanel(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean admin  = user.getAdmin();
        if (admin) {
            return "admin/index";
        }
        return "redirect:/";
    }
}
