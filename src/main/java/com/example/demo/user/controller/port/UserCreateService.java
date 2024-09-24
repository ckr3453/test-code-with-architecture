package com.example.demo.user.controller.port;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserCreate;

/**
 * packageName : com.example.demo.user.controller.port
 * fileName    : UserService
 * author      : ckr
 * date        : 24. 9. 23.
 * description :
 */
public interface UserCreateService {

    User create(UserCreate userCreate);
}
