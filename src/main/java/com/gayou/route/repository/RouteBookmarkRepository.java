package com.gayou.route.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gayou.auth.model.User;
import com.gayou.route.model.RouteBookmark;
import com.gayou.route.model.RouteHead;

public interface RouteBookmarkRepository extends JpaRepository<RouteBookmark, Long> {
    RouteBookmark findByRouteHeadAndUser(RouteHead routeHead, User user);

    List<RouteBookmark> findAllByUser(User user);
}
