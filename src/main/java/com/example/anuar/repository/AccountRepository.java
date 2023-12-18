package com.example.anuar.repository;

import com.example.anuar.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account,Integer> {

    Optional<Account> findById(Long AccountId);
    Optional<Account> findByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE Account a SET a.firstName = :#{#account.firstName}, a.lastName = :#{#account.lastName}, a.email = :#{#account.email}, a.password = :#{#account.password} WHERE a.id = :accountId")
    void updateAccount(@Param("account") Account Account);


}
