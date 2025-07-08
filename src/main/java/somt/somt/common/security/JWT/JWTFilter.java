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

        List<String> noCertifiedUrls = new ArrayList<>();
        noCertifiedUrls.add("/api/member/login");
        noCertifiedUrls.add("/api/member/register");
        noCertifiedUrls.add("/api/member/logout");


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
        System.out.println("5");

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

