package somt.somt.common.security.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import somt.somt.common.exception.CustomException;
import somt.somt.common.exception.ErrorCode;
import somt.somt.common.exception.ErrorResponse;
import somt.somt.common.redis.RedisRepository;
import somt.somt.common.security.JWT.*;
import somt.somt.common.security.exception.CustomAccessDeniedHandler;
import somt.somt.common.security.exception.CustomAuthenticationEntryPoint;
import somt.somt.domain.member.service.MemberHistoryService;

import java.io.IOException;


/**
 * 시큐리티 설정
 *
 * @since 2025-06-19
 * @author  이광석
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;
    private final RedisRepository redisRepository;
    private final ObjectMapper objectMapper;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final MemberHistoryService memberHistoryService;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {

        return new BCryptPasswordEncoder();
    }




    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        LoginFilter loginFilter = new LoginFilter(authenticationManager(authenticationConfiguration),jwtUtil, redisRepository, objectMapper,memberHistoryService);
        loginFilter.setFilterProcessesUrl("/api/member/login");






        http
                .csrf((auth) -> auth.disable())

                .formLogin((auth) -> auth.disable())

                .httpBasic((auth) -> auth.disable())

                .securityMatcher("/api/**")
                .authorizeHttpRequests((auth) -> auth

                        .requestMatchers(
                                "/", "/error", "/css/**", "/js/**", "/images/**", "/favicon.ico",
                                "/api/member/login", "/api/member/register", "/api/member/logout", "/api/public/**"
                        ).permitAll()
                        .requestMatchers("/api/user/**").hasAnyAuthority("USER", "ADMIN")  // USER or ADMIN 권한 필요
                        .requestMatchers("/api/admin/**").hasAuthority("ADMIN")  // ADMIN 권한 필요
                        .anyRequest().authenticated() // 인증 필요


                )
                .headers(headers -> headers
                        .defaultsDisabled()
                        .frameOptions(frame -> frame.sameOrigin())
                )

                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(customAuthenticationEntryPoint)  // 인증 실패 시 JSON 응답
                        .accessDeniedHandler(customAccessDeniedHandler)
                )



                .addFilterBefore(new JWTFilter(jwtUtil,redisRepository),UsernamePasswordAuthenticationFilter.class)  // jwt 유효성 검사

                .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class) // 로그인 유효성 검사

                .addFilterAfter(new LogoutFilter(jwtUtil, redisRepository,memberHistoryService),  UsernamePasswordAuthenticationFilter.class)

                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));  //세션 stateless 상태 설정

        return http.build();
    }


}