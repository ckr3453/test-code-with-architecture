package com.example.demo.user.controller.port;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserUpdate;

/**
 * packageName : com.example.demo.user.controller.port
 * fileName    : UserService
 * author      : ckr
 * date        : 24. 9. 23.
 * description :
 */
public interface UserService {

    User create(UserCreate userCreate);

    User update(long id, UserUpdate userUpdate);

    User getByEmail(String email);

    User getById(long id);

    void login(long id);

    void verifyEmail(long id, String certificationCode);
}
