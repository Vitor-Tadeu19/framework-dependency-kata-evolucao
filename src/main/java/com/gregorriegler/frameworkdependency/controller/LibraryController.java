package com.gregorriegler.frameworkdependency.controller;

import com.gregorriegler.frameworkdependency.dtos.request.CreateBookRequest;
import com.gregorriegler.frameworkdependency.model.Book;
import com.gregorriegler.frameworkdependency.services.LibraryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
public class LibraryController {

    private final LibraryService libraryService;

    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @GetMapping("/books")
    public ResponseEntity<Collection<Book>> get() {
        return ResponseEntity.ok(libraryService.getAllBooks());
    }

    @PutMapping("/books/{isbn}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void save(@PathVariable String isbn, @RequestBody CreateBookRequest request) {
        request.isbn = isbn;
        libraryService.createBook(new Book(isbn, request.title, request.author));
    }
}
