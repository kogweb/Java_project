package com.example.anuar.repository;

import com.example.anuar.entity.Genre;
import com.example.anuar.entity.GenreTranslation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GenreTranslationRepository extends JpaRepository<GenreTranslation,Long> {
    GenreTranslation findByTranslatedName(String genre);
    List<GenreTranslation> findAllByTranslatedName(String genre);
}
