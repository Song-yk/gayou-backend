package com.gayou.route.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gayou.route.model.RouteItem;

@Repository
public interface RouteItemRepository extends JpaRepository<RouteItem, Long> {
}
