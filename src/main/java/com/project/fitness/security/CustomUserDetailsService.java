package com.project.fitness.security;

import com.project.fitness.model.User;
import com.project.fitness.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String idString) throws UsernameNotFoundException {
        User user;
        try {
            UUID id = UUID.fromString(idString);
            user = userRepository.findById(id).orElse(null);
        } catch (IllegalArgumentException e) {
            // Fallback to email if it's not a UUID (for initial transition or other uses)
            user = userRepository.findByEmail(idString);
        }

        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + idString);
        }

        List<SimpleGrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities);
    }
}
