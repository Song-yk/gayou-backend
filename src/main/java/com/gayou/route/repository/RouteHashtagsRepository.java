package com.gayou.route.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gayou.route.model.RouteHashtags;

public interface RouteHashtagsRepository extends JpaRepository<RouteHashtags, Long> {

    void deleteByRouteHeadId(Long routeHeadId);
}
