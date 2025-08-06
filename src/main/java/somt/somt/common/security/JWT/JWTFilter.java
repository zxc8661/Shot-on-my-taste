package somt.somt.common.security.JWT;


import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import somt.somt.common.exception.CustomException;
import somt.somt.common.exception.ErrorCode;
import somt.somt.common.redis.RedisRepository;
import somt.somt.common.security.dto.CustomUserData;
import somt.somt.common.security.dto.CustomUserDetails;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    final private JWTUtil jwtUtil;
    final private RedisRepository redisRepository;

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    private static final List<String> WHITELIST = List.of(
            "/",
            "/error",
            "/css/**",
            "/js/**",
            "/images/**",
            "/favicon.ico",
            "/api/member/login",
            "/api/member/register",
            "/api/member/logout",
            "/api/public/**",
            "/v3/api-docs"
    );


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return WHITELIST.stream()
                .anyMatch(pattern -> pathMatcher.match(pattern, path));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        String accessToken = request.getHeader("access");

        String refreshToken = getRefreshToken(request.getCookies());

        if (refreshToken == null) {
            throw new CustomException(ErrorCode.TOKEN_NOT_EFFECTIVE);
        }

        //토큰 존재 확인
        if (accessToken == null) {
            filterChain.doFilter(request, response);
            return;
        }
        // 토큰 만료 확인
            if(jwtUtil.isExpired(accessToken)){
                throw new CustomException(ErrorCode.ACCESSTOKEN_IS_EXPIRED);

            }

        // 토큰 종류 확인
        if (!jwtUtil.getCategory(accessToken).equals("access")) {
            throw new CustomException(ErrorCode.NOT_ACCESSTOKEN);
        }

        String redisAccessToken = redisRepository.getValue(refreshToken);

        //Db와 비교
        if (!accessToken.equals(redisAccessToken)) {
            redisRepository.delete(refreshToken);
            throw new CustomException(ErrorCode.TOKEN_MISMATCH);
        }

        String username = jwtUtil.getUsername(accessToken);
        Long memberId = jwtUtil.getMemberId(accessToken);
        String role = jwtUtil.getRole(accessToken);
        String nickname = jwtUtil.getNickname(accessToken);


        if (username == null || memberId == null || role == null) {
            throw new CustomException(ErrorCode.TOKEN_NOT_EFFECTIVE);
        }


//        CustomUserData customUserData = new CustomUserData(memberId, username, nickname, role, "tmp");
        CustomUserData customUserData = new CustomUserData(memberId,username,role,"tmp",nickname);



        CustomUserDetails customUserDetails = new CustomUserDetails(customUserData);

        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);


    }



    private String getRefreshToken(Cookie[] cookies) {


        if(cookies==null) throw new CustomException(ErrorCode.NOT_FOUND_REFRESHTOKEN);

        for(Cookie cookie: cookies){
            if(cookie.getName().equals("refresh")){

                return cookie.getValue();
            }
        }

        throw new CustomException(ErrorCode.NOT_FOUND_REFRESHTOKEN);
    }



}

