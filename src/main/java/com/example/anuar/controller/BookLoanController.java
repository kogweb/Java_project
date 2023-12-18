package com.example.anuar.controller;

import com.example.anuar.dto.BookLoanResponse;
import com.example.anuar.entity.BookLoan;
import com.example.anuar.service.BookLoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/book-loans")
@RequiredArgsConstructor
public class BookLoanController {

    private final BookLoanService bookLoanService;

    @PostMapping("/{bookId}/{accountId}")
    public ResponseEntity<BookLoanResponse> createBookLoan(@PathVariable Long bookId, @PathVariable Long accountId) {
        BookLoanResponse bookLoan = bookLoanService.createBookLoan(bookId, accountId);
        return new ResponseEntity<>(bookLoan, HttpStatus.CREATED);
    }

    @PutMapping("/return/{bookLoanId}")
    public ResponseEntity<String> returnBook(@PathVariable Long bookLoanId) {
        bookLoanService.returnBook(bookLoanId);
        return new ResponseEntity<>("Book returned successfully", HttpStatus.OK);
    }

    @DeleteMapping("/{bookLoanId}")
    public ResponseEntity<String> deleteBookLoan(@PathVariable Long bookLoanId) {
        bookLoanService.deleteBookLoan(bookLoanId);
        return new ResponseEntity<>("BookLoan deleted successfully", HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<BookLoanResponse>> getAllBookLoans() {
        List<BookLoanResponse> bookLoans = bookLoanService.getAllBookLoans();
        return new ResponseEntity<>(bookLoans, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BookLoanResponse>> getBookLoansByUser(@PathVariable Long userId) {
        List<BookLoanResponse> bookLoans = bookLoanService.getBookLoansByUser(userId);
        return new ResponseEntity<>(bookLoans, HttpStatus.OK);
    }

    @GetMapping("/{bookLoanId}")
    public ResponseEntity<BookLoanResponse> getBookLoanById(@PathVariable Long bookLoanId) {
        BookLoanResponse bookLoan = bookLoanService.getBookLoanById(bookLoanId);
        return new ResponseEntity<>(bookLoan, HttpStatus.OK);
    }
}
