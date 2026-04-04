package com.library_api.controller;

import com.library_api.model.Borrow;
import com.library_api.model.User;
import com.library_api.repository.UserRepository;
import com.library_api.service.BorrowService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrows")
public class BorrowController {

    private final BorrowService borrowService;
    private final UserRepository userRepository;

    public BorrowController(BorrowService borrowService, UserRepository userRepository) {
        this.borrowService = borrowService;
        this.userRepository = userRepository;
    }
    //получить id пользователя из security

    private Long getCurrentUserId(Authentication authentication) {
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        return user.getId();
    }

    //взять книгу
    @PostMapping("/borrow/{bookId}")
    public ResponseEntity<?> borrowBook(Authentication authentication, @PathVariable Long bookId) {
        try {
            Long userId = getCurrentUserId(authentication);
            Borrow borrow = borrowService.borrowBook(userId, bookId);
            return ResponseEntity.ok(borrow);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //Вернуть книгу
    @PostMapping("/return/{bookId}")
    public ResponseEntity<?> returnBook(@PathVariable Long bookId) {
        try {
            Borrow borrow = borrowService.returnBook(bookId);
            return ResponseEntity.ok(borrow);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //мои активные книги

    @GetMapping("/my/active")
    public ResponseEntity<List<Borrow>> getMyActiveBorrows(Authentication authentication) {
        Long userId = getCurrentUserId(authentication);
        return ResponseEntity.ok(borrowService.getActiveBorrowsByUser(userId));
    }

    //моя история выдач

    @GetMapping("/my/history")
    public ResponseEntity<List<Borrow>> getMyHistory(Authentication authentication) {
        Long userId = getCurrentUserId(authentication);
        return ResponseEntity.ok(borrowService.getBorrowHistoryByUser(userId));
    }

    //есть ли просрочки

    @GetMapping("/my/has-overdue")
    public ResponseEntity<Boolean> hasOverdue(Authentication authentication) {
        Long userId = getCurrentUserId(authentication);
        return ResponseEntity.ok(borrowService.hasOverdueBooks(userId));
    }
}