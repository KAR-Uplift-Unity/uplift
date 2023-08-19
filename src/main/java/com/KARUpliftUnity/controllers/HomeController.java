package com.KARUpliftUnity.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    @GetMapping("/")
    public String returnHomePage(Model model){
        model.addAttribute("pageTitle", "UpliftUnity");
        return "index";
    }
}
