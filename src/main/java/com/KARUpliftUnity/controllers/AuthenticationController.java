package com.KARUpliftUnity.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthenticationController {

    @GetMapping("/login")
    public String showLoginForm(HttpServletRequest request, HttpSession session) {

        String referer = request.getHeader("Referer");

        if(referer != null) {
            session.setAttribute("redirectUrl", referer);
        }

        return "login/login";
    }

    @PostMapping("/login")
    public String processLogin(HttpSession session){

        String redirectUrl = (String)session.getAttribute("redirectUrl");

        if(redirectUrl != null){
            return "redirect:" + redirectUrl;
        }

        return "redirect:/";
    }
}
