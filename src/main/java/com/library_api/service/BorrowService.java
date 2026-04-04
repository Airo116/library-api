package com.library_api.service;

import com.library_api.model.Book;
import com.library_api.model.Borrow;
import com.library_api.model.User;
import com.library_api.repository.BookRepository;
import com.library_api.repository.BorrowRepository;
import com.library_api.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class BorrowService {

    private final BorrowRepository borrowRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    private static final int MAX_BOOKS_PER_USER = 3;
    private static final int BORROW_DAYS = 14;

    public BorrowService(BorrowRepository borrowRepository,
                         UserRepository userRepository,
                         BookRepository bookRepository) {
        this.borrowRepository = borrowRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    public Borrow borrowBook(Long userId, Long bookId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Книга не найдена"));

        long activeBorrows = borrowRepository.findByUserAndReturnDateIsNull(user).size();
        if (activeBorrows >= MAX_BOOKS_PER_USER) {
            throw new RuntimeException("Вы уже взяли максимум книг (" + MAX_BOOKS_PER_USER + ")");
        }

        if (borrowRepository.findByBookAndReturnDateIsNull(book).isPresent()) {
            throw new RuntimeException("Книга уже выдана");
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime dueDate = now.plusDays(BORROW_DAYS);

        Borrow borrow = new Borrow(user, book, now, dueDate);
        return borrowRepository.save(borrow);
    }

    public Borrow returnBook(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Книга не найдена"));

        Borrow activeBorrow = borrowRepository.findByBookAndReturnDateIsNull(book)
                .orElseThrow(() -> new RuntimeException("Книга не находится в выдаче"));

        activeBorrow.setReturnDate(LocalDateTime.now());
        return borrowRepository.save(activeBorrow);
    }

    public List<Borrow> getActiveBorrowsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        return borrowRepository.findByUserAndReturnDateIsNull(user);
    }

    public List<Borrow> getBorrowHistoryByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        return borrowRepository.findByUserOrderByBorrowDateDesc(user);
    }

    public boolean hasOverdueBooks(Long userId) {
        List<Borrow> activeBorrows = getActiveBorrowsByUser(userId);
        return activeBorrows.stream().anyMatch(Borrow::isOverdue);
    }
}