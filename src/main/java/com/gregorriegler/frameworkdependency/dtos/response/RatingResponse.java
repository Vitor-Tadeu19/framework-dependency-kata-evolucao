package com.gregorriegler.frameworkdependency.dtos.response;

import com.gregorriegler.frameworkdependency.model.enums.RatingStars;

public record RatingResponse(Long id, String user, RatingStars stars, String comment) {
}
