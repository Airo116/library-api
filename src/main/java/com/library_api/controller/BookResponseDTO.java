package com.library_api.dto;

import java.time.LocalDate;

public class BookResponseDTO {
    private Long id;
    private String title;
    private String author;
    private Integer publicationYear;
    private String genre;
    private String language;
    private Integer pageCount;
    private LocalDate addedDate;
    private Boolean read;

    public BookResponseDTO() {
    }

    public BookResponseDTO(Long id, String title, String author, Integer publicationYear,
                           String genre, String language, Integer pageCount,
                           LocalDate addedDate, Boolean read) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publicationYear = publicationYear;
        this.genre = genre;
        this.language = language;
        this.pageCount = pageCount;
        this.addedDate = addedDate;
        this.read = read;
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

    public LocalDate getAddedDate() { return addedDate; }
    public void setAddedDate(LocalDate addedDate) { this.addedDate = addedDate; }

    public Boolean getRead() { return read; }
    public void setRead(Boolean read) { this.read = read; }
}