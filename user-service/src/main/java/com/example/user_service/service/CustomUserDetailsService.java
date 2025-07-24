package com.example.user_service.service;

import com.example.user_service.jpa.User;
import com.example.user_service.jpa.UserRepository;
import com.example.user_service.vo.UserCond;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User foundUser = repository.selectByEmail(email);

        return new org.springframework.security.core.userdetails.User(
                foundUser.getEmail(), foundUser.getEncryptedPwd(), true, true, true, true, new ArrayList<>()
        );
    }
}
