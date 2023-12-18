package com.example.anuar.controller;

import com.example.anuar.dto.GenreDTO;
import com.example.anuar.entity.Genre;
import com.example.anuar.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @PostMapping
    public ResponseEntity<GenreDTO> createGenre(@RequestBody GenreDTO genre) {
        GenreDTO createdGenre = genreService.createGenre(genre);
        return new ResponseEntity<>(createdGenre, HttpStatus.CREATED);
    }

    @DeleteMapping("/{genreId}")
    public ResponseEntity<String> deleteGenre(@PathVariable Long genreId) {
        genreService.deleteGenre(genreId);
        return new ResponseEntity<>("Genre deleted successfully", HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<GenreDTO>> getAllGenres(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "3") int size,
                                                       @RequestParam(defaultValue = "id") String sortBy,
                                                       @RequestParam(defaultValue = "desc") String order,
                                                       @RequestHeader("lang") String lang) {
        List<GenreDTO> genres = genreService.getAllGenres(page, size, sortBy, order,lang);
        return new ResponseEntity<>(genres, HttpStatus.OK);
    }

    @GetMapping("/{genreId}")
    public ResponseEntity<GenreDTO> getGenreById(@PathVariable Long genreId,@RequestHeader("lang") String lang) {
        GenreDTO genre = genreService.getGenreById(genreId,lang);
        return new ResponseEntity<>(genre, HttpStatus.OK);
    }
}

