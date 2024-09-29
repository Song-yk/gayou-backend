package com.gayou.auth.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gayou.auth.dto.LoginResponse;
import com.gayou.auth.dto.UserDto;
import com.gayou.auth.service.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    // application.properties 파일에서 Kakao REST API 키를 주입받음
    @Value("${kakao.rest.api.key}")
    private String KAKAO_REST_API_KEY;
    @Value("${kakao.redirect.uri}")
    private String KAKAO_REDIRECT_URI;

    // UserService 객체를 주입받아 사용
    private final UserService userService;

    // 생성자를 통한 의존성 주입
    public AuthController(UserService userService) {
        this.userService = userService;
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
        try {
            LoginResponse response = userService.authenticate(userDto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 실패: 아이디 또는 비밀번호가 잘못되었습니다.");
        }
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
        try {
            userService.register(userDto);
            return ResponseEntity.ok("");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("회원가입 실패: 이미 존재하는 이메일입니다.");
        }
    }

    /**
     * 카카오 로그인 콜백 처리 엔드포인트
     * 
     * @param body - 클라이언트에서 전달받은 카카오 인증 코드
     * @return JWT 토큰이 담긴 ResponseEntity
     * 
     *         카카오 로그인 처리 후 사용자의 정보를 받아와, 로그인 처리를 하고 JWT 토큰을 발급합니다.
     */
    @PostMapping("/kakao/callback")
    public ResponseEntity<?> kakaoLogin(@RequestBody Map<String, String> body) {
        String code = body.get("code");
        try {
            String accessToken = getKakaoAccessToken(code);
            Map<String, Object> userInfo = getKakaoUserInfo(accessToken);
            Map<String, Object> kakaoAccount = (Map<String, Object>) userInfo.get("kakao_account");

            if (kakaoAccount != null && kakaoAccount.containsKey("email")) {
                LoginResponse response = userService.kakaoLogin((String) kakaoAccount.get("email"));
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("카카오 계정에 이메일이 없습니다.");
            }
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("JSON 처리 중 오류가 발생했습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("로그인 처리 중 오류가 발생했습니다.");
        }
    }

    /**
     * 카카오 API로부터 액세스 토큰을 가져오는 메서드
     * 
     * @param code - 카카오 인증 코드
     * @return 액세스 토큰
     * @throws JsonProcessingException - JSON 파싱 중 오류 발생 시 예외 처리
     */
    private String getKakaoAccessToken(String code) throws JsonProcessingException {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "authorization_code");
            params.add("client_id", KAKAO_REST_API_KEY);
            params.add("redirect_uri", KAKAO_REDIRECT_URI);
            params.add("code", code);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(
                    "https://kauth.kakao.com/oauth/token", request, String.class);

            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readTree(response.getBody()).get("access_token").asText();
        } catch (Exception e) {
            throw new RuntimeException("카카오 서버로부터 액세스 토큰을 가져오는 중 오류가 발생했습니다.");
        }
    }

    /**
     * 카카오 API로부터 사용자 정보를 가져오는 메서드
     * 
     * @param accessToken - 카카오에서 발급받은 액세스 토큰
     * @return 사용자 정보가 담긴 Map
     * @throws JsonProcessingException - JSON 파싱 중 오류 발생 시 예외 처리
     */
    private Map<String, Object> getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    "https://kapi.kakao.com/v2/user/me", HttpMethod.GET, entity, String.class);

            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(response.getBody(), Map.class);
        } catch (Exception e) {
            throw new RuntimeException("카카오 서버로부터 사용자 정보를 가져오는 중 오류가 발생했습니다.");
        }
    }

    /**
     * 특정 사용자의 프로필 정보를 반환하는 엔드포인트
     * 
     * @param email - 현재 인증된 사용자의 사용자 이메일 (JWT 토큰에서 추출된 값)
     * @return 사용자의 프로필 정보가 담긴 ResponseEntity
     */
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@AuthenticationPrincipal String email) {
        try {
            UserDto userDto = userService.getUserProfile(email);
            return ResponseEntity.ok(userDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자의 프로필 정보를 찾을 수 없습니다.");
        }
    }

    /**
     * 사용자의 프로필을 업데이트하는 엔드포인트
     * 
     * @param userDto - 업데이트할 사용자의 정보
     * @return 성공 메시지가 담긴 ResponseEntity
     */
    @PostMapping("/profile/update")
    public ResponseEntity<?> updateProfile(@RequestBody UserDto userDto) {
        try {
            userService.updateProfile(userDto);
            return ResponseEntity.ok("프로필이 성공적으로 업데이트되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("프로필 업데이트 중 오류가 발생했습니다.");
        }
    }

    /**
     * 사용자의 비밀번호를 변경하는 엔드포인트
     * 
     * @param userDto - 비밀번호를 변경할 사용자의 정보
     * @return 성공 메시지가 담긴 ResponseEntity
     */
    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody UserDto userDto) {
        try {
            userService.passwordChange(userDto);
            return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("비밀번호 변경 중 오류가 발생했습니다.");
        }
    }
}
