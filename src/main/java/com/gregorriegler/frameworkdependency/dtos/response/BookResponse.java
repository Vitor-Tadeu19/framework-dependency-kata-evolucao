package com.gregorriegler.frameworkdependency.dtos.response;

import java.util.List;

public record BookResponse(String isbn, String title, String author, List<RatingResponse> ratings) {
}
