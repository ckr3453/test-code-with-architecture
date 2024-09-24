package com.example.demo.user.controller.port;

/**
 * packageName : com.example.demo.user.controller.port
 * fileName    : CertificationService
 * author      : ckr
 * date        : 24. 9. 23.
 * description :
 */
public interface CertificationService {

    void send(String email, long id, String certificationCode);
}
