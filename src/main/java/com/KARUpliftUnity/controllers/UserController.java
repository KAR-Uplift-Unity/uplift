package com.KARUpliftUnity.controllers;

import com.KARUpliftUnity.models.User;
import com.KARUpliftUnity.repositories.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.regex.Pattern;

@Controller
public class UserController {
    private UserRepository userDao;
    private PasswordEncoder passwordEncoder;

    public UserController(UserRepository userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/signup")
    public String showSignupForm(Model model){
        model.addAttribute("user", new User());
        return "login/signup";
    }

    @PostMapping("/signup")
    public String saveUser(@ModelAttribute User user, @RequestParam String confirmPassword, Model model) {

        // Password match
        if (!user.getPassword().equals(confirmPassword)) {
            model.addAttribute("passwordMismatch", "Passwords do not match!");
            return "login/signup";
        }

        // Username
        if (user.getUsername().length() < 4) {
            model.addAttribute("usernameError", "Username should be at least 4 characters long");
            return "login/signup";
        }

        // Email
        Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
        if (!emailPattern.matcher(user.getEmail()).matches()) {
            model.addAttribute("emailError", "Please provide a valid email address");
            return "login/signup";
        }

        // Password
        Pattern passwordPattern = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}$");
        if (!passwordPattern.matcher(user.getPassword()).matches()) {
            model.addAttribute("passwordError", "Password should have at least 8 characters, 1 uppercase, 1 lowercase, and 1 number");
            return "login/signup";
        }

        // Check if username or email already exists
        User existingUserByUsername = userDao.findByUsername(user.getUsername());
        User existingUserByEmail = userDao.findByEmail(user.getEmail());

        if (existingUserByUsername != null) {
            model.addAttribute("usernameError", "Username already exists!");
            return "login/signup";
        }

        if (existingUserByEmail != null) {
            model.addAttribute("emailError", "Email already exists!");
            return "login/signup";
        }

        // Save user
        String hash = passwordEncoder.encode(user.getPassword());
        user.setPassword(hash);
        userDao.save(user);
        return "redirect:/login";
    }

    @PostMapping("/user/archive/{id}")
    public String archiveUser(@PathVariable long id, HttpServletRequest request) throws ServletException {
        User user = userDao.findUserById(id);
        user.setArchive(true);
        user.setArchiveDate(new Date());
        userDao.save(user);

        request.logout();
        return "redirect:/";
    }

}
