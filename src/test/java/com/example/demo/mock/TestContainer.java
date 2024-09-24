package com.example.demo.mock;

import com.example.demo.common.service.port.ClockHolder;
import com.example.demo.common.service.port.UuidHolder;
import com.example.demo.post.controller.PostController;
import com.example.demo.post.controller.PostCreateController;
import com.example.demo.post.controller.port.PostService;
import com.example.demo.post.service.PostServiceImpl;
import com.example.demo.post.service.port.PostRepository;
import com.example.demo.user.controller.UserController;
import com.example.demo.user.controller.UserCreateController;
import com.example.demo.user.controller.port.AuthenticationService;
import com.example.demo.user.controller.port.UserCreateService;
import com.example.demo.user.controller.port.UserReadService;
import com.example.demo.user.controller.port.UserUpdateService;
import com.example.demo.user.service.CertificationService;
import com.example.demo.user.service.UserServiceImpl;
import com.example.demo.user.service.port.MailSender;
import com.example.demo.user.service.port.UserRepository;
import lombok.Builder;

/**
 * packageName : com.example.demo.mock
 * fileName    : TestContainer
 * author      : ckr
 * date        : 24. 9. 23.
 * description :
 */
public class TestContainer {

    public final MailSender mailSender;
    public final PostService postService;
    public final UserRepository userRepository;
    public final PostRepository postRepository;
    public final UserReadService userReadService;
    public final UserCreateService userCreateService;
    public final UserUpdateService userUpdateService;
    public final AuthenticationService authenticationService;
    public final CertificationService certificationService;
    public final UserController userController;
    public final UserCreateController userCreateController;
    public final PostCreateController postCreateController;
    public final PostController postController;

    @Builder
    public TestContainer(ClockHolder clockHolder, UuidHolder uuidHolder) {
        this.mailSender = new FakeMailSender();
        this.userRepository = new FakeUserRepository();
        this.postRepository = new FakePostRepository();
        this.postService = PostServiceImpl.builder()
            .postRepository(postRepository)
            .userRepository(userRepository)
            .clockHolder(clockHolder)
            .build();
        this.certificationService = new CertificationService(this.mailSender);
        UserServiceImpl userServiceImpl = UserServiceImpl.builder()
            .uuidHolder(uuidHolder)
            .clockHolder(clockHolder)
            .userRepository(this.userRepository)
            .certificationService(this.certificationService)
            .build();

        this.userReadService = userServiceImpl;
        this.userCreateService = userServiceImpl;
        this.userUpdateService = userServiceImpl;
        this.authenticationService = userServiceImpl;
        this.userController = UserController.builder()
            .userReadService(this.userReadService)
            .userCreateService(this.userCreateService)
            .authenticationService(this.authenticationService)
            .userUpdateService(this.userUpdateService)
            .build();
        this.userCreateController = UserCreateController.builder()
            .userCreateService(this.userCreateService)
            .build();
        this.postController = PostController.builder()
            .postService(this.postService)
            .build();
        this.postCreateController = PostCreateController.builder()
            .postService(this.postService)
            .build();
    }
}
