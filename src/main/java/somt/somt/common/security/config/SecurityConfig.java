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
@EnableMethodSecurity // @PreAuthorize를 사용하기위함
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;
    private final RedisRepository redisRepository;
    private final ObjectMapper objectMapper;
    private final JwtAuthenticationEntryPoint jwtEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;


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
        LoginFilter loginFilter = new LoginFilter(authenticationManager(authenticationConfiguration),jwtUtil, redisRepository, objectMapper);
        loginFilter.setFilterProcessesUrl("/api/member/login");






        http
                .csrf((auth) -> auth.disable())

                .formLogin((auth) -> auth.disable())

                .httpBasic((auth) -> auth.disable())

                .authorizeHttpRequests((auth) -> auth

                        .requestMatchers("/api/member/login", "/api/member/register", "/api/member/logout","/").permitAll()  // 누구나 접근 가능
                        .requestMatchers("/api/public/**").permitAll()  // 누구나 접근 가능
                        .requestMatchers("/api/user/**").hasAnyAuthority("USER", "ADMIN")  // USER or ADMIN 권한 필요
                        .requestMatchers("/api/admin/**").hasAuthority("ADMIN")  // ADMIN 권한 필요
                        .anyRequest().authenticated() // 인증 필요
                )
                .headers(headers -> headers
                        .defaultsDisabled()
                        .frameOptions(frame -> frame.sameOrigin())
                )

                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(jwtEntryPoint)  // 인증 실패 시 JSON 응답
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                )



                .addFilterBefore(new JWTFilter(jwtUtil,redisRepository),UsernamePasswordAuthenticationFilter.class)  // jwt 유효성 검사

                .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class) // 로그인 유효성 검사

                .addFilterAfter(new LogoutFilter(jwtUtil, redisRepository),  UsernamePasswordAuthenticationFilter.class)

                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));  //세션 stateless 상태 설정

        return http.build();
    }

    @Component
    @RequiredArgsConstructor
    public static class JwtAccessDeniedHandler implements AccessDeniedHandler {

        private final ObjectMapper objectMapper;

        @Override
        public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
                throws IOException, ServletException {

            ErrorResponse err = new ErrorResponse(ErrorCode.NOT_ADMIN);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            objectMapper.writeValue(response.getWriter(), err);
        }
    }

    @Component
    @RequiredArgsConstructor
    public static class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
        private final ObjectMapper objectMapper;


        @Override
        public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
            ErrorCode code = failed.getCause() instanceof CustomException
                    ? ((CustomException)failed.getCause()).getErrorCode()
                    : ErrorCode.NOT_FOUND_MEMBER;

            ErrorResponse err = new ErrorResponse(code);
            response.setStatus(code.getHttpStatus().value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            objectMapper.writeValue(response.getWriter(), err);
        }
    }
}