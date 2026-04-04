package com.library_api.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class BookCreateDTO {

    @NotBlank(message = "Название книги обязательно")
    @Size(min = 1, max = 200, message = "Название должно быть от 1 до 200 символов")
    private String title;

    @NotBlank(message = "Автор обязателен")
    @Size(min = 2, max = 100, message = "Имя автора должно быть от 2 до 100 символов")
    private String author;

    @Min(value = 1000, message = "Год издания должен быть не ранее 1000")
    @Max(value = 2025, message = "Год издания не может быть в будущем")
    private Integer publicationYear;

    private String genre;
    private String language;

    @Min(value = 1, message = "Количество страниц должно быть больше 0")
    @Max(value = 10000, message = "Количество страниц не может превышать 10000")
    private Integer pageCount;

    private Boolean read = false;

    // Геттеры и сеттеры
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public Integer getPublicationYear() { return publicationYear; }
    public void setPublicationYear(Integer publicationYear) { this.publicationYear = publicationYear; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public Integer getPageCount() { return pageCount; }
    public void setPageCount(Integer pageCount) { this.pageCount = pageCount; }

    public Boolean getRead() { return read; }
    public void setRead(Boolean read) { this.read = read; }
}