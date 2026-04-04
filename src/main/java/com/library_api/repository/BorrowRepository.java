package com.library_api.repository;

import com.library_api.model.Borrow;
import com.library_api.model.User;
import com.library_api.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface BorrowRepository extends JpaRepository<Borrow, Long> {

    // Найти все активные выдачи
    List<Borrow> findByReturnDateIsNull();

    // Найти активные выдачи пользователя
    List<Borrow> findByUserAndReturnDateIsNull(User user);

    // Найти активную выдачу конкретной книги
    Optional<Borrow> findByBookAndReturnDateIsNull(Book book);

    // История выдач книги
    List<Borrow> findByBookOrderByBorrowDateDesc(Book book);

    // История выдачи пользователю
    List<Borrow> findByUserOrderByBorrowDateDesc(User user);
}