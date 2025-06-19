package somt.somt.common.security.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import somt.somt.common.redis.RedisRepository;
import somt.somt.common.security.JWT.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;


public class ReissueController {

    final private JWTUtil jwtUtil;
    final private RedisRepository redisRepository;


    public ReissueController(JWTUtil jwtUtil , RedisRepository redisRepository){
        this.jwtUtil = jwtUtil;
        this.redisRepository = redisRepository;
    }


    /**
     * accessToken이 만료되었을경우 refresh 토큰을 이용하여  새로운 accessToken을 재발급
     * 새로운 accessToken을 redis에 저장
     * @param request
     * @param response
     * @return ResponseEntity<?>
     * @author 이광석
     * @since 2025-03-27
     */
    @PostMapping("/api/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response){
        String refresh = null;
        Cookie[] cookies = request.getCookies();

        for(Cookie cookie : cookies){
            if(cookie.getName().equals("refresh")){
                refresh = cookie.getValue();
            }
        }

        if(refresh ==null){
            return ResponseEntity.badRequest().body("refresh token null");
        }

        try{
            jwtUtil.isExpired(refresh);
        }catch (ExpiredJwtException e){
            return ResponseEntity.badRequest().body("token expired");
        }

        if(!jwtUtil.getCategory(refresh).equals("refresh")){
            return ResponseEntity.badRequest().body("invalid refresh token");
        }

        String username = jwtUtil.getUsername(refresh);
        Long memberId = jwtUtil.getMemberId(refresh);
        String role = jwtUtil.getRole(refresh);
        String nickname = jwtUtil.getNickname(refresh);

        String newAccessToken = jwtUtil.createJwt("access",username,memberId,nickname,role,1);

        redisRepository.modify(refresh,newAccessToken);

        response.setHeader("access",newAccessToken);

        return new ResponseEntity<>(HttpStatus.OK);

    }
}
