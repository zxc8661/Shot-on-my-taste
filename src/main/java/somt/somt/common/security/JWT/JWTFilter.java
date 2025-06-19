package somt.somt.common.security.JWT;


import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import somt.somt.common.exception.CustomException;
import somt.somt.common.exception.ErrorCode;
import somt.somt.common.redis.RedisRepository;
import somt.somt.common.security.dto.CustomUserData;
import somt.somt.common.security.dto.CustomUserDetails;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * jwt 를 검증 및 CustomUserDetails 저장하는  클래스
 *
 * @author 이광석
 * @since 2025-03-26
 */
public class JWTFilter extends OncePerRequestFilter {

    final private JWTUtil jwtUtil;
    final private RedisRepository redisRepository;



    public JWTFilter(JWTUtil jwtUtil, RedisRepository redisRepository) {
        this.jwtUtil = jwtUtil;
        this.redisRepository = redisRepository;

    }


    /**
     * Jwt를 검증 및 CustomUserDetails 저장하는 메소드
     *
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     * @author 이광석
     * @since 2025-03-26
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().startsWith("/v3/api-docs") || request.getServletPath().startsWith("/swagger-ui") || request.getServletPath().startsWith("/swagger-resources")
            || (request.getServletPath().equals("/api/posts") && request.getMethod().equals("GET"))) {
            filterChain.doFilter(request, response);
            return;
        }

        List<String> noCertifiedUrls = new ArrayList<>();
        noCertifiedUrls.add("/api/member/login");
        noCertifiedUrls.add("/h2-console");
        noCertifiedUrls.add("/api/member/register");
        noCertifiedUrls.add("/api/login");
        noCertifiedUrls.add("/actuator/health");

        for (String noCertifiedUrl : noCertifiedUrls) {
            if (request.getServletPath().contains(noCertifiedUrl)) {
                filterChain.doFilter(request, response);
                return;
            }
        }

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
        try {
            jwtUtil.isExpired(accessToken);
        } catch (ExpiredJwtException e) {
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


        CustomUserData customUserData = new CustomUserData(memberId, username, nickname, role, "tmp");

        CustomUserDetails customUserDetails = new CustomUserDetails(customUserData);

        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);

    }


    /**
     * refresh 토큰 추출 메소드
     *
     * @param cookies
     * @return
     * @author 이광석
     * @since 2025-03-28
     */
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

