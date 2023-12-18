package com.example.anuar.dto;

import com.example.anuar.entity.GenreTranslation;
import lombok.Data;

import java.util.List;

@Data
public class BookDTO {
    private String title;
    private String description;
    private Integer publicationYear;
    private String author;
}
