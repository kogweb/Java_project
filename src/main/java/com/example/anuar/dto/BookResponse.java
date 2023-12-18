package com.example.anuar.dto;

import lombok.Data;

@Data
public class BookResponse {
    private Long id;
    private String title;
    private String description;
    private Integer publicationYear;
    private String author;

    private String translatedGenre;
}
