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
    public ResponseEntity<?> saveCourse(@AuthenticationPrincipal String email,
            @RequestBody RouteHeadDto routeDTO) {
        try {
            Long routeHeadId = routeService.saveRoute(routeDTO, email);
            return ResponseEntity.status(HttpStatus.CREATED).body(routeHeadId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("잘못된 경로 데이터입니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("경로 저장 중 문제가 발생했습니다.");
        }
    }

    /**
     * 사용자가 저장한 경로 목록을 반환하는 메서드
     *
     * @param email - 현재 인증된 사용자의 사용자 이메일 (JWT 토큰에서 추출된 값)
     * @return ResponseEntity<List<RouteHeadDto>> - 사용자가 저장한 경로 목록을 반환
     */
    @GetMapping("/locations")
    public ResponseEntity<?> getMyCourse(@AuthenticationPrincipal String email) {
        try {
            List<RouteHeadDto> data = routeService.getMyRoute(email);
            if (data.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("저장된 경로가 없습니다.");
            }
            return ResponseEntity.ok(data);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("경로 목록 조회 중 문제가 발생했습니다.");
        }
    }

    /**
     * 특정 경로 데이터를 ID로 조회하는 메서드
     *
     * @param email - 현재 인증된 사용자의 사용자 이메일 (JWT 토큰에서 추출된 값)
     * @param id    - 조회할 경로의 ID
     * @return ResponseEntity<?> - 해당 경로 데이터를 반환
     */
    @GetMapping("/data")
    public ResponseEntity<?> getPostData(@AuthenticationPrincipal String email, @RequestParam("id") Long id) {
        try {
            RouteHeadDto route = routeService.getRoute(id);
            if (route == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 ID의 경로를 찾을 수 없습니다.");
            }
            return ResponseEntity.ok(route);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("경로 데이터를 조회 중 문제가 발생했습니다.");
        }
    }

    /**
     * 모든 경로 데이터를 조회하는 메서드
     *
     * @param email - 현재 인증된 사용자의 사용자 이메일 (JWT 토큰에서 추출된 값)
     * @return ResponseEntity<?> - 모든 경로 데이터를 반환
     */
    @GetMapping("/datas")
    public ResponseEntity<?> getPostDatas(@AuthenticationPrincipal String email) {
        try {
            List<RouteHeadDto> routes = routeService.getRoutes();
            return ResponseEntity.ok(routes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("모든 경로 데이터를 조회 중 문제가 발생했습니다.");
        }
    }

    /**
     * 경로 정보를 수정하는 메서드
     *
     * @param email    - 현재 인증된 사용자의 사용자 이메일 (JWT 토큰에서 추출된 값)
     * @param routeDTO - 수정할 경로 정보 (RouteHeadDto)
     * @return ResponseEntity<?> - 수정 결과를 반환
     */
    @PutMapping("/post")
    public ResponseEntity<?> updateroutehead(@AuthenticationPrincipal String email,
            @RequestBody RouteHeadDto routeDTO) {
        try {
            routeService.updateRouteHead(routeDTO);
            return ResponseEntity.ok("경로 정보가 성공적으로 업데이트되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("잘못된 경로 데이터입니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("경로 정보 업데이트 중 문제가 발생했습니다.");
        }
    }

    /**
     * 경로 좋아요 정보를 수정하는 메서드
     *
     * @param email    - 현재 인증된 사용자의 사용자 이메일 (JWT 토큰에서 추출된 값)
     * @param routeDTO - 좋아요 정보를 포함한 경로 데이터 (RouteHeadDto)
     * @return ResponseEntity<?> - 수정 결과를 반환
     */
    @PutMapping("/like")
    public ResponseEntity<?> updatelike(@AuthenticationPrincipal String email,
            @RequestBody RouteHeadDto routeDTO) {
        try {
            routeService.updateLike(routeDTO);
            return ResponseEntity.ok("좋아요 정보가 성공적으로 업데이트되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("잘못된 좋아요 정보입니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("좋아요 정보 업데이트 중 문제가 발생했습니다.");
        }
    }

    @PutMapping("/update-public")
    public ResponseEntity<?> updateIsPublic(@AuthenticationPrincipal String email, @RequestParam("id") Long id,
            @RequestParam("isPublic") boolean isPublic) {

        routeService.updateIsPublic(id, isPublic);
        return ResponseEntity.ok("");
    }
}
