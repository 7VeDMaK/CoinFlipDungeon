    package com.coinflip.dungeon.Security.JWT;

    import com.coinflip.dungeon.Security.Services.UserDetailsImpl;
    import io.jsonwebtoken.*;
    import jakarta.servlet.http.Cookie;
    import jakarta.servlet.http.HttpServletRequest;
    import jakarta.servlet.http.HttpServletResponse;
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.stereotype.Component;

    import java.util.Date;

    @Component
    public class JwtUtils {
        private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

        @Value("${dml.app.jwtSecret}")
        private String jwtSecret;

        @Value("${dml.app.jwtExpirationMs}")
        private int jwtExpirationMs;

        public String generateJwtToken(UserDetailsImpl userPrincipal) {
            return generateTokenFromUsername(userPrincipal.getUsername());
        }

        public String generateTokenFromUsername(String username) {
            return Jwts.builder().setSubject(username).setIssuedAt(new Date())
                    .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                    .signWith(SignatureAlgorithm.HS512, jwtSecret)
                    .compact();
        }

        public String getUserNameFromJwtToken(String token) {
            return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
        }

        public String  getJwtTokenFromRequest(HttpServletRequest request){
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("JWT_TOKEN")) {
                        return cookie.getValue();
                    }
                }
            }
            return null;
        }

        public boolean validateJwtToken(String authToken) {
            try {
                Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
                return true;
            } catch (SignatureException e) {
                logger.error("Invalid JWT signature: {}", e.getMessage());
            } catch (MalformedJwtException e) {
                logger.error("Invalid JWT token: {}", e.getMessage());
            } catch (ExpiredJwtException e) {
                logger.error("JWT token is expired: {}", e.getMessage());
            } catch (UnsupportedJwtException e) {
                logger.error("JWT token is unsupported: {}", e.getMessage());
            } catch (IllegalArgumentException e) {
                logger.error("JWT claims string is empty: {}", e.getMessage());
            }

            return false;
        }

        public void addJwtTokenToCookie(HttpServletResponse response, String token) {
            Cookie cookie = new Cookie("JWT_TOKEN", token);
            cookie.setPath("/");
            cookie.setHttpOnly(true); // Запретить доступ JavaScript к кукам
            cookie.setMaxAge(jwtExpirationMs / 1000); // Установите срок действия куки
            response.addCookie(cookie);
        }
    }
