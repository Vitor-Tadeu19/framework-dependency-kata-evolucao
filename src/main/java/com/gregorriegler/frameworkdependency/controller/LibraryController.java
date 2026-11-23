package com.gregorriegler.frameworkdependency.controller;

import com.gregorriegler.frameworkdependency.dtos.request.CreateBookRequest;
import com.gregorriegler.frameworkdependency.dtos.response.BookResponse;
import com.gregorriegler.frameworkdependency.dtos.response.RatingResponse;
import com.gregorriegler.frameworkdependency.model.Book;
import com.gregorriegler.frameworkdependency.model.User;
import com.gregorriegler.frameworkdependency.services.LibraryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class LibraryController {

    private final LibraryService libraryService;

    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @GetMapping("/books")
    public ResponseEntity<List<BookResponse>> get(Authentication authentication) {
        return ResponseEntity.ok(libraryService.getAllBooks(toUser(authentication)).stream()
            .map(this::toBookResponse)
            .toList());
    }

    @PutMapping("/books/{isbn}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void save(Authentication authentication, @PathVariable String isbn, @RequestBody CreateBookRequest request) {
        request.isbn = isbn;
        libraryService.createBook(toUser(authentication), new Book(isbn, request.title, request.author));
    }

    private BookResponse toBookResponse(Book book) {
        List<RatingResponse> ratings = book.ratings.stream()
            .map(rating -> new RatingResponse(rating.id, rating.user, rating.stars, rating.comment))
            .toList();
        return new BookResponse(book.isbn, book.title, book.author, ratings);
    }

    private User toUser(Authentication authentication) {
        Set<String> roles = authentication.getAuthorities().stream()
            .map(authority -> authority.getAuthority())
            .collect(Collectors.toSet());
        return new User(authentication.getName(), roles);
    }
}
