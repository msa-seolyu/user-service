package com.seolyu.userservice.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.*;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.seolyu.userservice.common.error.ErrorCode;
import com.seolyu.userservice.security.dto.UserContext;
import com.seolyu.userservice.security.exception.AuthenticationFailException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@Component
public class JwtFactory {
    public static final String JWT_TOKEN_HEADER = "Authorization";
    public static final String JWT_TOKEN_PREFIX = "Bearer ";
    private static final ZoneId DEFAULT_ZONE = ZoneId.of("Asia/Seoul");
    private static final String LOGIN_ID = "email";
    private static final Duration ACCESS_TOKEN_EXPIRY = Duration.ofMinutes(30);
    private static final Duration REFRESH_TOKEN_EXPIRY = Duration.ofDays(30);

    @Value("${jwt.secret}")
    private String secret;

    public String generateAccessToken(UserContext userContext) {
        Date accessTokenExpiry = this.addDurationToCurrentTime(ACCESS_TOKEN_EXPIRY);
        return this.generateToken(userContext, accessTokenExpiry);
    }

    public String generateRefreshToken(UserContext userContext) {
        Date accessTokenExpiry = this.addDurationToCurrentTime(REFRESH_TOKEN_EXPIRY);
        return this.generateToken(userContext, accessTokenExpiry);
    }

    private String generateToken(UserContext userContext, Date expiresAt) {
        ZonedDateTime now = ZonedDateTime.now(DEFAULT_ZONE);
        return JWT.create()
                .withIssuedAt(Date.from(now.toInstant()))
                .withExpiresAt(expiresAt)
                .withClaim(LOGIN_ID, userContext.getUsername())
                .sign(generateSign());
    }

    private Date addDurationToCurrentTime(Duration duration) {
        ZonedDateTime now = ZonedDateTime.now(DEFAULT_ZONE);
        return Date.from(now.plus(duration).toInstant());
    }

    private Algorithm generateSign() {
        return Algorithm.HMAC256(secret);
    }

    public UserContext decode(String token) {
        DecodedJWT decodedJWT = this.verifyToken(token);
        return this.extractUserContext(decodedJWT);
    }

    private DecodedJWT verifyToken(String token) {
        Algorithm algorithm = this.generateSign();
        JWTVerifier verifier = JWT.require(algorithm).build();

        try {
            return verifier.verify(token);
        } catch (AlgorithmMismatchException | SignatureVerificationException
                 | InvalidClaimException | JWTDecodeException e) {
            throw new AuthenticationFailException(ErrorCode.INVALID_TOKEN);
        } catch (TokenExpiredException e) {
            throw new AuthenticationFailException(ErrorCode.EXPIRED_TOKEN);
        }
    }

    private UserContext extractUserContext(DecodedJWT decodedJWT) {
        String email = decodedJWT.getClaim(LOGIN_ID).asString();
        return UserContext.of(email, "");
    }
}
