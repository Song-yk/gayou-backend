package com.gayou.settings.config;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.gayou.settings.filter.JwtAuthenticationFilter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    @Value("${cors.allowed.origins}")
    private String CORS_ALLOWED_ORIGINS;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * 보안 설정을 정의하는 메서드
     *
     * @param httpSecurity - HttpSecurity 객체로 보안 구성을 설정
     * @return SecurityFilterChain - 설정된 보안 필터 체인
     * @throws Exception - 설정 중 오류 발생 시 예외 처리
     */
    @Bean
    protected SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(csrf -> csrf.disable()) // CSRF 비활성화
                .httpBasic(httpBasic -> httpBasic.disable()) // HTTP 기본 인증 비활성화
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login", "/auth/register", "/email/join", "/locations/*",
                                "/auth/kakao/callback")
                        .permitAll()
                        .anyRequest().authenticated()) // 인증 요청 설정
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(new FailedAuthenticationEntryPoint())); // 인증 실패 시 처리

        httpSecurity.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // JWT 필터 추가

        return httpSecurity.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(CORS_ALLOWED_ORIGINS)); // 허용할 도메인 설정
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); // 허용할 메서드 설정
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

/**
 * 인증 실패 시 처리하는 엔트리 포인트
 */
class FailedAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * 인증 실패 시 실행되는 메서드
     *
     * @param request       - HttpServletRequest 객체
     * @param response      - HttpServletResponse 객체
     * @param authException - 인증 실패 시 발생하는 예외
     * @throws IOException      - 입출력 예외 처리
     * @throws ServletException - 서블릿 관련 예외 처리
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        // response.setContentType("application/json"); // 응답의 컨텐츠 타입을 JSON으로 설정
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 응답 상태 코드를 401 (Unauthorized)로 설정
        // JSON 형식으로 인증 실패 메시지 작성
        response.getWriter().write("{ \"message\": \"Authentication has failed. Please check your credentials.\" }");
    }
}
