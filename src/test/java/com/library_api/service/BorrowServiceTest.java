package com.library_api.service;

import com.library_api.model.Book;
import com.library_api.model.Borrow;
import com.library_api.model.User;
import com.library_api.repository.BookRepository;
import com.library_api.repository.BorrowRepository;
import com.library_api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BorrowServiceTest {

    @Mock
    private BorrowRepository borrowRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BorrowService borrowService;

    private User testUser;
    private Book testBook;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");

        testBook = new Book();
        testBook.setId(1L);
        testBook.setTitle("Test Book");
    }

    @Test
    void borrowBook_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(borrowRepository.findByUserAndReturnDateIsNull(testUser)).thenReturn(new ArrayList<>());
        when(borrowRepository.findByBookAndReturnDateIsNull(testBook)).thenReturn(Optional.empty());
        when(borrowRepository.save(any(Borrow.class))).thenReturn(new Borrow());

        Borrow result = borrowService.borrowBook(1L, 1L);

        assertNotNull(result);
        verify(borrowRepository, times(1)).save(any(Borrow.class));
    }

    @Test
    void borrowBook_UserNotFound_ThrowsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> borrowService.borrowBook(1L, 1L));
        verify(borrowRepository, never()).save(any(Borrow.class));
    }

    @Test
    void borrowBook_BookNotFound_ThrowsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> borrowService.borrowBook(1L, 1L));
    }

    @Test
    void borrowBook_MaxBooksLimit_ThrowsException() {
        List<Borrow> activeBorrows = new ArrayList<>();
        activeBorrows.add(new Borrow());
        activeBorrows.add(new Borrow());
        activeBorrows.add(new Borrow());

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(borrowRepository.findByUserAndReturnDateIsNull(testUser)).thenReturn(activeBorrows);

        assertThrows(RuntimeException.class, () -> borrowService.borrowBook(1L, 1L));
    }

    @Test
    void borrowBook_BookAlreadyBorrowed_ThrowsException() {
        Borrow existingBorrow = new Borrow();
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(borrowRepository.findByUserAndReturnDateIsNull(testUser)).thenReturn(new ArrayList<>());
        when(borrowRepository.findByBookAndReturnDateIsNull(testBook)).thenReturn(Optional.of(existingBorrow));

        assertThrows(RuntimeException.class, () -> borrowService.borrowBook(1L, 1L));
    }

    @Test
    void returnBook_Success() {
        Borrow activeBorrow = new Borrow();
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(borrowRepository.findByBookAndReturnDateIsNull(testBook)).thenReturn(Optional.of(activeBorrow));
        when(borrowRepository.save(any(Borrow.class))).thenReturn(activeBorrow);

        Borrow result = borrowService.returnBook(1L);

        assertNotNull(result);
        verify(borrowRepository, times(1)).save(activeBorrow);
    }

    @Test
    void returnBook_BookNotFound_ThrowsException() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> borrowService.returnBook(1L));
    }

    @Test
    void returnBook_BookNotBorrowed_ThrowsException() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(borrowRepository.findByBookAndReturnDateIsNull(testBook)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> borrowService.returnBook(1L));
    }
}