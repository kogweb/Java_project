package com.example.anuar.service;

import com.example.anuar.dto.GenreDTO;
import com.example.anuar.entity.Genre;
import com.example.anuar.entity.GenreTranslation;
import com.example.anuar.repository.GenreRepository;
import com.example.anuar.repository.GenreTranslationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreRepository genreRepository;
    private final GenreTranslationRepository genreTranslationRepository;

    public GenreDTO createGenre(GenreDTO genreDTO) {
        Genre genre = new Genre();
        GenreTranslation genreTranslation = new GenreTranslation();
        genreTranslation.setTranslatedName(genreDTO.getName());
        genre.setTranslations(Collections.singletonList(genreTranslation));
        genreTranslationRepository.save(genreTranslation);
        genreRepository.save(genre);
        return genreDTO;
    }

    public void deleteGenre(Long genreId) {
        Genre genre = getGenreById(genreId);
        genreRepository.delete(genre);
    }

    public List<GenreDTO> getAllGenres(int page, int size, String sortBy, String order, String lang) {
        Sort sort = order.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Genre> all = genreRepository.findAll(pageable);

        return all.stream()
                .map(genre -> convertToGenreDTO(genre,lang))
                .toList();
    }

    private GenreDTO convertToGenreDTO(Genre genre, String lang){
        GenreDTO genreDTO = new GenreDTO();
        if (genre.getTranslations() != null && !genre.getTranslations().isEmpty()){
            genre.getTranslations().stream()
                    .filter(genreTranslation1 -> lang.equals(genreTranslation1.getLanguageCode()))
                    .findFirst().ifPresent(genreTranslation -> genreDTO.setName(genreTranslation.getTranslatedName()));
        }
        return genreDTO;
    }

    public GenreDTO getGenreById(Long genreId,String lang) {
        Genre genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new EntityNotFoundException("Genre not found with id: " + genreId));
        if (genre.getTranslations() != null && !genre.getTranslations().isEmpty()){
            GenreTranslation genreTranslation = genre.getTranslations().stream()
                    .filter(genreTranslation1 -> lang.equals(genreTranslation1.getLanguageCode()))
                    .findFirst()
                    .orElse(null);

            if (genreTranslation != null) {
                GenreDTO genreDTO = new GenreDTO();
                genreDTO.setName(genreTranslation.getTranslatedName());
                return genreDTO;
            }
        }
        return null;
    }

    public Genre getGenreById(Long genreId) {
        return genreRepository.findById(genreId)
                .orElseThrow(() -> new EntityNotFoundException("Genre not found with id: " + genreId));
    }
}
