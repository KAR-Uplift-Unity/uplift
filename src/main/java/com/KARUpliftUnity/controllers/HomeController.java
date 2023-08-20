package com.KARUpliftUnity.controllers;

import com.KARUpliftUnity.models.ContactForm;
import com.KARUpliftUnity.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    @Autowired
    private EmailService emailService;

    @GetMapping("/")
    public String returnHomePage(){
        return "index";
    }

    @GetMapping("/mission")
    public String showMissionPage() {
        return "mission";
    }

    @GetMapping("/contact_us")
    public String showContactUsPage(Model model) {
        model.addAttribute("contactForm", new ContactForm());
        return "contact_us";
    }

    @PostMapping("/contact_us")
    public String handleContactUsForm(@ModelAttribute ContactForm form) {
        emailService.sendContactFormEmail(form);
        return "redirect:/contact_us?messageSent=true";
    }
}
