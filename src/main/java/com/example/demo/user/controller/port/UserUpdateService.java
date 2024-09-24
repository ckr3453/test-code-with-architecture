package com.example.demo.user.controller.port;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserUpdate;

/**
 * packageName : com.example.demo.user.controller.port
 * fileName    : UserService
 * author      : ckr
 * date        : 24. 9. 23.
 * description :
 */
public interface UserUpdateService {


    User update(long id, UserUpdate userUpdate);
}
