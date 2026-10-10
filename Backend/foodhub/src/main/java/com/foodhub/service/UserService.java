package com.foodhub.service;

import com.foodhub.dto.CreateUserRequest;
import com.foodhub.dto.UserResponse;
import com.foodhub.entity.User;
import com.foodhub.entity.UserRole;
import com.foodhub.entity.UserStatus;
import com.foodhub.exception.DuplicateEmailException;
import com.foodhub.repository.UserRepository;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserResponse createUser(CreateUserRequest request) {

        String normalizedEmail =
                request.getEmail().trim().toLowerCase(Locale.ROOT);

        if (userRepository.existsByEmailIgnoreCase(normalizedEmail)) {
            throw new DuplicateEmailException(
                    "An account with this email already exists"
            );
        }

        User user = new User();
        user.setName(request.getName().trim());
        user.setEmail(normalizedEmail);
        user.setPassword(request.getPassword()); // Replace with BCrypt before real use
        user.setPhone(
                request.getPhone() == null
                        ? null
                        : request.getPhone().trim()
        );

        // Never trust role or status supplied by a public client.
        user.setRole(UserRole.CUSTOMER);
        user.setStatus(UserStatus.ACTIVE);

        try {
            User savedUser = userRepository.saveAndFlush(user);

            return new UserResponse(
                    savedUser.getId(),
                    savedUser.getName(),
                    savedUser.getEmail(),
                    savedUser.getPhone(),
                    savedUser.getRole(),
                    savedUser.getStatus()
            );
        } catch (DataIntegrityViolationException exception) {
            // The database unique constraint also protects against
            // concurrent requests attempting to register the same email.
            throw new DuplicateEmailException(
                    "An account with this email already exists"
            );
        }
    }
}