package com.gayou.route.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gayou.route.dto.RouteHeadDto;
import com.gayou.route.service.RouteService;

@RestController
@RequestMapping("/route")
public class RouteController {

    @Autowired
    private RouteService routeService;

    /**
     * 사용자의 경로를 저장하는 메서드
     *
     * @param email    - 현재 인증된 사용자의 사용자 이메일 (JWT 토큰에서 추출된 값)
     * @param routeDTO - 저장할 경로 정보 (RouteHeadDto)
     * @return ResponseEntity<Long> - 생성된 경로의 ID와 함께 HTTP 상태 코드를 반환
     */
    @PostMapping("/locations")
    public ResponseEntity<Long> saveCourse(@AuthenticationPrincipal String email,
            @RequestBody RouteHeadDto routeDTO) {

        Long routeHeadId = routeService.saveRoute(routeDTO, email);
        return new ResponseEntity<>(routeHeadId, HttpStatus.CREATED);
    }

    /**
     * 사용자가 저장한 경로 목록을 반환하는 메서드
     *
     * @param email - 현재 인증된 사용자의 사용자 이메일 (JWT 토큰에서 추출된 값)
     * @return ResponseEntity<List<RouteHeadDto>> - 사용자가 저장한 경로 목록을 반환
     */
    @GetMapping("/locations")
    public ResponseEntity<?> getMyCourse(@AuthenticationPrincipal String email) {
        List<RouteHeadDto> data = routeService.getMyRoute(email);
        return ResponseEntity.ok(data);
    }

    @GetMapping("/data")
    public ResponseEntity<?> getPostData(@AuthenticationPrincipal String email, @RequestParam("id") Long id) {
        return ResponseEntity.ok(routeService.getRoute(id)); // 특정 ID에 해당하는 데이터를 반환
    }

    @PutMapping("/post")
    public ResponseEntity<?> updateroutehead(@AuthenticationPrincipal String email,
            @RequestBody RouteHeadDto routeDTO) {

        routeService.updateRouteHead(routeDTO);

        return ResponseEntity.ok("success update");
    }

    @PutMapping("/like")
    public ResponseEntity<?> updatelike(@AuthenticationPrincipal String email,
            @RequestBody RouteHeadDto routeDTO) {

        routeService.updateLike(routeDTO);

        return ResponseEntity.ok("success update");
    }
}
