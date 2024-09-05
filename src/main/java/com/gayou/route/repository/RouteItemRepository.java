package com.gayou.route.repository;

import com.gayou.route.model.RouteItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteItemRepository extends JpaRepository<RouteItem, Long> {
}
