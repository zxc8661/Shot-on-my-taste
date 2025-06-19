package somt.somt.common.security.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import somt.somt.common.redis.RedisRepository;
import somt.somt.common.security.JWT.JWTFilter;
import somt.somt.common.security.JWT.JWTUtil;
import somt.somt.common.security.JWT.LoginFilter;
import somt.somt.common.security.JWT.LogoutFilter;


/**
 * 시큐리티 설정
 *
 * @since 2025-06-19
 * @author  이광석
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;
    private final RedisRepository redisRepository;



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
        LoginFilter loginFilter = new LoginFilter(authenticationManager(authenticationConfiguration),jwtUtil, redisRepository);
        loginFilter.setFilterProcessesUrl("/api/member/login");




        http
                .csrf((auth) -> auth.disable())

                .formLogin((auth) -> auth.disable())

                .httpBasic((auth) -> auth.disable())

                .authorizeHttpRequests((auth) -> auth

                        .requestMatchers(HttpMethod.GET, "/api/posts").permitAll()
                        .requestMatchers(HttpMethod.POST,
                                "/api/member/login" ,
                                "/api/member/register" ,
                                "/api/member/logout").permitAll()
                        .anyRequest().authenticated() // 인증 필요
                )
                .headers(headers -> headers
                        .defaultsDisabled()
                        .frameOptions(frame -> frame.sameOrigin())
                )

                .addFilterBefore(new JWTFilter(jwtUtil,redisRepository),UsernamePasswordAuthenticationFilter.class)  // jwt 유효성 검사

                .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class) // 로그인 유효성 검사

                .addFilterBefore(new LogoutFilter(jwtUtil, redisRepository),  UsernamePasswordAuthenticationFilter.class)

                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));  //세션 stateless 상태 설정

        return http.build();
    }
}