package com.example.picpay_challenge.service;

import com.example.picpay_challenge.domain.user.User;
import com.example.picpay_challenge.domain.user.dto.AuthDTO;
import com.example.picpay_challenge.infra.security.TokenData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private TokenService jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    public TokenData login(AuthDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var token = jwtTokenProvider.generateToken((User) auth.getPrincipal());
        UUID userId = ((User) auth.getPrincipal()).getId();

        return new TokenData(userId, token);
    }

}