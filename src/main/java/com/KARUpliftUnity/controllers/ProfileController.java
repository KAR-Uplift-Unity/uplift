package com.KARUpliftUnity.controllers;

import com.KARUpliftUnity.models.Post;
import com.KARUpliftUnity.models.User;
import com.KARUpliftUnity.repositories.PostRepository;
import com.KARUpliftUnity.repositories.TagRepository;
import com.KARUpliftUnity.repositories.UserRepository;
import com.KARUpliftUnity.services.EmailService;
import com.KARUpliftUnity.services.UserDetailsLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Controller
public class ProfileController {

    private final PostRepository postDao;

    private final UserRepository userDao;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private UserDetailsLoader userDetailsLoader;

    @Value("${filestack.key}")
    private String fileStackKey;


    public ProfileController(PostRepository postDao, UserRepository userDao) {
        this.postDao = postDao;
        this.userDao = userDao;
    }

    @GetMapping("/profile")
    public String showProfile(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean admin  = user.getAdmin();
        if (admin) {
            model.addAttribute("admin", 1);
        }

        String imageUrl = user.getProfileImageUrl();
        if (imageUrl == null || imageUrl.isEmpty()) {
            imageUrl = "/images/default-image.png";
        }

        List<Post> posts = postDao.findByUserIdAndArchiveTrue(user.getId());
        Date curTime = new Date();

        long curTimeLong = curTime.getTime();

        for (Post post: posts){
            long archiveTime = 0;
            if (post.getArchive()){
                archiveTime = post.getArchiveDate().getTime();
                System.out.println("archiveTime = " + archiveTime);
            }
            long totalTime = curTimeLong - archiveTime;

            if (totalTime <= 86400000 && totalTime > 0){
                post.setDays(7);
                postDao.save(post);
            } else if (totalTime <= 172800000 && totalTime > 86400001){
                post.setDays(6);
                postDao.save(post);
            } else if (totalTime <= 259200000 && totalTime > 172800001){
                post.setDays(5);
                postDao.save(post);
            } else if (totalTime <= 345600000 && totalTime > 259200001){
                post.setDays(4);
                postDao.save(post);
            } else if (totalTime <= 432000000 && totalTime > 345600001){
                post.setDays(3);
                postDao.save(post);
            } else if (totalTime <= 518400000 && totalTime > 432000001){
                post.setDays(2);
                postDao.save(post);
            } else if (totalTime <= 604800000 && totalTime > 518400001){
                post.setDays(1);
                postDao.save(post);
            }
        }

        model.addAttribute("profileImage", imageUrl);

        model.addAttribute("user", user);
        model.addAttribute("activePosts", postDao.findByUserIdAndArchiveFalse(user.getId()));
        model.addAttribute("archivedPosts", posts);

        return "users/profile";
    }

    @PostMapping("/posts/{id}/archive")
    public String archivePost(@PathVariable long id) {
        Post post = postDao.findById(id).orElse(null);
        if (post != null) {
            post.setArchive(true);
            post.setArchiveDate(new Date());
            postDao.save(post);
        }
        return "redirect:/profile";
    }

    @PostMapping("/posts/{id}/unarchive")
    public String unarchivePost(@PathVariable long id) {
        Post post = postDao.findById(id).orElse(null);
        if (post != null) {
            post.setArchive(false);
            post.setArchiveDate(null);
            post.setDays(0);
            postDao.save(post);
        }
        return "redirect:/profile";
    }

    @GetMapping("/settings")
    public String showProfileSettings(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean admin  = user.getAdmin();
        if (admin) {
            model.addAttribute("admin", 1);
        }

        String imageUrl = user.getProfileImageUrl();
        if (imageUrl == null || imageUrl.isEmpty()) {
            imageUrl = "/images/default-image.png";
        }

        model.addAttribute("fileStackKey", fileStackKey);
        model.addAttribute("profileImage", imageUrl);
        model.addAttribute("user", user);
        return "users/profile_settings";
    }

    @PostMapping("/update-details")
    public String updateUserDetails(@RequestParam String username, @RequestParam String email, RedirectAttributes redirectAttributes, Model model) {
        if (username.length() == 0 || email.length() == 0) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please fill in all fields.");
            return "redirect:/settings";
        }

        // Username validation
        if (username.length() < 4) {
            model.addAttribute("usernameError", "Username should be at least 4 characters long");
            return "settings";
        }

        // Email validation
        Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
        if (!emailPattern.matcher(email).matches()) {
            model.addAttribute("emailError", "Please provide a valid email address");
            return "settings";
        }

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userToUpdate = userDao.findById(user.getId()).orElse(null);
        User userWithSameUsername = userDao.findByUsername(username);
        User userWithSameEmail = userDao.findByEmail(email);

        // Check if another user has the same username or email
        if (userWithSameUsername != null && userWithSameUsername.getId() != user.getId()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Username already in use. Please choose another.");
            return "redirect:/settings";
        }

        if (userWithSameEmail != null && userWithSameEmail.getId() != user.getId()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Email already in use. Please choose another.");
            return "redirect:/settings";
        }


        if (userToUpdate != null) {
            userToUpdate.setUsername(username);
            userToUpdate.setEmail(email);
            userDao.save(userToUpdate);
            redirectAttributes.addFlashAttribute("successMessage", "User details updated successfully.");

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            UserDetails updatedUserDetails = userDetailsLoader.loadUserByUsername(userToUpdate.getUsername());
            Authentication newAuth = new UsernamePasswordAuthenticationToken(updatedUserDetails, auth.getCredentials(), updatedUserDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(newAuth);

        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating user details.");
        }
        return "redirect:/settings";
    }

    @PostMapping("/update-password")
    public String updatePassword(@RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmNewPassword,
                                 RedirectAttributes redirectAttributes) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userToUpdate = userDao.findById(user.getId()).orElse(null);

        if (userToUpdate == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "User not found.");
            return "redirect:/settings";
        }

        Pattern passwordPattern = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}$");
        if (!passwordPattern.matcher(newPassword).matches()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Password should have at least 8 characters, 1 uppercase, 1 lowercase, and 1 number");
            return "redirect:/settings";
        }

        if (!passwordEncoder.matches(currentPassword, userToUpdate.getPassword())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Current password is incorrect.");
            return "redirect:/settings";
        }

        if (!newPassword.equals(confirmNewPassword)) {
            redirectAttributes.addFlashAttribute("errorMessage", "New password and confirm password do not match.");
            return "redirect:/settings";
        }

        userToUpdate.setPassword(passwordEncoder.encode(newPassword));
        userDao.save(userToUpdate);
        redirectAttributes.addFlashAttribute("successMessage", "Password updated successfully.");

        return "redirect:/settings";
    }

    @PostMapping("/update-profile-image")
    public ResponseEntity<Map<String, String>> updateProfileImage(@RequestBody Map<String, String> body, Principal principal) {
        User user = userDao.findByUsername(principal.getName());

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "User not found"));
        }

        user.setProfileImageUrl(body.get("imageUrl"));
        userDao.save(user);

        User updatedUser = userDao.findByUsername(principal.getName());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Authentication newAuth = new UsernamePasswordAuthenticationToken(updatedUser, auth.getCredentials(), auth.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(newAuth);

        return ResponseEntity.ok(Map.of("message", "Profile image updated successfully"));
    }
}
