package com.triple.service;

import com.triple.domain.User;
import com.triple.exception.BusinessException;
import com.triple.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.triple.exception.ExceptionCode.MEMBER_INVALID;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(MEMBER_INVALID));
    }
}
