package fr.fms.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class JwtAuthorizationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
       // response.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
        if (request.getServletPath().equals("/refreshToken")) {
            filterChain.doFilter(request, response);
        } else {
            String token = request.getHeader(SecurityConstants.HEADER_STRING);

            if (token != null && token.startsWith(SecurityConstants.TOKEN_PREFIX)) {
                try {
                    String jwtToken = token.substring(SecurityConstants.TOKEN_PREFIX.length());
                    JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(SecurityConstants.SECRET)).build();
                    DecodedJWT decodedJWT = jwtVerifier.verify(jwtToken);

                    String username = decodedJWT.getSubject();
                    String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
                    Collection<GrantedAuthority> authorities = new ArrayList<>();
                    for (String role : roles) authorities.add(new SimpleGrantedAuthority(role));

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authToken);

                } catch (Exception e) {
                    response.setHeader(SecurityConstants.ERROR_MSG, e.getMessage());
                    response.sendError(HttpServletResponse.SC_FORBIDDEN);
                }

            } else {
                filterChain.doFilter(request, response);
            }
        }

    }
}
