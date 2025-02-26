package recipe.util;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import recipe.entity.User;

@Slf4j
@Component
public class JWTUtil {

    private SecretKey hmacKey;
    private Long expirationTime;

    // 생성자에서 Secret Key 설정
    public JWTUtil(@Value("${spring.jwt.secret}") String secret , @Value("${spring.jwt.expiration}") String exptime) {
//        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
//                Jwts.SIG.HS256.key().build().getAlgorithm());
        
        this.hmacKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.expirationTime = Long.parseLong(exptime);
        
    }

//    // JWT에서 username 가져오기
//    public String getUsername(String token) {
//        return Jwts.parser().verifyWith(secretKey).build()
//                .parseSignedClaims(token).getPayload()
//                .get("username", String.class);
//    }
//
//    // JWT에서 role 가져오기
//    public String getRole(String token) {
//        return Jwts.parser().verifyWith(secretKey).build()
//                .parseSignedClaims(token).getPayload()
//                .get("role", String.class);
//    }
//
//    // JWT 만료 여부 확인
//    public Boolean isExpired(String token) {
//        return Jwts.parser().verifyWith(secretKey).build()
//                .parseSignedClaims(token).getPayload()
//                .getExpiration().before(new Date());
//    }

//    // JWT 생성 (username, role 포함)
//    public String createJwt(String username, String role, Long expiredMs) {
//        return Jwts.builder()
//                .claim("username", username)
//                .claim("role", role)
//                .issuedAt(new Date(System.currentTimeMillis()))
//                .expiration(new Date(System.currentTimeMillis() + expiredMs))
//                .signWith(secretKey)
//                .compact();
//    }
    
    public String generateToken(User user) {
    	Date now = new Date();
    	
    	String jwtToken = Jwts.builder()
    						.claim("username", user.getUsername())
    						.claim("email", user.getEmail())
    						.claim("role", user.getRole())
    						.subject(user.getUsername())
    						.id(String.valueOf(user.getUserId()))
    						.issuedAt(now)
    						.expiration(new Date(now.getTime()+ this.expirationTime))
    						.signWith(this.hmacKey, Jwts.SIG.HS256)
    						.compact();
    	log.debug(jwtToken);
    	
    	return jwtToken;
    	
    }
    
    private Claims getAllClaimsFromToken(String token) {
        Jws<Claims> jwt = Jwts.parser()
            .verifyWith(this.hmacKey)
            .build()
            .parseSignedClaims(token);
        return jwt.getPayload();
    }
    
    private Date getExpirationDateFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return claims.getExpiration();
    }
    
    private boolean isTokenExpired(String token) {
        Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }    
    
    public String getSubjectFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return claims.getSubject();
    }
    
    public boolean validateToken(String token, User user) {
        // 토큰 유효기간 체크
        if (isTokenExpired(token)) {
            return false;
        }
        
        // 토큰 내용을 검증
        String subject = getSubjectFromToken(token);
        String username = user.getUsername();
        
        return subject != null && username != null && subject.equals(username);        
    }
    
    
}
