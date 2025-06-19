package somt.somt.common.security.service;


import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import somt.somt.common.redis.RedisRepository;
import somt.somt.common.security.JWT.JWTUtil;
import somt.somt.common.security.dto.CustomUserDetails;

@Service
public class LoginService {
    private final JWTUtil jwtUtil;
    private final RedisRepository redisRepository;





    public LoginService(JWTUtil jwtUtil, RedisRepository redisRepository) {
        this.jwtUtil = jwtUtil;
        this.redisRepository = redisRepository;
    }

    public void generateAndAttachTokens(HttpServletResponse response, CustomUserDetails userDetails) {
        String accessToken = jwtUtil.createJwt("access", userDetails.getUsername(), userDetails.getMemberId(), userDetails.getNickname(), userDetails.getRole(), 1);

        String refreshToken = jwtUtil.createJwt("refresh", userDetails.getUsername(), userDetails.getMemberId(), userDetails.getNickname(), userDetails.getRole(), 2);

        redisRepository.save(refreshToken, accessToken, 86400000L);

        // refresh 토큰은 쿠키로
        String refreshCookieString = ResponseCookie
                .from("refresh", refreshToken)
                .path("/")
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .build()
                .toString();

        response.addHeader("Set-Cookie", refreshCookieString);
        // access 토큰은 헤더로
        response.setHeader("access", accessToken);
    }
}
