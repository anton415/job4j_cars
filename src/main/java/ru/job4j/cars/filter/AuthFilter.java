package ru.job4j.cars.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import ru.job4j.cars.model.User;

import java.io.IOException;

@Component
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        if (isCreatePostPage(httpRequest) && currentUser(httpRequest) == null) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/users/login");
            return;
        }
        chain.doFilter(request, response);
    }

    private boolean isCreatePostPage(HttpServletRequest request) {
        return "/posts/create".equals(request.getServletPath());
    }

    private User currentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        User user = (User) session.getAttribute("user");
        return user == null || user.getId() == null ? null : user;
    }
}
