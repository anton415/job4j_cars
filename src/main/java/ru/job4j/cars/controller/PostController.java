package ru.job4j.cars.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.model.User;
import ru.job4j.cars.service.CarService;
import ru.job4j.cars.service.PostService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
public class PostController {
    private static final String UPLOAD_DIR = "uploads";

    private final PostService postService;

    private final CarService carService;

    public PostController(PostService postService, CarService carService) {
        this.postService = postService;
        this.carService = carService;
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

    @GetMapping("/posts/create")
    public String createPage(Model model, HttpSession session) {
        if (currentUser(session) == null) {
            return "redirect:/users/login";
        }
        addPostForm(model, new Post());
        return "posts/create";
    }

    @PostMapping("/posts/create")
    public String create(@ModelAttribute("post") Post post,
                         BindingResult bindingResult,
                         @RequestParam(name = "photo", required = false) MultipartFile photo,
                         Model model,
                         HttpSession session) {
        User user = currentUser(session);
        if (user == null) {
            return "redirect:/users/login";
        }
        preparePost(post);
        String error = validatePost(post, bindingResult);
        if (error != null) {
            addPostForm(model, post);
            model.addAttribute("message", error);
            return "posts/create";
        }
        try {
            post.setPhotoPath(savePhoto(photo));
        } catch (IOException e) {
            addPostForm(model, post);
            model.addAttribute("message", "Не удалось сохранить фото");
            return "posts/create";
        }
        Car car = carService.create(post.getCar());
        post.setCar(car);
        Post createdPost = postService.create(post, user);
        return "redirect:/posts/" + createdPost.getId();
    }

    @PostMapping("/posts/{postId}/status")
    public String changeStatus(@PathVariable int postId,
                               @RequestParam boolean sold,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        User user = currentUser(session);
        if (user == null) {
            return "redirect:/users/login";
        }
        if (!postService.changeSoldStatus(postId, sold, user)) {
            redirectAttributes.addFlashAttribute("message", "Статус может менять только автор объявления");
        }
        return "redirect:/posts/" + postId;
    }

    private void addPostForm(Model model, Post post) {
        preparePost(post);
        model.addAttribute("post", post);
    }

    private void preparePost(Post post) {
        if (post.getCar() == null) {
            post.setCar(new Car());
        }
        post.setSold(false);
        post.setTitle(trim(post.getTitle()));
        post.setDescription(trim(post.getDescription()));
        post.setPhotoPath(null);
        Car car = post.getCar();
        car.setBrand(trim(car.getBrand()));
        car.setModel(trim(car.getModel()));
        car.setBodyType(trim(car.getBodyType()));
        car.setEngineType(trim(car.getEngineType()));
        car.setTransmission(trim(car.getTransmission()));
    }

    private String validatePost(Post post, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "Проверьте корректность числовых полей";
        }
        if (isBlank(post.getTitle())) {
            return "Укажите название объявления";
        }
        if (post.getPrice() == null || post.getPrice().signum() <= 0) {
            return "Укажите цену больше нуля";
        }
        Car car = post.getCar();
        if (isBlank(car.getBrand())) {
            return "Укажите марку автомобиля";
        }
        if (isBlank(car.getModel())) {
            return "Укажите модель автомобиля";
        }
        if (car.getYear() <= 0) {
            return "Укажите год выпуска";
        }
        if (isBlank(car.getBodyType())) {
            return "Укажите тип кузова";
        }
        if (isBlank(car.getEngineType())) {
            return "Укажите тип двигателя";
        }
        if (isBlank(car.getTransmission())) {
            return "Укажите коробку передач";
        }
        if (car.getMileage() < 0) {
            return "Пробег не может быть отрицательным";
        }
        return null;
    }

    private User currentUser(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getId() == null) {
            return null;
        }
        return user;
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private String trim(String value) {
        return value == null ? null : value.trim();
    }

    private String savePhoto(MultipartFile photo) throws IOException {
        if (photo == null || photo.isEmpty()) {
            return null;
        }
        Path uploadPath = Paths.get(UPLOAD_DIR).toAbsolutePath().normalize();
        Files.createDirectories(uploadPath);
        String fileName = UUID.randomUUID() + extension(photo.getOriginalFilename());
        photo.transferTo(uploadPath.resolve(fileName));
        return "/" + UPLOAD_DIR + "/" + fileName;
    }

    private String extension(String fileName) {
        if (fileName == null || fileName.isBlank()) {
            return "";
        }
        String normalizedName = fileName.replace('\\', '/');
        String cleanName = normalizedName.substring(normalizedName.lastIndexOf('/') + 1);
        int index = cleanName.lastIndexOf('.');
        return index == -1 ? "" : cleanName.substring(index);
    }
}
