package com.library_api.mapper;

import com.library_api.dto.BookCreateDTO;
import com.library_api.dto.BookResponseDTO;
import com.library_api.model.Book;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookMapper {

    public BookResponseDTO toResponseDTO(Book book) {
        if (book == null) {
            return null;
        }
        return new BookResponseDTO(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getPublicationYear(),
                book.getGenre(),
                book.getLanguage(),
                book.getPageCount(),
                book.getAddedDate(),
                book.getRead()
        );
    }

    public List<BookResponseDTO> toResponseDTOList(List<Book> books) {
        return books.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public Book toEntity(BookCreateDTO dto) {
        if (dto == null) {
            return null;
        }
        Book book = new Book();
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setPublicationYear(dto.getPublicationYear());
        book.setGenre(dto.getGenre());
        book.setLanguage(dto.getLanguage());
        book.setPageCount(dto.getPageCount());
        book.setRead(dto.getRead() != null ? dto.getRead() : false);
        book.setAddedDate(LocalDate.now());
        return book;
    }

    public void updateEntity(Book existingBook, BookCreateDTO dto) {
        if (dto.getTitle() != null) {
            existingBook.setTitle(dto.getTitle());
        }
        if (dto.getAuthor() != null) {
            existingBook.setAuthor(dto.getAuthor());
        }
        if (dto.getPublicationYear() != null) {
            existingBook.setPublicationYear(dto.getPublicationYear());
        }
        if (dto.getGenre() != null) {
            existingBook.setGenre(dto.getGenre());
        }
        if (dto.getLanguage() != null) {
            existingBook.setLanguage(dto.getLanguage());
        }
        if (dto.getPageCount() != null) {
            existingBook.setPageCount(dto.getPageCount());
        }
        if (dto.getRead() != null) {
            existingBook.setRead(dto.getRead());
        }
    }
}