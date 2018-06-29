package com.xebia.vulnmanager.auth.security;

import com.xebia.vulnmanager.models.company.Person;
import com.xebia.vulnmanager.services.UserDetailsServiceImpl;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

import static com.xebia.vulnmanager.auth.security.SecurityConstants.*;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private UserDetailsServiceImpl userDetailsService;

    public JWTAuthorizationFilter(final AuthenticationManager authManager, final UserDetailsServiceImpl userDetailsService) {
        super(authManager);
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        String header = req.getHeader(HEADER_STRING);

        if (header == null) {
            chain.doFilter(req, res);
            return;
        }

        if (header.startsWith(TOKEN_PREFIX)) {
            UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else if (header.toLowerCase().startsWith(API_PREFIX.toLowerCase())) {
            // Api key handler
            SecurityContextHolder.getContext().setAuthentication(getAPIAuthentication(req));
        }


        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAPIAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);
        if (token != null) {
            // Remove prefix and spaces before and after
            String key = token.replace(API_PREFIX, "").trim();

            // Search for user with api key
            Person user = userDetailsService.loadPersonByApikey(key);

            return new UsernamePasswordAuthenticationToken(user.getUsername(), null, new ArrayList<>());
        }
        return null;
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);
        if (token != null) {
            String user = Jwts.parser()
                    .setSigningKey(SECRET.getBytes())
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                    .getBody()
                    .getSubject();

            //

            if (user != null) {
                return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
            }
            return null;
        }
        return null;
    }
}
