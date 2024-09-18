package com.example.demo.user.service.port;

/**
 * packageName : com.example.demo.user.service.port
 * fileName    : MailSender
 * author      : ckr
 * date        : 24. 9. 18.
 * description :
 */
public interface MailSender {

    void send(String email, String title, String content);
}
