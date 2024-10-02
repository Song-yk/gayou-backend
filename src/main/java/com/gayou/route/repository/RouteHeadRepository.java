package com.gayou.route.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gayou.route.model.RouteHead;

@Repository
public interface RouteHeadRepository extends JpaRepository<RouteHead, Long> {
    List<RouteHead> findAllByUserIdAndCourseNameIsNull(Long userId, Sort sort);

    List<RouteHead> findAllByUserIdAndCourseNameIsNotNull(Long userId, Sort sort);

    Optional<RouteHead> findById(Long id);

    List<RouteHead> findByIsPublicAndUserIdNot(boolean isPublic, Long userId, Sort sort);
}
