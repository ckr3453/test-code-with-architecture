package com.example.demo.user.controller.port;

import com.example.demo.user.domain.User;

/**
 * packageName : com.example.demo.user.controller.port
 * fileName    : UserService
 * author      : ckr
 * date        : 24. 9. 23.
 * description :
 */
public interface UserReadService {

    User getByEmail(String email);

    User getById(long id);
}
