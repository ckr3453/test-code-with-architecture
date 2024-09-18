package com.example.demo.user.service;

import com.example.demo.mock.FakeMailSender;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * packageName : com.example.demo.user.service
 * fileName    : CertificationServiceTest
 * author      : ckr
 * date        : 24. 9. 18.
 * description :
 */
class CertificationServiceTest {

    @Test
    void 이메일과_컨텐츠가_제대로_생성되어_보내지는지_확인한다() {
        //given
        long userId = 1L;
        String userEmail = "david3453@naver.com";
        String userCertificationCode = "123123-123123-123-123-123123123";
        FakeMailSender fakeMailSender = new FakeMailSender();
        CertificationService certificationService = new CertificationService(fakeMailSender);

        //when
        certificationService.send(userEmail, userId, userCertificationCode);

        //then
        assertThat(fakeMailSender.email).isEqualTo(userEmail);
        assertThat(fakeMailSender.title).isEqualTo("Please certify your email address");
        assertThat(fakeMailSender.content).isEqualTo("Please click the following link to certify your email address: http://localhost:8080/api/users/" + userId + "/verify?certificationCode=" + userCertificationCode);

    }
}
