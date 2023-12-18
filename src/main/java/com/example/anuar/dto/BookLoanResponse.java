package com.example.anuar.dto;

import com.example.anuar.entity.Account;
import com.example.anuar.entity.Book;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookLoanResponse {
    private String title;
    private String name;
    private LocalDateTime loanDate;
    private LocalDateTime returnDate;
}
