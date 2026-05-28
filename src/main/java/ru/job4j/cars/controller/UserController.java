package ru.job4j.cars.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.job4j.cars.model.User;
import ru.job4j.cars.service.UserService;

@Controller
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/register")
    public String registerPage(Model model) {
        model.addAttribute("newUser", new User());
        return "users/register";
    }

    @PostMapping("/users/register")
    public String register(@ModelAttribute("newUser") User user, Model model, HttpSession session) {
        var createdUser = userService.register(user);
        if (createdUser.isEmpty()) {
            model.addAttribute("message", "Пользователь с такой почтой уже существует");
            return "users/register";
        }
        session.setAttribute("user", createdUser.get());
        return "redirect:/";
    }

    @GetMapping("/users/login")
    public String loginPage() {
        return "users/login";
    }

    @PostMapping("/users/login")
    public String login(@RequestParam String email, @RequestParam String password,
                        Model model, HttpSession session) {
        var user = userService.login(email, password);
        if (user.isEmpty()) {
            model.addAttribute("message", "Неверная почта или пароль");
            return "users/login";
        }
        session.setAttribute("user", user.get());
        return "redirect:/";
    }

    @GetMapping("/users/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
