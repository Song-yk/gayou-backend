package com.gayou.route.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gayou.auth.model.User;
import com.gayou.route.model.RouteLike;
import com.gayou.route.model.RouteHead;

public interface RouteLikeRepository extends JpaRepository<RouteLike, Long> {
    RouteLike findByRouteHeadAndUser(RouteHead routeHead, User user);

    List<RouteLike> findAllByUser(User user);
}
