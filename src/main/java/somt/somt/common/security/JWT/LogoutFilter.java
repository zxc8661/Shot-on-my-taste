package somt.somt.common.security.JWT;


import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.web.filter.OncePerRequestFilter;
import somt.somt.common.exception.CustomException;
import somt.somt.common.exception.ErrorCode;
import somt.somt.common.redis.RedisRepository;
import somt.somt.domain.member.service.MemberHistoryService;

import java.io.IOException;

/**
 * 로그아웃 필터
 */
@RequiredArgsConstructor
public class LogoutFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final RedisRepository redisRepository;

    private final MemberHistoryService memberHistoryService;



    /**
     * 로그아웃 필터
     * 적절한 logout 요청인지 확인하고 redis 에서 해당 사용자의 토큰들을 지운 다음 빈 토큰을 쿠키에 담아 전달
     *
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     * @author 이광석
     * @since 25.03.31
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 로그아웃 요청이 아닌 경우 필터 통과
        if (!request.getRequestURI().equals("/api/member/logout") || !request.getMethod().equals("POST")
       ) {
            filterChain.doFilter(request, response);
            return;
        }


        // 쿠키에서 refresh 토큰 꺼내기
        String refresh = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("refresh".equals(cookie.getName())) {
                    refresh = cookie.getValue();
                    break;
                }
            }
        }

        // refresh 토큰 검증
        if (refresh == null) {
            throw new CustomException(ErrorCode.NOT_FOUND_REFRESHTOKEN);
        }

        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {

            throw new CustomException(ErrorCode.REFRESHTOKEN_IS_EXPIRED);
        }

        if (!"refresh".equals(jwtUtil.getCategory(refresh))) {

            throw new CustomException(ErrorCode.IS_NOT_REFRESHTOKEN);
        }

        // Redis에서 해당 refresh로 저장된 access 토큰 삭제
        String accessToken = redisRepository.getValue(refresh);
        if (accessToken == null) {
            throw new CustomException(ErrorCode.NOT_FOUND_ACCESSTOKEN);
        }

        redisRepository.delete(refresh);

        String refreshCookieString = ResponseCookie
                .from("refresh", null)
                .path("/")
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .maxAge(0)
                .build()
                .toString();

        response.addHeader("Set-Cookie", refreshCookieString);

        response.setStatus(HttpServletResponse.SC_OK);

        memberHistoryService.createHistory(
                "LOGOUT",
                jwtUtil.getNickname(accessToken)+"님이 로그아웃 했습니다",
                jwtUtil.getMemberId(accessToken));
    }
}
