package com.example.anuar.service;

import com.example.anuar.dto.BookDTO;
import com.example.anuar.dto.BookLoanResponse;
import com.example.anuar.dto.BookResponse;
import com.example.anuar.dto.GenreDTO;
import com.example.anuar.entity.*;
import com.example.anuar.repository.AuthorRepository;
import com.example.anuar.repository.BookRepository;
import com.example.anuar.repository.GenreRepository;
import com.example.anuar.repository.GenreTranslationRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;
    private final AuthorRepository authorRepository;
    private final GenreTranslationRepository genreTranslationRepository;
    private final EntityManager entityManager;

    public BookDTO createBook(BookDTO bookDTO, List<GenreTranslation> genreTranslations) {
        Author author = authorRepository.findByName(bookDTO.getAuthor());

// Create a new Book instance and set its properties
        Book book = new Book();
        book.setTitle(bookDTO.getTitle());
        book.setDescription(bookDTO.getDescription());
        book.setPublicationYear(bookDTO.getPublicationYear());
        book.setAuthor(author);

// Save the book to get its ID
        Book savedBook = bookRepository.save(book);

// Create a new Genre instance and set its properties
        Genre genre = new Genre();
        genre.setTranslations(genreTranslations);

// Save the genre to get its ID
        genreRepository.save(genre);

// Set the genreTranslations' genre to the saved genre
        for (GenreTranslation genreTranslation : genreTranslations) {
            genreTranslation.setGenre(genre);
            genreTranslationRepository.save(genreTranslation);
        }

// Update the saved book's genre
        savedBook.setGenre(genre);
        bookRepository.save(savedBook);

// Create and return a new BookDTO with savedBook's properties
        BookDTO createdBookDTO = new BookDTO();
        createdBookDTO.setTitle(savedBook.getTitle());
        createdBookDTO.setDescription(savedBook.getDescription());
        createdBookDTO.setPublicationYear(savedBook.getPublicationYear());
        createdBookDTO.setAuthor(savedBook.getAuthor().getName());

        return createdBookDTO;

    }

    public Book updateBook(Long bookId, Book updatedBook) {
        Book existingBook = getBookById(bookId);
        existingBook.setTitle(updatedBook.getTitle());
        existingBook.setDescription(updatedBook.getDescription());
        existingBook.setAuthor(updatedBook.getAuthor());
        existingBook.setGenre(updatedBook.getGenre());
        existingBook.setPublicationYear(updatedBook.getPublicationYear());
        // Другие поля, если необходимо
        return bookRepository.save(existingBook);
    }

    public void deleteBook(Long bookId) {
        bookRepository.deleteById(bookId);
    }

    public List<BookResponse> getAllBooks(String lang, int page, int size, String sortBy, String order) {
        Sort sort = order.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Book> all = bookRepository.findAll(pageable);

        return all.stream()
                .map(book -> convertBookDTO(book,lang))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private BookResponse convertBookDTO(Book book, String lang) {
        BookResponse bookDTO = new BookResponse();
        if (book.getGenre().getTranslations() != null && !book.getGenre().getTranslations().isEmpty()){
            GenreTranslation genreTranslation = book.getGenre().getTranslations().stream()
                    .filter(genreTranslation1 -> lang.equals(genreTranslation1.getLanguageCode()))
                    .findFirst()
                    .orElse(null);

            if (genreTranslation != null) {
                bookDTO.setId(bookDTO.getId());
                bookDTO.setTitle(book.getTitle());
                bookDTO.setAuthor(book.getAuthor().getName());
                bookDTO.setDescription(book.getDescription());
                bookDTO.setTranslatedGenre(genreTranslation.getTranslatedName());
                bookDTO.setPublicationYear(book.getPublicationYear());
                return bookDTO;
            }
        }

        return null;
    }

    public Book getBookById(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + bookId));
    }
}
