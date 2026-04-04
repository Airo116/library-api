package com.library_api.controller;

import com.library_api.dto.BookCreateDTO;
import com.library_api.dto.BookResponseDTO;
import com.library_api.service.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/books")
public class BookViewController {

    private final BookService bookService;

    public BookViewController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public String listBooks(Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        return "book-list";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        BookResponseDTO book = bookService.getBookById(id);
        model.addAttribute("book", book);
        return "book-edit";
    }

    @PostMapping("/edit/{id}")
    public String updateBook(@PathVariable Long id, @ModelAttribute BookCreateDTO dto) {
        bookService.updateBook(id, dto);
        return "redirect:/books";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("book", new BookCreateDTO());
        return "book-create";
    }

    @PostMapping("/create")
    public String createBook(@ModelAttribute BookCreateDTO dto) {
        bookService.createBook(dto);
        return "redirect:/books";
    }

    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return "redirect:/books";
    }
}