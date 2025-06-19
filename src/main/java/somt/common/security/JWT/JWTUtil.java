package somt.common.security.JWT;


import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * jwt 유틸 틀래스
 * jwt를 생성하거나 정보를 추출
 *
 * @since 2025-03-26
 * @author 이광석
 */
@Component
public class JWTUtil {
    private SecretKey secretKey;

    @Value("${security.time.access}")
    private  Long accessExpiration;

    @Value("${security.time.refresh}")
    private   Long refreshExpiration;

    public JWTUtil(@Value("${spring.jwt.secret}")String secret) {


        secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String getUsername(String token){
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("username",String.class);

    }

    public String getNickname(String token){
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("nickname",String.class);
    }

    public String getRole(String token){
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role",String.class);
    }

    public Long getMemberId(String token){
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("memberId", Long.class);
    }

    public Boolean isExpired(String token){
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }

    public String getCategory(String token){
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("category",String.class );
    }

    /**
     * jwt 생성 메소드
     * @param username
     * @param memberId
     * @param role
     * @param type - 1 = access, 2 = refresh
     * @return String(jwt token)
     * @author 이광석
     * @since 2025-03-26
     */
    public String createJwt(String category,String username, Long memberId, String nickname,String role,int type){
        if(type ==1) {
            return Jwts.builder()
                    .claim("category", category)
                    .claim("username", username)
                    .claim("memberId", memberId)
                    .claim("nickname", nickname)
                    .claim("role", role)
                    .issuedAt(new Date(System.currentTimeMillis()))
                    .expiration(new Date(System.currentTimeMillis() + accessExpiration))
                    .signWith(secretKey)
                    .compact();

        }else {
            return Jwts.builder()
                    .claim("category", category)
                    .claim("username", username)
                    .claim("memberId", memberId)
                    .claim("nickname", nickname)
                    .claim("role", role)
                    .issuedAt(new Date(System.currentTimeMillis()))
                    .expiration(new Date(System.currentTimeMillis() + refreshExpiration))
                    .signWith(secretKey)
                    .compact();
        }
    }
}
