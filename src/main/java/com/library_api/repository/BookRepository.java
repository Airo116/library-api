package com.library_api.repository;

import com.library_api.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


@Repository
public interface BookRepository extends JpaRepository<Book, Long> {


    @Query("SELECT b FROM Book b WHERE " +
    "(:author IS NULL OR LOWER(b.author) LIKE LOWER(CONCAT('%', :author, '%'))) AND " +
    "(:title IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%')))")
    List<Book> searchBooks(@Param("author") String author, @Param("title") String title);

    List<Book> findByAuthorContainingIgnoreCase(String author);
    List<Book> findByTitleContainingIgnoreCase(String title);

}

