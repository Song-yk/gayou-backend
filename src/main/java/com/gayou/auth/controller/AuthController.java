package com.gayou.auth.controller;

import com.gayou.auth.dto.LoginResponse;
import com.gayou.auth.dto.UserDto;
import com.gayou.auth.service.UserService;
import com.gayou.auth.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    // 생성자를 통한 의존성 주입
    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * 로그인 엔드포인트
     * 
     * @param userDto - 사용자의 로그인 정보 (username, password)
     * @return JWT 토큰이 담긴 ResponseEntity
     * 
     *         사용자의 로그인 요청을 처리하고, 인증에 성공하면 JWT 토큰을 발급하여 반환합니다.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDto userDto) {
        // 로그인 처리 후 JWT 토큰 발급
        LoginResponse response = userService.authenticate(userDto);
        return ResponseEntity.ok(response);
    }

    /**
     * 회원 가입 엔드포인트
     * 
     * @param userDto - 회원 가입할 사용자의 정보 (username, password, name, email,
     *                phoneNumber, birthday)
     * @return 성공 메시지가 담긴 ResponseEntity
     * 
     *         새로운 사용자를 데이터베이스에 저장합니다. 중복된 username이 있을 경우 예외가 발생합니다.
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDto userDto) {
        // 회원 가입 처리
        userService.register(userDto);
        return ResponseEntity.ok("User registered successfully");
    }

    /**
     * 사용자 정보 조회 엔드포인트
     * 
     * @param request - HTTP 요청 (Authorization 헤더에 JWT 토큰이 있어야 함)
     * @return 사용자 정보(UserDto)가 담긴 ResponseEntity
     * 
     *         클라이언트가 Authorization 헤더에 담긴 JWT 토큰을 통해 현재 로그인한 사용자의 정보를 조회할 수 있습니다.
     *         JWT 토큰이 없거나 유효하지 않으면 401 Unauthorized 응답을 반환합니다.
     */
    @GetMapping("/me")
    public ResponseEntity<?> getMyInfo(HttpServletRequest request) {
        // Authorization 헤더에서 JWT 토큰 추출
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Missing or invalid Authorization header");
        }

        // JWT 토큰에서 사용자 정보를 추출하고, 사용자 정보를 반환
        String token = authorizationHeader.substring(7);
        UserDto userDto = userService.getUserDetails(token);
        return ResponseEntity.ok(userDto);
    }

    /**
     * 회원 탈퇴 엔드포인트
     * 
     * @param request - HTTP 요청 (Authorization 헤더에 JWT 토큰이 있어야 함)
     * @return 성공 메시지가 담긴 ResponseEntity
     * 
     *         현재 로그인한 사용자의 계정을 삭제합니다. JWT 토큰을 통해 사용자를 인증한 후, 해당 사용자를 데이터베이스에서
     *         삭제합니다.
     *         JWT 토큰이 없거나 유효하지 않으면 401 Unauthorized 응답을 반환합니다.
     */
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(HttpServletRequest request) {
        // Authorization 헤더에서 JWT 토큰 추출
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Missing or invalid Authorization header");
        }

        // JWT 토큰에서 사용자 정보를 추출하고, 해당 사용자를 삭제
        String token = authorizationHeader.substring(7);
        userService.deleteUser(token);
        return ResponseEntity.ok("User deleted successfully");
    }
}
