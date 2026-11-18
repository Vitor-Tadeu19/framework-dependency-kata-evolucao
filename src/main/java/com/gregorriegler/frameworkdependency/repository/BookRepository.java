package com.gregorriegler.frameworkdependency.repository;

import com.gregorriegler.frameworkdependency.model.Book;

import java.util.Collection;
import java.util.Optional;

public interface BookRepository {
    Collection<Book> findAll();

    Optional<Book> findById(String isbn);

    void save(Book book);

    void deleteAll();
}
