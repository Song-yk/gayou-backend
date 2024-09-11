package com.gayou.auth.repository;

import com.gayou.auth.model.User;
import com.gayou.auth.model.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAllByStatusAndLastLoginTimeBefore(AccountStatus status, Date time);

    java.util.Optional<User> findByUsername(String username);
}
