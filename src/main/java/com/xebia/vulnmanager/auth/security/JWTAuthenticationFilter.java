package com.xebia.vulnmanager.auth.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xebia.vulnmanager.models.company.PersonLogin;
import com.xebia.vulnmanager.repositories.PersonRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static com.xebia.vulnmanager.auth.security.SecurityConstants.EXPIRATION_TIME;
import static com.xebia.vulnmanager.auth.security.SecurityConstants.HEADER_STRING;
import static com.xebia.vulnmanager.auth.security.SecurityConstants.SECRET;
import static com.xebia.vulnmanager.auth.security.SecurityConstants.TOKEN_PREFIX;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;

    @Autowired
    private PersonRepository personRepository;

    public JWTAuthenticationFilter(final AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) {
        try {
            PersonLogin cred = new ObjectMapper()
                    .readValue(request.getInputStream(), PersonLogin.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            cred.getUsername(),
                            cred.getPassword(),
                            new ArrayList<>())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication auth) {
        User user = (User) auth.getPrincipal();

        //Person person = personRepository.findByUsername(user.getUsername());
        //System.out.println("Looking for user: " + user.getUsername());
        //ObjectMapper mapper = new ObjectMapper();
        String token = null;
        token = Jwts.builder()
                .setSubject(user.getUsername()) //mapper.writeValueAsString(person))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET.getBytes())
                .compact();
        response.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
        response.setHeader("Access-Control-Expose-Headers", "Authorization");
    }
}
