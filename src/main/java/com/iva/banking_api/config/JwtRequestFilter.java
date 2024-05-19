package com.iva.banking_api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Date;
import java.util.function.Function;

/**
 * Фильтр для обработки JWT токенов в каждом HTTP запросе.
 * Обеспечивает аутентификацию пользователя на основе переданного JWT токена.
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Value("${jwt.secret}")
    private String secret;

    private final UserDetailsService userDetailsService;

    public JwtRequestFilter(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * Основной метод фильтрации, который обрабатывает каждый входящий запрос.
     * Выполняет следующие шаги:
     * 1. Проверяет наличие заголовка Authorization.
     * 2. Извлекает и валидирует JWT токен.
     * 3. Устанавливает аутентификацию в SecurityContext, если токен валиден.
     *
     * @param request  текущий HTTP запрос
     * @param response текущий HTTP ответ
     * @param chain    цепочка фильтров
     * @throws ServletException в случае ошибки сервлета
     * @throws IOException      в случае ошибки ввода-вывода
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");
        String username = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            username = extractUsername(jwt);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            if (validateToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        chain.doFilter(request, response);
    }

    /**
     * Извлекает имя пользователя из JWT токена.
     *
     * @param token JWT токен
     * @return имя пользователя
     */
    private String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Извлекает определенное утверждение (claim) из JWT токена.
     *
     * @param token          JWT токен
     * @param claimsResolver функция для разрешения утверждения
     * @param <T>            тип утверждения
     * @return извлеченное утверждение
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Извлекает все утверждения (claims) из JWT токена.
     *
     * @param token JWT токен
     * @return все утверждения
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    /**
     * Проверяет валидность JWT токена.
     *
     * @param token       JWT токен
     * @param userDetails детали пользователя
     * @return true, если токен валиден; false в противном случае
     */
    private Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Проверяет, истек ли срок действия JWT токена.
     *
     * @param token JWT токен
     * @return true, если срок действия истек; false в противном случае
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Извлекает дату истечения срока действия JWT токена.
     *
     * @param token JWT токен
     * @return дата истечения срока действия
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
