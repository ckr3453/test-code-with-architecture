package com.example.demo.user.controller.port;

/**
 * packageName : com.example.demo.user.controller.port
 * fileName    : UserService
 * author      : ckr
 * date        : 24. 9. 23.
 * description :
 */
public interface AuthenticationService {

    void login(long id);

    void verifyEmail(long id, String certificationCode);
}
