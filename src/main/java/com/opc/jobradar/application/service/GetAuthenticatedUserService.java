package com.opc.jobradar.application.service;

import com.opc.jobradar.domain.model.User;
import com.opc.jobradar.domain.port.out.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class GetAuthenticatedUserService {

    private final UserRepository userRepository;

    public GetAuthenticatedUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUser(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null.");
        }

        return userRepository.findById(userId);
    }
}