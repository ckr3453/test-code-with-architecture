package com.example.demo.user.service.port;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;

import java.util.Optional;

/**
 * packageName : com.example.demo.user.infrastructure
 * fileName    : UserRepository
 * author      : ckr
 * date        : 24. 9. 18.
 * description :
 */
public interface UserRepository {

    Optional<User> findByIdAndStatus(long id, UserStatus userStatus);

    Optional<User> findByEmailAndStatus(String email, UserStatus userStatus);

    User save(User user);

    Optional<User> findById(long id);
}
