package com.integrador.spring.app.Servicio;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.security.Key;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    // Clave secreta para firmar el JWT (debe estar codificada en Base64)
    private static final String SECRET_KEY = "8A25CB2D0F3E4F79B3A8B934AE86B4F1A2B3C4D5E6F7A8B9C0D1E2F3A4B5C6D7";
    private final Cache<String, String> tokenCache = CacheBuilder.newBuilder()
        .maximumSize(1000) // Máximo 1000 tokens en cache
        .expireAfterWrite(1, TimeUnit.HOURS) // Expira después de 1 hora
        .build();
        
    //Genera un token JWT para un usuario dado sin claims adicionales.
    public String getToken(UserDetails user){
        return getToken(new HashMap<>(),user);
    }

    public String getToken(Map<String, Object> extraClaims, UserDetails user) {
        return Jwts
            .builder()
            .setClaims(extraClaims)
            .setSubject(user.getUsername())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24 horas
            .signWith(getkey(), SignatureAlgorithm.HS256)
            .compact();
    }

    private Key getkey() {
        byte[] keyBytes=Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getUsernameFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String nickname=getUsernameFromToken(token);
        return (nickname.equals(userDetails.getUsername())&& !isTokenExpired(token));
    }

    private Claims getAllClaims(String token){
        return Jwts
            .parserBuilder()
            .setSigningKey(getkey())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    public <T> T getClaim(String token, Function<Claims,T>claimsResolver){
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }
    
    private Date getExpiration(String token){
        return getClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token){
        return getExpiration(token).before(new Date());
    }

    public String generateRefreshToken(UserDetails user) {
        return Jwts
            .builder()
            .setSubject(user.getUsername())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 30)) // 30 días
            .signWith(getkey(), SignatureAlgorithm.HS256)
            .compact();
    }
    // Genera token temporal solo para 2FA
    public String generateTempToken(UserDetails user) {
        return Jwts.builder()
            .setSubject(user.getUsername())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 5)) // 5 minutos
            .signWith(getkey(), SignatureAlgorithm.HS256)
            .compact();
    }

    // Verifica token temporal
    public boolean isTempTokenValid(String token) {
        try {
            return !getAllClaims(token).getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public void cacheToken(String username, String token) {
        tokenCache.put(username, token);
    }

    public Optional<String> getCachedToken(String username) {
        return Optional.ofNullable(tokenCache.getIfPresent(username));
    }

    // Añade este método a tus llamadas después de generar un token
    public String getTokenAndCache(UserDetails user) {
        String token = getToken(user);
        cacheToken(user.getUsername(), token);
        return token;
    }

}
