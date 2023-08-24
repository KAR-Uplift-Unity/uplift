package com.KARUpliftUnity.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthenticationController {
    @GetMapping("/login")
    public String showLoginForm(HttpServletRequest request) {
        String previousUrl = (String) request.getSession().getAttribute("previousUrl");
        System.out.println("Previous URL: " + previousUrl);
        if (previousUrl != null) {
            request.getSession().removeAttribute("previousUrl");
            return "redirect:" + previousUrl;
        }
        return "login/login";
    }
}
