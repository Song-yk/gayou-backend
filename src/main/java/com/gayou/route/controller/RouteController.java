package com.gayou.route.controller;

import com.gayou.auth.dto.UserDto;
import com.gayou.route.dto.RouteHeadDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.gayou.route.service.RouteService;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/route")
public class RouteController {

    @Autowired
    private RouteService routeService;

    @PostMapping("/locations")
    public ResponseEntity<RouteHeadDto> saveCourse(@AuthenticationPrincipal String username,
            @RequestBody RouteHeadDto routeDTO) {

        routeService.saveRoute(routeDTO, username);
        return new ResponseEntity<>(routeDTO, HttpStatus.CREATED);
    }

    @GetMapping("/locations")
    public ResponseEntity<?> getMyCourse(@AuthenticationPrincipal String username) {
        List<RouteHeadDto> data = routeService.getMyRoute(username);
        return ResponseEntity.ok(data);
    }
}
