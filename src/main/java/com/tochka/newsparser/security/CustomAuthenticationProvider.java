package com.tochka.newsparser.security;

import com.tochka.newsparser.domain.User;
import com.tochka.newsparser.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private UserRepository userRepository;

    public CustomAuthenticationProvider(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String name = authentication.getName().toLowerCase();
        String password = authentication.getCredentials().toString();

        //search in DB
        Optional<User> dbuser = userRepository.findOneWithAuthoritiesByLogin(name);
        if (dbuser.isPresent()) {
            User user = dbuser.get();
            if (!passwordEncoder.matches(password, user.getPassword())) {
                return null;
            }

            List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
                    .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                    .collect(Collectors.toList());

            return new UsernamePasswordAuthenticationToken(user.getLogin(), user.getPassword(), grantedAuthorities);
        }

        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
