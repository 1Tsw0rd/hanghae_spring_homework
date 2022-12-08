package com.sparta.hanghaememo.jwt;

import com.sparta.hanghaememo.entity.MemberRoleEnum;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component //빈 등록.. 다른 애들도 쓸거라서 ㅎㅎ
@RequiredArgsConstructor
public class JwtUtil {

    //1. 토큰에 필요한 값
    public static final String AUTHORIZATION_HEADER = "Authorization";  // Header KEY 값.. 다른 걸로(ex "테스트") 바꾸니까 안되더라
    public static final String AUTHORIZATION_KEY = "auth";  // 사용자 권한 값의 KEY 값 – token안에 사용자 권한 식별용
    private static final String BEARER_PREFIX = "TIREDTT";  // Token 식별자 – 토큰 앞에 붙임
    private static final long TOKEN_TIME = 60 * 60 * 1000L;  // 토큰 만료시간(60분)

    @Value("${jwt.secret.key}") //시크릿키 값 가져옴
    private String secretKey;
    private Key key;  //토큰을 만들 때 넣어줄 킷값, 아래 init() 참곤
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;  //이게 256보다 더 쌔...

    @PostConstruct
    public void init() {  // 키 객체 값 만드는 부분
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }
    // @PostConstruct 처음 객체 생성 시 초기화하는 어노테이션,
    // 의존성 주입이 이루어진 후 초기화 수행, 근데 아래보니까 HMAC 쓰는데, 암호화할 키 값 만들 때 쓰는 듯,
    // HMAC(키 XOR 메시지 => 해시함수 => 키 XOR => 해시함수)

    //2. header 토큰을 가져오기
    public String resolveToken(HttpServletRequest request) { //HttpServletRequest 객체 안에 토큰이 헤더값으로 들어있음
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER); //토큰값 가져오기
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) { //앞에 토큰 식별자 있는지 체크 Bearer
            return bearerToken.substring(7); //디코딩한 후에는 필요없으니 Bearer 문자열은 제외
        }
        return null;
    }


    //3. 토큰 생성
    public String createToken(String memberId, MemberRoleEnum role) {
        Date date = new Date();

        //아래 부분들에 각각 값 세팅하는 부분
        return BEARER_PREFIX +  //Bearer 문자열 들어가는 부분
                Jwts.builder()  //jwt 만들기ㄱㄱ
                        .setSubject(memberId)  //유저명 넣고
                        .claim(AUTHORIZATION_KEY, role)  //claim에는 사용자권한 값 넣고
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME)) //여긴 토큰 유효기간 넣고, 1시간
                        .setIssuedAt(date)  //토큰이 언제 생성되었는지
                        .signWith(key, signatureAlgorithm)  //사용할 암호알고리즘 세팅
                        .compact();   //압축하고 암호화 ㄱㄱ
    }

    //4. 토큰 검증
    public boolean validateToken(String token) {
        try {
            //토큰값과 암호화 킷값 사용
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }


    //5. 토큰에서 사용자 정보 가져오기, getBody()를 통해 사용자 정보 가져옴
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
}
