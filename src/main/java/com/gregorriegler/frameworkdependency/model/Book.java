package com.gregorriegler.frameworkdependency.model;

import java.util.ArrayList;
import java.util.List;

public class Book {
    public String isbn;
    public String title;
    public String author;
    public List<Rating> ratings = new ArrayList<>();

    public Book() {
    }

    public Book(String isbn, String title, String author) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
    }
}
