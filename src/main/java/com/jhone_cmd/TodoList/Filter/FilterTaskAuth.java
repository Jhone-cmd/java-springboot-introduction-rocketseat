package com.jhone_cmd.TodoList.Filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.jhone_cmd.TodoList.users.IUserRepository;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

    @Autowired
    private IUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        var servletPath = request.getServletPath();
        if (!servletPath.startsWith("/tasks")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        String authEncoded = authHeader.substring("Basic".length()).trim();

        byte[] authDecoded = java.util.Base64.getDecoder().decode(authEncoded);

        var authString = new String(authDecoded);
        String[] credentials = authString.split(":");

        String email = credentials[0];
        String password = credentials[1];

        var user = this.userRepository.findByEmail(email);

        if (user == null) {
            response.sendError(401, "Credentials Invalid");
            return;
        } else {
            BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
            if (result.verified) {
                request.setAttribute("idUser", user.getId());
                filterChain.doFilter(request, response);

            } else {
                response.sendError(401, "Credentials Invalid");
                return;
            }
        }
    }
}