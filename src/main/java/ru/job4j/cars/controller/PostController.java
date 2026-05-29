package ru.job4j.cars.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.job4j.cars.dto.PostDto;
import ru.job4j.cars.model.BodyType;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.model.CarBrand;
import ru.job4j.cars.model.CarModel;
import ru.job4j.cars.model.EngineType;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.model.Transmission;
import ru.job4j.cars.model.User;
import ru.job4j.cars.service.CarService;
import ru.job4j.cars.service.PostService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Year;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@Controller
public class PostController {
    private static final String UPLOAD_DIR = "uploads";
    private static final int MIN_PRODUCTION_YEAR = 1970;

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
    public String list(@RequestParam(name = "bodyType", required = false) String bodyType,
                       @RequestParam(name = "createdDate", required = false)
                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate createdDate,
                       @RequestParam(name = "withPhoto", defaultValue = "false") boolean withPhoto,
                       Model model) {
        bodyType = trim(bodyType);
        model.addAttribute("posts", postService.findAll(bodyType, createdDate, withPhoto));
        model.addAttribute("bodyTypes", BodyType.values());
        model.addAttribute("selectedBodyType", bodyType);
        model.addAttribute("selectedCreatedDate", createdDate);
        model.addAttribute("withPhoto", withPhoto);
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
    public String createPage(Model model) {
        addPostForm(model, new PostDto());
        return "posts/create";
    }

    @PostMapping("/posts/create")
    public String create(@ModelAttribute("post") PostDto postDto,
                         @RequestParam(name = "photo", required = false) MultipartFile photo,
                         Model model,
                         HttpSession session) {
        User user = currentUser(session);
        if (user == null) {
            return "redirect:/users/login";
        }
        preparePostDto(postDto);
        String photoPath;
        try {
            photoPath = savePhoto(photo);
        } catch (IOException e) {
            addPostForm(model, postDto);
            model.addAttribute("message", "Не удалось сохранить фото");
            return "posts/create";
        }
        Car car = carService.create(toCar(postDto));
        Post post = toPost(postDto);
        post.setCar(car);
        post.setPhotoPath(photoPath);
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

    private void addPostForm(Model model, PostDto postDto) {
        preparePostDto(postDto);
        model.addAttribute("post", postDto);
        model.addAttribute("carBrands", CarBrand.values());
        model.addAttribute("carModels", CarModel.values());
        model.addAttribute("productionYears", productionYears());
        model.addAttribute("bodyTypes", BodyType.values());
        model.addAttribute("engineTypes", EngineType.values());
        model.addAttribute("transmissions", Transmission.values());
    }

    private void preparePostDto(PostDto postDto) {
        postDto.setTitle(trim(postDto.getTitle()));
        postDto.setDescription(trim(postDto.getDescription()));
        postDto.setBrand(trim(postDto.getBrand()));
        postDto.setModel(trim(postDto.getModel()));
        postDto.setBodyType(trim(postDto.getBodyType()));
        postDto.setEngineType(trim(postDto.getEngineType()));
        postDto.setTransmission(trim(postDto.getTransmission()));
    }

    private Car toCar(PostDto postDto) {
        var car = new Car();
        car.setBrand(postDto.getBrand());
        car.setModel(postDto.getModel());
        car.setYear(postDto.getYear());
        car.setBodyType(postDto.getBodyType());
        car.setEngineType(postDto.getEngineType());
        car.setTransmission(postDto.getTransmission());
        car.setMileage(postDto.getMileage());
        return car;
    }

    private Post toPost(PostDto postDto) {
        var post = new Post();
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setPrice(postDto.getPrice());
        post.setSold(false);
        return post;
    }

    private User currentUser(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getId() == null) {
            return null;
        }
        return user;
    }

    private String trim(String value) {
        return value == null ? null : value.trim();
    }

    private List<Integer> productionYears() {
        int currentYear = Year.now().getValue();
        return IntStream.iterate(currentYear, year -> year >= MIN_PRODUCTION_YEAR, year -> year - 1)
                .boxed()
                .toList();
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
