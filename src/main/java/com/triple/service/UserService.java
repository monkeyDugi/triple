package com.triple.service;

import com.triple.domain.Place;
import com.triple.domain.User;
import com.triple.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findById(UUID id) {
        return userRepository.findById(id)
                .orElse(User.emptyUser(id));
    }
}
