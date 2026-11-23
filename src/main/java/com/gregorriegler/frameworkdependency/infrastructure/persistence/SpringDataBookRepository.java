package com.gregorriegler.frameworkdependency.infrastructure.persistence;

import com.gregorriegler.frameworkdependency.repository.BookReportEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SpringDataBookRepository extends JpaRepository<BookEntity, String> {
    @Query("""
        select new com.gregorriegler.frameworkdependency.repository.BookReportEntry(book.isbn, book.title)
        from BookEntity book
        """)
    List<BookReportEntry> findReportEntries();
}
