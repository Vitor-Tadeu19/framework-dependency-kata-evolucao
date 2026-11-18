package com.gregorriegler.frameworkdependency.services;

import com.gregorriegler.frameworkdependency.model.AuthorizationException;
import com.gregorriegler.frameworkdependency.model.Rating;
import com.gregorriegler.frameworkdependency.model.RatingStars;
import com.gregorriegler.frameworkdependency.model.User;
import com.gregorriegler.frameworkdependency.repository.BookRepository;
import com.gregorriegler.frameworkdependency.security.SecurityService;
import org.springframework.transaction.annotation.Transactional;

public class RatingService {

    private final SecurityService securityService;
    private final BookRepository repository;

    public RatingService(SecurityService securityService, BookRepository repository) {
        this.securityService = securityService;
        this.repository = repository;
    }

    @Transactional
    public void rateBook(User user, String isbn, RatingStars stars, String comment) {
        if (!securityService.canRateBook(user)) {
            throw new AuthorizationException("access denied!");
        }
        repository.findById(isbn).ifPresent(book -> {
            Rating rating = new Rating();
            rating.user = user.name();
            rating.stars = stars;
            rating.comment = comment;
            book.ratings.add(rating);
        });
    }
}
