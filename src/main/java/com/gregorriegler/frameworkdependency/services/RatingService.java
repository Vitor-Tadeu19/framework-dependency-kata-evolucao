package com.gregorriegler.frameworkdependency.services;

import com.gregorriegler.frameworkdependency.dtos.request.RatingRequest;
import com.gregorriegler.frameworkdependency.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class RatingService {

    private final LibraryService libraryService;

    public RatingService(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @PostMapping("/books/{isbn}/ratings")
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public void rate(@PathVariable String isbn, Authentication authentication, @RequestBody RatingRequest request) {
        Set<String> roles = authentication.getAuthorities().stream()
            .map(authority -> authority.getAuthority())
            .collect(Collectors.toSet());
        libraryService.rateBook(new User(authentication.getName(), roles), isbn, request.stars, request.comment);
    }
}
