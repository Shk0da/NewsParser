package com.tochka.newsparser.service;

import com.google.common.collect.Sets;
import com.tochka.newsparser.domain.Authority;
import com.tochka.newsparser.domain.User;
import com.tochka.newsparser.repository.AuthorityRepository;
import com.tochka.newsparser.repository.UserRepository;
import com.tochka.newsparser.security.AuthoritiesConstants;
import com.tochka.newsparser.utils.RepositoryUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;

@Service
public class SecurityService {

    private static final Logger logger = LoggerFactory.getLogger(SecurityService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public void createUser(User user) throws ConstraintViolationException {
        Authority authority = authorityRepository.findOne(AuthoritiesConstants.USER);
        user.setAuthorities(Sets.newHashSet(authority));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActivated(true);
        RepositoryUtils.save(user, userRepository);
        logger.debug(String.format("Create user %s successfully!", user.getLogin()));
    }
}
