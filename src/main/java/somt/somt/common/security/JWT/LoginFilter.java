package somt.somt.common.security.JWT;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import somt.somt.common.exception.CustomException;
import somt.somt.common.exception.ErrorCode;
import somt.somt.common.exception.ErrorResponse;
import somt.somt.common.redis.RedisRepository;
import somt.somt.common.security.dto.CustomUserDetails;
import somt.somt.domain.member.service.MemberHistoryService;

import java.io.IOException;
import java.rmi.server.ServerCloneException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    private final RedisRepository redisRepository;
    private final ObjectMapper objectMapper;

    private final MemberHistoryService memberHistoryService;




    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        System.out.println("LoginFilter1");

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> requestMap = this.objectMapper.readValue(request.getInputStream(), Map.class);

            String username = requestMap.get("userName");
            String password = requestMap.get("password");

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);

            return authenticationManager.authenticate(authToken);
        } catch (IOException e) {
            throw new RuntimeException("회원 정보를 다시 확인해주세요");
        }
    }




    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authentication) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        String username = customUserDetails.getUsername();
        Long memberId = customUserDetails.getMemberId();
        String nickname = customUserDetails.getNickname();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        String accessToken = jwtUtil.createJwt("access", username, memberId, nickname, role, 1);
        String refreshToken = jwtUtil.createJwt("refresh", username, memberId, nickname, role, 2);

        redisRepository.save(refreshToken, accessToken, 86400000L);

        String refreshCookieString = ResponseCookie
                .from("refresh", refreshToken)
                .path("/")
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .build()
                .toString();

        response.addHeader("Set-Cookie", refreshCookieString);
        response.addHeader("access", accessToken);
        response.setStatus(HttpStatus.OK.value());

        memberHistoryService.createHistory("LOGIN",nickname+"님이 로그인 하였습니다 ",memberId);

    }


    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException
    {
        CustomException ce = (CustomException) failed.getCause();
        ErrorCode errorCode = ce.getErrorCode();
        ErrorResponse errorResponse = new ErrorResponse(errorCode);

        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getWriter(),errorResponse);

    }
}