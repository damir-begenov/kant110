package kz.dossier.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import kz.dossier.security.models.User;
import kz.dossier.security.repository.UserRepository;
import kz.dossier.security.services.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtils {
  private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

  @Autowired
  UserRepository userRepository;
  // @Value("Y2Fubm90cm9hbF9leGFtcGxlX2tleV9zdHJpbmc6Y2Fubm90cm9hbF9leGFtcGxlX2tleV9zdHJpbmc6Y2Fubm90cm9hbF9leGFtcGxlX2tleV9zdHJpbmc6Y2Fubm90cm9hbF9leGFtcGxlX2tleV9zdHJpbmc=")
  // private String jwtSecret;

  // @Value("89000000")
  // private int jwtExpirationMs;


  // @Value("108000000")
  // private int refreshTokenExpirationMs;
  @Value("${bezkoder.app.jwtSecret}")
  private String jwtSecret;

  @Value("${bezkoder.app.jwtExpirationMs}")
  private int jwtExpirationMs;

  @Value("${bezkoder.app.refreshTokenExpirationMs}")
  private int refreshTokenExpirationMs;

  public String generateJwtToken(Authentication authentication, Integer tokenVersion) {
    UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
    int expiration = jwtExpirationMs;
    if (userPrincipal.getUsername().equals("derzeet@gmail.com")) {
      expiration = 8600000;
    }

    return Jwts.builder()
            .setSubject(userPrincipal.getUsername())
            .claim("tokenVersion", tokenVersion) // Add token version to JWT
            .setIssuedAt(new Date())
            .setExpiration(new Date((new Date()).getTime() + expiration))
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact();
  }
  public String generateRefreshToken(Authentication authentication, Integer tokenVersion) {
    UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
    return Jwts.builder()
            .setSubject((userPrincipal.getUsername()))
            .claim("tokenVersion", tokenVersion) // Add token version to JWT
            .setIssuedAt(new Date())
            .setExpiration(new Date((new Date()).getTime() + refreshTokenExpirationMs))
            .signWith(getSigningKey(), SignatureAlgorithm.HS512)
            .compact();
  }

  private SecretKey getSigningKey() {
    // Ensure that jwtSecret is long enough for the HS512 algorithm.
    byte[] keyBytes = jwtSecret.getBytes();
    if (keyBytes.length < 64) { // HS512 requires at least 64 bytes (512 bits)
      throw new IllegalArgumentException("JWT secret key must be at least 64 bytes for HS512 algorithm");
    }
    return Keys.hmacShaKeyFor(keyBytes);
  }

  public String getUserNameFromJwtToken(String token) {
    Claims claims = Jwts.parser()
            .setSigningKey(getSigningKey())
            .build()
            .parseClaimsJws(token)
            .getBody();

    return claims.getSubject();
  }

  public boolean validateJwtToken(String authToken) {
    try {
      Claims claims = Jwts.parser().setSigningKey(getSigningKey())
              .build()
              .parseClaimsJws(authToken)
              .getBody();

      Integer tokenVersion = (Integer) claims.get("tokenVersion");
      String username = getUserNameFromJwtToken(authToken);
      User user = userRepository.findByUsernameTwo(username);// ... get from database
      Integer currentTokenVersion = user.getTokenVersion();
      if (tokenVersion == null || !tokenVersion.equals(currentTokenVersion)) {
        logger.error("Invalid token version");
        return false;
      }

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
}
