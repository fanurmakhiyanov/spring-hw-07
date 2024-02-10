package ru.gb.springdemo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ru.gb.springdemo.repository.UserRepository;
import ru.gb.springdemo.model.security.Users;

import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService{
    final UserRepository userRepository;

    public Optional<Users> findUserByName(String username) {
        return userRepository.findUserByLogin(username);
    }
    @Override
    public UserDetails loadUserByUsername(String username) {
        Users user = findUserByName(username).orElseThrow(() -> new UsernameNotFoundException(username));
        System.out.println(user.getRoleList().toString());
        return new org.springframework.security.core.userdetails.User(
                user.getLogin(),
                user.getPassword(),
                user.getRoleList().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                        .collect(Collectors.toList())
        );
    }
}