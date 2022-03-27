package com.example.security.utils;

import com.example.security.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Component;

import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtils {
    private final JwtConfig jwtConfig;
    private volatile PrivateKey privateKey;
    private volatile PublicKey publicKey;

    public JwtUtils(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    public PrivateKey getPrivateKey() {
        if (privateKey == null) {
            synchronized (JwtUtils.class) {
                if (privateKey == null) {
                    try {
                        byte[] encodedKey = Base64.decodeBase64(jwtConfig.getPrivateKey());
                        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                        privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(encodedKey));
                    } catch (GeneralSecurityException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return privateKey;
    }

    public PublicKey getPublicKey() {
        if (publicKey == null) {
            synchronized (JwtUtils.class) {
                if (publicKey == null) {
                    try {
                        byte[] encodedKey = Base64.decodeBase64(jwtConfig.getPublicKey());
                        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                        publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
                    } catch (GeneralSecurityException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return publicKey;
    }

    private JwtBuilder getJwtBuilder(String subject, Long ttlMillis, String uuid) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        if (ttlMillis == null) {
            ttlMillis = jwtConfig.getTimeToLive();
        }
        long expMillis = nowMillis + ttlMillis;
        Date expDate = new Date(expMillis);
        return Jwts.builder()
                .setId(uuid)
                .setSubject(subject)
                .setIssuedAt(now)
                .setIssuer(jwtConfig.getIssuer())
                .signWith(getPrivateKey())
                .setExpiration(expDate);
    }

    public String createJWT(String subject, Long ttlMillis) {
        return getJwtBuilder(subject, ttlMillis, UUID.randomUUID().toString()).compact();
    }

    public String createJWT(String subject) {
        return getJwtBuilder(subject, null, UUID.randomUUID().toString()).compact();
    }

    public Claims parseJWT(String jwt) throws Exception {
        return Jwts.parserBuilder().setSigningKey(getPublicKey()).build().parseClaimsJws(jwt).getBody();
    }

}
