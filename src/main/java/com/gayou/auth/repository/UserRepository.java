package com.gayou.auth.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gayou.auth.model.AccountStatus;
import com.gayou.auth.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAllByStatusAndLastLoginTimeBefore(AccountStatus status, Date time);

    Optional<User> findByEmail(String email);

    Optional<User> findById(Long id);
}
