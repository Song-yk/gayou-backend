package com.gayou.route.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.gayou.auth.model.User;
import com.gayou.route.model.RouteComment;
import com.gayou.route.model.RouteHead;

public interface RouteCommentRepository extends JpaRepository<RouteComment, Long> {
    List<RouteComment> findAllByRouteHead(RouteHead routeHead, Sort sort);

    RouteComment findByRouteHeadAndUser(RouteHead routeHead, User user);
}
