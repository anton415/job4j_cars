package ru.job4j.cars.service;

import org.springframework.stereotype.Service;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.model.User;
import ru.job4j.cars.repository.PostRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public Optional<Post> findById(int postId) {
        return postRepository.findById(postId);
    }

    public Post create(Post post, User currentUser) {
        requireCurrentUser(currentUser);
        post.setUser(currentUser);
        return postRepository.create(post);
    }

    public boolean changeSoldStatus(int postId, boolean sold, User currentUser) {
        if (currentUser == null || currentUser.getId() == null) {
            return false;
        }
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isEmpty()) {
            return false;
        }
        User author = postOptional.get().getUser();
        if (author == null || !currentUser.getId().equals(author.getId())) {
            return false;
        }
        return postRepository.updateSoldStatus(postId, sold);
    }

    private void requireCurrentUser(User currentUser) {
        if (currentUser == null || currentUser.getId() == null) {
            throw new IllegalArgumentException("Current user is required");
        }
    }
}
