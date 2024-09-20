package com.gayou.route.repository;

import java.util.List;
import com.gayou.route.model.RouteHead;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteHeadRepository extends JpaRepository<RouteHead, Long> {
    List<RouteHead> findAllByUserId(Long userId);
}
