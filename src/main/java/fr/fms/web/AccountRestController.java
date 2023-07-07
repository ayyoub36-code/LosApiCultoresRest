package fr.fms.web;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.fms.entities.AppRole;
import fr.fms.entities.AppUser;
import fr.fms.forms.UserRoleForm;
import fr.fms.security.SecurityConstants;
import fr.fms.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("*")
public class AccountRestController {

    @Autowired
    AccountService accountService;

    @GetMapping("/users")
    @PostAuthorize("hasAuthority('USER')")
    ResponseEntity<List<AppUser>> getUsers() {
        return accountService.listUsers();
    }

    @PostMapping("/users")
    @PostAuthorize("hasAuthority('ADMIN')")
    AppUser create(@RequestBody AppUser user) {
        return accountService.saveUser(user);
    }

    @PostMapping("/role")
    @PostAuthorize("hasAuthority('ADMIN')")
    AppRole createRole(@RequestBody AppRole role) {
        return accountService.saveRole(role);
    }

    @PostMapping("/roleUser")
    @PostAuthorize("hasAuthority('ADMIN')")
    void postRoleUser(@RequestBody UserRoleForm userRole) {
        accountService.addRoleToUser(userRole.getUsername(), userRole.getUsername());
    }


    /*
     * @author : Mehdioui ayyoub
     * @param : request
     * utiliser le refreshToken pour générer un nouveau token
     * */
    @GetMapping(path = "/refreshToken")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authToken = request.getHeader("Authorization");
        if (authToken != null && authToken.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            try {
                String jwtRefreshToken = authToken.substring(SecurityConstants.TOKEN_PREFIX.length());
                JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(SecurityConstants.SECRET)).build();
                DecodedJWT decodedJWT = jwtVerifier.verify(jwtRefreshToken);
                String username = decodedJWT.getSubject();
                AppUser springUser = accountService.findUserByUsername(username);

                String jwtAccessToken = JWT.create()
                        .withSubject(springUser.getUsername()).withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", springUser.getRoles().stream().map(r -> r.getRolename()).collect(Collectors.toList()))
                        .sign(Algorithm.HMAC256(SecurityConstants.SECRET));

                Map<String, String> allTokens = new HashMap();
                allTokens.put("access-token", jwtAccessToken);
                allTokens.put("refresh-token", jwtRefreshToken);
                response.setContentType("application/json");

                new ObjectMapper().writeValue(response.getOutputStream(), allTokens);
            } catch (Exception e) {
                response.setHeader(SecurityConstants.ERROR_MSG, e.getMessage());
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
            }

        } else {
            throw new RuntimeException("Refresh token required");
        }
    }


}
