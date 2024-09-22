package com.gayou.settings.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.gayou.settings.filter.JwtAuthenticationFilter;

import org.springframework.security.web.AuthenticationEntryPoint;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;
import java.io.IOException;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

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
                .cors().and() // CORS 설정
                .csrf().disable() // CSRF 보호 비활성화
                .httpBasic().disable() // 기본 HTTP 인증 비활성화
                // 세션 관리 정책을 상태 비저장 방식으로 설정 (JWT 사용)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeHttpRequests() // 요청에 대한 인가 설정
                // 로그인, 회원가입, 카카오 콜백 엔드포인트는 인증 필요 없음
                .requestMatchers("/auth/login", "/auth/register", "/email/join", "auth/kakao/callback").permitAll()

                .anyRequest().authenticated().and() // 그 외 모든 요청은 인증 필요
                // 인증 실패 시 처리할 핸들러 설정
                .exceptionHandling().authenticationEntryPoint(new FailedAuthenticationEntryPoint());

        // JWT 인증 필터를 UsernamePasswordAuthenticationFilter 앞에 추가
        httpSecurity.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build(); // 보안 필터 체인 빌드
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
        response.setContentType("application/json"); // 응답의 컨텐츠 타입을 JSON으로 설정
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 응답 상태 코드를 401 (Unauthorized)로 설정
        // JSON 형식으로 인증 실패 메시지 작성
        response.getWriter().write("{ \"message\": \"Authentication has failed. Please check your credentials.\" }");
    }
}
