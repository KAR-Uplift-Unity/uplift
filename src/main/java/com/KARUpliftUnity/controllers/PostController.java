package com.KARUpliftUnity.controllers;

import com.KARUpliftUnity.models.*;
import com.KARUpliftUnity.repositories.*;
import com.KARUpliftUnity.services.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
public class PostController {

    private final PostRepository postDao;

    private final UserRepository userDao;

    private final ImageRepository imageDao;

    private final TagRepository tagDao;

    private final EmailService emailService;

    private final CommentRepository commentDao;

    private final CategoryRepository categoryRepository;

    private final LikeRepository likeDao;


    public PostController(PostRepository postDao, UserRepository userDao, ImageRepository imageDao, TagRepository tagDao, EmailService emailService, CommentRepository commentDao, CategoryRepository categoryRepository, LikeRepository likeDao) {
        this.postDao = postDao;
        this.userDao = userDao;
        this.imageDao = imageDao;
        this.tagDao = tagDao;
        this.emailService = emailService;
        this.commentDao = commentDao;
        this.categoryRepository = categoryRepository;
        this.likeDao = likeDao;
    }

    @GetMapping("/posts")
    public String index(Model model) {
        model.addAttribute("posts", postDao.findByArchiveFalse());
        return "feed";
    }


    @GetMapping("/posts/{id}")
    public String postId(@PathVariable long id, Model model) {
        try{
            Post post = postDao.getById(id);
            User postAuthor = post.getUser();
            List<Comment> comments = commentDao.findByPostOrderByIdAsc(post);
            List<Image> images = post.getImages();

            Like existingLike = null;
            User loggedInUser = null;

            try {
                loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                existingLike = likeDao.findByUserAndPost(loggedInUser, post);
            } catch (Exception e) {

            }

            boolean hasLiked = existingLike != null;

            int likeCount = likeDao.countByPost(post);

            String imageUrl = postAuthor.getProfileImageUrl();
            if (imageUrl == null || imageUrl.isEmpty()) {
                imageUrl = "/images/default-image.png";
            }

            model.addAttribute("post", post);
            model.addAttribute("comments", comments);
            model.addAttribute("hasLiked", hasLiked);
            model.addAttribute("likeCount", likeCount);
            model.addAttribute("images", images);
            model.addAttribute("profileImage", imageUrl);

        }catch (Exception e){
            Post post = postDao.getById(id);
            List<Comment> comments = commentDao.findByPostOrderByIdAsc(post);
            List<Image> images = post.getImages();
            int likeCount = likeDao.countByPost(post);

            model.addAttribute("post", post);
            model.addAttribute("comments", comments);
            model.addAttribute("hasLiked", false);
            model.addAttribute("likeCount", likeCount);
            model.addAttribute("images", images);
        }
        return "posts/show";
    }

    @PostMapping("/posts/{id}/comments")
    public String addComment(@PathVariable long id, @RequestParam String comment) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Post post = postDao.getById(id);

        Comment newComment = new Comment();
        newComment.setComment(comment);
        newComment.setUser(user);
        newComment.setPost(post);

        commentDao.save(newComment);

        return "redirect:/posts/" + id;
    }

    @PostMapping("/posts/{postId}/toggle-like")
    public ResponseEntity<Integer> toggleLike(@PathVariable long postId) {

        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Like existingLike = likeDao.findByUserAndPost(loggedInUser, postDao.findPostById(postId));

        if (existingLike != null) {
            likeDao.delete(existingLike);
        } else {
            Like newLike = new Like();
            newLike.setUser(loggedInUser);
            newLike.setPost(postDao.findPostById(postId));
            likeDao.save(newLike);
        }

        int updatedLikeCount = likeDao.countByPost(postDao.findPostById(postId));

        return ResponseEntity.ok(updatedLikeCount);
    }
    @PostMapping("/posts/search")
    public String searchByStory(@RequestParam(name = "query") String query, Model model) {
        List<Post> searchResultsTitle = postDao.findAllByTitleContainingIgnoreCaseOrStoryContainingIgnoreCase(query, query);
        List<Tag> searchResultsTags = tagDao.findAllByTagContainsIgnoreCase(query);

        model.addAttribute("searchResultsTitle", searchResultsTitle);
        model.addAttribute("searchResultsTags", searchResultsTags);
        model.addAttribute("query", query);

        return "posts/search";
    }

    @PostMapping("/posts/category/{id}")
    public String searchByCategory(@PathVariable(name = "id") long id, Model model) {
        List<Post> catPost = new ArrayList<>();

        List<Post> posts = postDao.findByArchiveFalse();
        for (Post post: posts){
            List<Category> cat = categoryRepository.findAllByPost(post);
            for (Category category: cat){
                if (category.getId() == id) {
                    catPost.add(post);
                }
            }
        }

        if (catPost != null){
            model.addAttribute("category", catPost);
            return "posts/search";
        }
        return "redirect:/posts";
    }

    @GetMapping("/posts/{id}/edit")
    public String postEdit(@PathVariable long id, Model model) {
        Post post = postDao.getById(id);
        model.addAttribute("post", post);
        model.addAttribute("images", post.getImages());

        List<Category> allCategories = categoryRepository.findAll();
        model.addAttribute("allCategories", allCategories);

        // get selected categories
        List<Long> selectedCategories = new ArrayList<>();
        for (Category category : post.getCategories()) {
            selectedCategories.add(category.getId());
        }
        model.addAttribute("selectedCategories", selectedCategories);

        // get tags
        String tags = "";
        for (Tag tag : post.getTags()) {
            if (!tags.isEmpty()) {
                tags += ", ";
            }
            tags += tag.getTag();
        }
        model.addAttribute("tagString", tags);

        return "posts/edit";
    }

    @PostMapping("/posts/{id}/edit")
    public String editPost(@PathVariable long id,
                           @ModelAttribute Post editedPost,
                           @RequestParam(name = "selectedCategories") List<Long> selectedCategories) {

        Post existingPost = postDao.getById(id);

        existingPost.setTitle(editedPost.getTitle());
        existingPost.setStory(editedPost.getStory());
        existingPost.setSolution(editedPost.getSolution());

        List<Category> categories = categoryRepository.findAllById(selectedCategories);
        existingPost.setCategories(categories);

        List<Tag> existingTags = existingPost.getTags();

        // Convert tags to lowercase and filter duplicates
        String[] rawTags = editedPost.getTagString().split(",\\s*");
        List<String> newTags = new ArrayList<>();
        for (String rawTag : rawTags) {
            String cleanedTag = rawTag.trim().toLowerCase();
            if (!newTags.contains(cleanedTag)) {
                newTags.add(cleanedTag);
            }
        }

        // Add new tags
        for (String newTag : newTags) {
            boolean tagExists = false;
            for (Tag existingTag : existingTags) {
                if (existingTag.getTag().equals(newTag)) {
                    tagExists = true;
                    break;
                }
            }
            if (!tagExists) {
                Tag tag = new Tag(newTag, existingPost);
                tagDao.save(tag);
            }
        }

        // Remove tags that are no longer associated
        List<Tag> tagsToRemove = new ArrayList<>();
        for (Tag existingTag : existingTags) {
            if (!newTags.contains(existingTag.getTag())) {
                tagsToRemove.add(existingTag);
            }
        }

        for (Tag tag : tagsToRemove) {
            existingTags.remove(tag);
            tagDao.delete(tag);
        }

        postDao.save(existingPost);

        return "redirect:/posts/" + id;
    }


    @GetMapping("/posts/create")
    public String postsCreate(Model model) {
        if (!model.containsAttribute("postForm")) {
            model.addAttribute("postForm", new Post());
        }

        List<Category> allCategories = categoryRepository.findAll();
        model.addAttribute("allCategories", allCategories);

        if (!model.containsAttribute("selectedCategoriesForm")) {
            model.addAttribute("selectedCategoriesForm", new ArrayList<Long>());
        }
        return "posts/create";
    }


    @PostMapping("/posts/create")
    public String createPost(@ModelAttribute Post post,
                             @RequestParam(name = "tagString") String tagString,
                             @RequestParam(name = "selectedCategories") List<Long> selectedCategories,
                             @RequestParam(name = "imageUrls", required = false) String imageUrls,
                             RedirectAttributes redirectAttributes) {

        if (imageUrls.equals("")) {
            redirectAttributes.addFlashAttribute("errorMessage", "You must upload an image to save the post.");
            redirectAttributes.addFlashAttribute("postForm", post);
            redirectAttributes.addFlashAttribute("selectedCategoriesForm", selectedCategories);
            return "redirect:/posts/create";
        }
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        post.setUser(user);

        post.setDate(new Date());

        List<Category> categories = categoryRepository.findAllById(selectedCategories);
        post.setCategories(categories);

        postDao.save(post);

        if (imageUrls != null && !imageUrls.isEmpty()) {
            String[] urls = imageUrls.split(",");
            for (String imageUrl : urls) {
                Image image = new Image();
                image.setImage(imageUrl);
                image.setPost(post);
                imageDao.save(image);
            }
        }

        long postId = postDao.getIdByTitle(post.getTitle()).getId();
        Post postInfo = postDao.getById(postId);

        String[] uniqueTags = Arrays.stream(tagString.toLowerCase().split(",\\s*"))
                .distinct()
                .toArray(String[]::new);

        for (String tag : uniqueTags) {
            Tag tagEntity = new Tag(tag, postInfo);
            tagDao.save(tagEntity);
        }

        System.out.println("postId = " + postId);
        emailService.prepareAndSend(post, user.getUsername()
                + " thanks for creating a post!", user.getUsername()
                + " thanks for creating the post: " + post.getTitle() + "You can view your post at https://upliftunity.com/posts/" + postId);
        return "redirect:/posts";
    }
}
