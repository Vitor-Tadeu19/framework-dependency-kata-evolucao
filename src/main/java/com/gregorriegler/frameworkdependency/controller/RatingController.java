package com.gregorriegler.frameworkdependency.controller;

import com.gregorriegler.frameworkdependency.dtos.request.RatingRequest;
import com.gregorriegler.frameworkdependency.model.User;
import com.gregorriegler.frameworkdependency.services.RatingService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class RatingController {

    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @PostMapping("/books/{isbn}/ratings")
    @ResponseStatus(HttpStatus.CREATED)
    public void rateBook(@PathVariable String isbn, Authentication authentication, @RequestBody RatingRequest request) {
        ratingService.rateBook(toUser(authentication), isbn, request.stars, request.comment);
    }

    private User toUser(Authentication authentication) {
        Set<String> roles = authentication.getAuthorities().stream()
            .map(authority -> authority.getAuthority())
            .collect(Collectors.toSet());
        return new User(authentication.getName(), roles);
    }
}
