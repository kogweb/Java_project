package com.example.anuar.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "genre_translation")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenreTranslation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "genre_id")
    private Genre genre;
    @Column(name = "language_code")
    private String languageCode;

    @Column(name = "translated_name")
    private String translatedName;

}
