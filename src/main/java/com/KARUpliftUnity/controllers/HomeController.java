package com.KARUpliftUnity.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    @GetMapping("/")
    public String returnHomePage(){
        return "index";
    }

    @GetMapping("/mission")
    public String showMissionPage() {
        return "mission"; // Assuming you have a Thymeleaf template named "mission.html"
    }

    @GetMapping("/contact_us")
    public String showContactUsPage() {
        return "contact_us"; // Assuming you have a Thymeleaf template named "contact_us.html"
    }
}
