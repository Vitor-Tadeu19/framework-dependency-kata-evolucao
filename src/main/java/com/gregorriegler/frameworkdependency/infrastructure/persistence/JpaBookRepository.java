package com.gregorriegler.frameworkdependency.infrastructure.persistence;

import com.gregorriegler.frameworkdependency.model.Book;
import com.gregorriegler.frameworkdependency.model.Rating;
import com.gregorriegler.frameworkdependency.repository.BookRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class JpaBookRepository implements BookRepository {
    private final SpringDataBookRepository springRepository;

    public JpaBookRepository(SpringDataBookRepository springRepository) {
        this.springRepository = springRepository;
    }

    @Override
    public Collection<Book> findAll() {
        return springRepository.findAll().stream()
            .map(this::toBook)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<Book> findById(String isbn) {
        return springRepository.findById(isbn).map(this::toBook);
    }

    @Override
    public void save(Book book) {
        BookEntity entity = springRepository.findById(book.isbn).orElseGet(() -> new BookEntity(book.isbn, book.title, book.author));
        entity.title = book.title;
        entity.author = book.author;

        entity.ratings.clear();
        for (Rating rating : book.ratings) {
            RatingEntity ratingEntity = new RatingEntity();
            ratingEntity.id = rating.id;
            ratingEntity.user = rating.user;
            ratingEntity.stars = rating.stars;
            ratingEntity.comment = rating.comment;
            ratingEntity.book = entity;
            entity.ratings.add(ratingEntity);
        }

        springRepository.save(entity);
    }

    @Override
    public void deleteAll() {
        springRepository.deleteAll();
    }

    private BookEntity toEntity(Book book) {
        BookEntity entity = new BookEntity(book.isbn, book.title, book.author);
        for (Rating rating : book.ratings) {
            RatingEntity ratingEntity = new RatingEntity();
            ratingEntity.user = rating.user;
            ratingEntity.stars = rating.stars;
            ratingEntity.comment = rating.comment;
            ratingEntity.book = entity;
            entity.ratings.add(ratingEntity);
        }
        return entity;
    }

    private Book toBook(BookEntity entity) {
        Book book = new Book(entity.isbn, entity.title, entity.author);
        for (RatingEntity ratingEntity : entity.ratings) {
            Rating rating = new Rating();
            rating.id = ratingEntity.id;
            rating.user = ratingEntity.user;
            rating.stars = ratingEntity.stars;
            rating.comment = ratingEntity.comment;
            book.ratings.add(rating);
        }
        return book;
    }
}
