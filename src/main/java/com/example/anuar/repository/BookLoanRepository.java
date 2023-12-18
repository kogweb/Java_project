package com.example.anuar.repository;

import com.example.anuar.entity.Account;
import com.example.anuar.entity.Book;
import com.example.anuar.entity.BookLoan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookLoanRepository extends JpaRepository<BookLoan,Long> {
    List<BookLoan> findByUserId(Long userId);

    boolean existsByBookAndUserAndReturnDateIsNull(Book book, Account account);
}
