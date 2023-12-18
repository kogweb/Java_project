package com.example.anuar.dto;

import com.example.anuar.entity.Book;
import com.example.anuar.entity.GenreTranslation;
import lombok.Data;

import java.util.List;

@Data
public class BookWithTranslationDto {
    private BookDTO book;
    private List<GenreTranslation> translations;
}
