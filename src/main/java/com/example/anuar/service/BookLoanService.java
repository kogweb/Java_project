package com.example.anuar.service;

import com.example.anuar.dto.BookLoanResponse;
import com.example.anuar.entity.Account;
import com.example.anuar.entity.Book;
import com.example.anuar.entity.BookLoan;
import com.example.anuar.repository.AccountRepository;
import com.example.anuar.repository.BookLoanRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookLoanService {

    private final BookLoanRepository bookLoanRepository;
    private final BookService bookService;
    private final AccountRepository accountService;

    public BookLoanResponse createBookLoan(Long bookId, Long accountId) {
        Book book = bookService.getBookById(bookId);
        Account account = accountService.findById(accountId).orElseThrow();

        if (bookLoanRepository.existsByBookAndUserAndReturnDateIsNull(book, account)) {
            // Возвращаем ошибку или выбрасываем исключение, так как заказ уже существует
            throw new RuntimeException("Заказ для данной книги и пользователя уже существует");
        }

        BookLoan bookLoan = BookLoan.builder()
                .book(book)
                .user(account)
                .loanDate(LocalDateTime.now())
                .build();

         bookLoanRepository.save(bookLoan);

         BookLoanResponse bookLoanResponse = new BookLoanResponse();
         bookLoanResponse.setTitle(book.getTitle());
         bookLoanResponse.setName(account.getFirstName());
         bookLoanResponse.setLoanDate(LocalDateTime.now());
         bookLoanResponse.setReturnDate(LocalDateTime.now());
         return bookLoanResponse;
    }

    public void returnBook(Long bookLoanId) {
        BookLoan bookLoan = bookLoanRepository.getById(bookLoanId);
        bookLoan.setReturnDate(LocalDateTime.now());
        bookLoanRepository.save(bookLoan);
    }

    public void deleteBookLoan(Long bookLoanId) {
        bookLoanRepository.deleteById(bookLoanId);
    }

    public List<BookLoanResponse> getAllBookLoans() {
        List<BookLoan> all = bookLoanRepository.findAll();

        return all.stream()
                .map(this::convertBookLoanResponse)
                .collect(Collectors.toList());
    }

    private BookLoanResponse convertBookLoanResponse(BookLoan bookLoan) {
        BookLoanResponse bookLoanResponse = new BookLoanResponse();
        bookLoanResponse.setTitle(bookLoan.getBook().getTitle());
        bookLoanResponse.setName(bookLoan.getUser().getFirstName());
        bookLoanResponse.setLoanDate(bookLoan.getLoanDate());
        bookLoanResponse.setReturnDate(bookLoan.getReturnDate());
        return bookLoanResponse;
    }

    public List<BookLoanResponse> getBookLoansByUser(Long userId) {
        List<BookLoan> byUserId = bookLoanRepository.findByUserId(userId);

        return byUserId.stream()
                .map(this::convertBookLoanResponse)
                .collect(Collectors.toList());
    }

    public BookLoanResponse getBookLoanById(Long bookLoanId) {
        BookLoan bookLoan = bookLoanRepository.findById(bookLoanId)
                .orElseThrow(() -> new EntityNotFoundException("BookLoan not found with id: " + bookLoanId));
        return convertBookLoanResponse(bookLoan);

    }
}
