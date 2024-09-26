package com.gayou.route.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gayou.route.model.RouteHead;

@Repository
public interface RouteHeadRepository extends JpaRepository<RouteHead, Long> {
    List<RouteHead> findAllByUserId(Long userId);

    Optional<RouteHead> findById(Long id);
}
