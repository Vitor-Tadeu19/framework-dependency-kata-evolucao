package com.gregorriegler.frameworkdependency.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataBookRepository extends JpaRepository<BookEntity, String> {
}
