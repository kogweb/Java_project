package com.example.anuar.controller;

import com.example.anuar.dto.BookDTO;
import com.example.anuar.dto.BookResponse;
import com.example.anuar.dto.BookWithTranslationDto;
import com.example.anuar.entity.Book;
import com.example.anuar.entity.GenreTranslation;
import com.example.anuar.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping
    public ResponseEntity<BookDTO> createBook(@RequestBody BookWithTranslationDto bookWithTranslationDto) {
        BookDTO book = bookWithTranslationDto.getBook();
        List<GenreTranslation> translations = bookWithTranslationDto.getTranslations();
        BookDTO createdBook = bookService.createBook(book,translations);
        return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
    }

    @PutMapping("/{bookId}")
    public ResponseEntity<Book> updateBook(@PathVariable Long bookId, @RequestBody Book updatedBook) {
        Book book = bookService.updateBook(bookId, updatedBook);
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<String> deleteBook(@PathVariable Long bookId) {
        bookService.deleteBook(bookId);
        return new ResponseEntity<>("Book deleted successfully", HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<BookResponse>> getAllBooks(
            @RequestHeader("lang") String lang,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String order) {
        List<BookResponse> books = bookService.getAllBooks(lang, page, size, sortBy, order);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<Book> getBookById(@PathVariable Long bookId) {
        Book book = bookService.getBookById(bookId);
        return new ResponseEntity<>(book, HttpStatus.OK);
    }
}
