package com.ecoomerce.autorisation.config.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import fr.grdf.softwarefactory.abj.configuration.ApplicationProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JWTAuthorizationFilter  extends OncePerRequestFilter {

    private final ApplicationProperties props;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        if(request.getRequestURI().equals("/login")) {
            filterChain.doFilter(request, response);
            return;
        }
        else {
            String jwtToken = request.getHeader(props.getAuthentication().getHeaders().getAuthorization());
            if(jwtToken == null || !jwtToken.startsWith(props.getAuthentication().getHeaders().getAuthorizationPrefix())){
                log.warn("token invalid !");
                filterChain.doFilter(request, response);
                return;
            }
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(props.getAuthentication().getHeaders().getAuthorizationSecret()))
                .build();

            DecodedJWT decodedJWT = verifier.verify(jwtToken.substring(props.getAuthentication().getHeaders()
                .getAuthorizationPrefix().concat(" ").length()));

            String gid = decodedJWT.getSubject();
            List<String> roles = decodedJWT.getClaims().get("roles").asList(String.class);
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            roles.forEach(rn -> {
                authorities.add(new SimpleGrantedAuthority(rn));
            });

            // créer l'objet authentication
            var auth = new AppAuthentication(gid, null);
            auth.setAuthorities(authorities);

            log.debug("Information OKTA, Gaia {}", auth.getCredentials());

            UsernamePasswordAuthenticationToken user =
                new UsernamePasswordAuthenticationToken(gid, null, authorities);

            SecurityContextHolder.getContext().setAuthentication(user);
            filterChain.doFilter(request, response);
        }
    }
}
