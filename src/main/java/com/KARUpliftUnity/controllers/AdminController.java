package com.KARUpliftUnity.controllers;

import com.KARUpliftUnity.models.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {


    @GetMapping("/admin-panel")
    public String showAdminPanel(){
        User admin = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean isAdmin = admin.isAdmin();
        if (isAdmin) {
            return "admin/index";
        }
        return "/";
    }

}
