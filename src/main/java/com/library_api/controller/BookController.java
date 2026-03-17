package com.library_api.controller;

import com.library_api.model.Book;
import com.library_api.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired // найти и вставить зависимость
    private BookRepository bookRepository;

    @GetMapping //отвечает на гет запросы по адресу
    public List<Book> getAllBooks() {
        return bookRepository.findAll(); //входит в БД и возмет все что есть
    }

    @GetMapping("/{id}") //вернет одну книгу по id
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Book book = bookRepository.findById(id).orElse(null);
        if (book == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(book);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book bookDetails) {
        return bookRepository.findById(id)
                .map(book -> {

                    book.setTitle((bookDetails.getTitle()));
                    book.setAuthor(bookDetails.getAuthor());
                    book.setPublicationYear(bookDetails.getpublicationYear());
                    book.setGenre(bookDetails.getGenre());
                    book.setPageCount(bookDetails.getPageCount());
                    Book updateBook = bookRepository.save(book);
                    return ResponseEntity.ok(updateBook);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Book createBook(@RequestBody Book book) {
        return bookRepository.save(book);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/search")
    public List<Book> searchBooks(
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String title) {

        if (author != null && !author.isEmpty()) {
            return bookRepository.findByAuthorContainingIgnoreCase(author);
        } else if (title != null && !title.isEmpty()) {
            return bookRepository.findByTitleContainingIgnoreCase(title);
        } else {
            return bookRepository.findAll();
        }
    }
}