package com.gregorriegler.frameworkdependency.infrastructure.persistence;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "book")
public class BookEntity {
    @Id
    public String isbn;
    public String title;
    public String author;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "book_ratings",
        joinColumns = @JoinColumn(name = "book_isbn"),
        inverseJoinColumns = @JoinColumn(name = "ratings_id", unique = true))
    public Set<RatingEntity> ratings = new LinkedHashSet<>();

    public BookEntity() {
    }

    public BookEntity(String isbn, String title, String author) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
    }
}
