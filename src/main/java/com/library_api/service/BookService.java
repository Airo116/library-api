package com.library_api.service;

import com.library_api.dto.BookCreateDTO;
import com.library_api.dto.BookResponseDTO;
import com.library_api.exception.ResourceNotFoundException;
import com.library_api.mapper.BookMapper;
import com.library_api.model.Book;
import com.library_api.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public BookService(BookRepository bookRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    public List<BookResponseDTO> getAllBooks() {
        return bookMapper.toResponseDTOList(bookRepository.findAll());
    }

    public BookResponseDTO getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Книга не найдена с id: " + id));
        return bookMapper.toResponseDTO(book);
    }

    public BookResponseDTO createBook(BookCreateDTO dto) {
        Book book = bookMapper.toEntity(dto);
        Book savedBook = bookRepository.save(book);
        return bookMapper.toResponseDTO(savedBook);
    }

    public BookResponseDTO updateBook(Long id, BookCreateDTO dto) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Книга не найдена с id: " + id));

        bookMapper.updateEntity(existingBook, dto);
        Book updatedBook = bookRepository.save(existingBook);
        return bookMapper.toResponseDTO(updatedBook);
    }

    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Книга не найдена с id: " + id);
        }
        bookRepository.deleteById(id);
    }

    public List<BookResponseDTO> searchBooks(String author, String title) {
        List<Book> books;
        if (author != null && !author.isEmpty()) {
            books = bookRepository.findByAuthorContainingIgnoreCase(author);
        } else if (title != null && !title.isEmpty()) {
            books = bookRepository.findByTitleContainingIgnoreCase(title);
        } else {
            books = bookRepository.findAll();
        }
        return bookMapper.toResponseDTOList(books);
    }
}