package com.example.demo.mock;

import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.service.port.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * packageName : com.example.demo.mock
 * fileName    : FakeUserRepository
 * author      : ckr
 * date        : 24. 9. 20.
 * description :
 */
public class FakeUserRepository implements UserRepository {

    private long autoGeneratedId = 0L;
    private final List<User> storedUsers = new ArrayList<>();

    @Override
    public User getById(Long id) {
        return storedUsers.stream()
            .filter(u -> u.getId().equals(id))
            .findAny()
            .orElseThrow(() -> new ResourceNotFoundException("Users", id));
    }

    @Override
    public Optional<User> findByIdAndStatus(long id, UserStatus userStatus) {
        return storedUsers.stream()
            .filter(storedUser -> storedUser.getId().equals(id) && storedUser.getStatus().equals(userStatus))
            .findAny();
    }

    @Override
    public Optional<User> findByEmailAndStatus(String email, UserStatus userStatus) {
        return storedUsers.stream()
            .filter(storedUser -> storedUser.getEmail().equals(email) && storedUser.getStatus().equals(userStatus))
            .findAny();
    }

    @Override
    public User save(User user) {
        // jpa에서 동작하는 save를 유사하게 직접 구현
        if (user.getId() == null || user.getId() == 0L) {
            User newUser = User.builder()
                .id(autoGeneratedId++)
                .email(user.getEmail())
                .nickname(user.getNickname())
                .address(user.getAddress())
                .certificationCode(user.getCertificationCode())
                .status(user.getStatus())
                .lastLoginAt(user.getLastLoginAt())
                .build();
            storedUsers.add(newUser);
            return newUser;
        } else {
            storedUsers.removeIf(storedUser -> storedUser.getId().equals(user.getId()));
            storedUsers.add(user);
            return user;
        }
    }

    @Override
    public Optional<User> findById(long id) {
        return storedUsers.stream()
            .filter(storedUser -> storedUser.getId().equals(id))
            .findAny();
    }
}