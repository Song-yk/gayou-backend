package com.gayou.route.controller;

import com.gayou.route.dto.RouteHeadDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.gayou.route.service.RouteService;

@RestController
@RequestMapping("/route")
public class RouteController {

    @Autowired
    private RouteService routeService;

    @PostMapping("/locations")
    public ResponseEntity<RouteHeadDto> saveCourse(@RequestBody RouteHeadDto routeDTO) {
        routeService.saveRoute(routeDTO);
        return new ResponseEntity<>(routeDTO, HttpStatus.CREATED);
    }
}
