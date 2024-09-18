package com.example.demo.mock;

import com.example.demo.user.service.port.MailSender;

/**
 * packageName : com.example.demo.mock
 * fileName    : FakeMailSender
 * author      : ckr
 * date        : 24. 9. 18.
 * description :
 */
public class FakeMailSender implements MailSender {

    public String email;
    public String title;
    public String content;

    @Override
    public void send(String email, String title, String content) {
        this.email = email;
        this.title = title;
        this.content = content;
    }
}
