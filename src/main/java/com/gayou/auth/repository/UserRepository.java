package com.gayou.auth.repository;

import com.gayou.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
