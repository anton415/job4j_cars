package ru.job4j.cars.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.job4j.cars.service.PostService;

@Controller
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/posts";
    }

    @GetMapping("/posts")
    public String list(Model model) {
        model.addAttribute("posts", postService.findAll());
        return "posts/list";
    }

    @GetMapping("/posts/{postId}")
    public String detail(@PathVariable int postId, Model model) {
        var post = postService.findById(postId);
        if (post.isEmpty()) {
            return "redirect:/posts";
        }
        model.addAttribute("post", post.get());
        return "posts/detail";
    }
}
