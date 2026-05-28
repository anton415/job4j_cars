package ru.job4j.cars.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.model.User;
import ru.job4j.cars.repository.PostRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PostServiceTest {
    private PostRepository postRepository;

    private PostService postService;

    @BeforeEach
    void setUp() {
        postRepository = mock(PostRepository.class);
        postService = new PostService(postRepository);
    }

    @Test
    void whenAuthorChangesStatusThenRepositoryUpdatesPost() {
        var author = user(1);
        when(postRepository.findById(10)).thenReturn(Optional.of(post(author)));
        when(postRepository.updateSoldStatus(10, true)).thenReturn(true);

        var result = postService.changeSoldStatus(10, true, author);

        assertThat(result).isTrue();
        verify(postRepository).updateSoldStatus(10, true);
    }

    @Test
    void whenAnotherUserChangesStatusThenFalseAndPostNotUpdated() {
        when(postRepository.findById(10)).thenReturn(Optional.of(post(user(1))));

        var result = postService.changeSoldStatus(10, true, user(2));

        assertThat(result).isFalse();
        verify(postRepository, never()).updateSoldStatus(10, true);
    }

    @Test
    void whenAnonymousUserChangesStatusThenFalseAndPostNotLoaded() {
        var result = postService.changeSoldStatus(10, true, null);

        assertThat(result).isFalse();
        verify(postRepository, never()).findById(10);
        verify(postRepository, never()).updateSoldStatus(10, true);
    }

    @Test
    void whenPostNotFoundThenFalseAndStatusNotUpdated() {
        when(postRepository.findById(10)).thenReturn(Optional.empty());

        var result = postService.changeSoldStatus(10, true, user(1));

        assertThat(result).isFalse();
        verify(postRepository, never()).updateSoldStatus(10, true);
    }

    private Post post(User author) {
        var post = new Post();
        post.setUser(author);
        return post;
    }

    private User user(int id) {
        var user = new User();
        user.setId(id);
        user.setName("User " + id);
        user.setEmail("user" + id + "@example.com");
        user.setPassword("password");
        return user;
    }
}
