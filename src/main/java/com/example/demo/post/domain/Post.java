package com.example.demo.post.domain;

import com.example.demo.user.domain.User;
import com.example.demo.user.infrastructure.UserEntity;
import lombok.Builder;
import lombok.Getter;

import java.time.Clock;

/**
 * packageName : com.example.demo.post.domain
 * fileName    : Post
 * author      : ckr
 * date        : 24. 9. 18.
 * description :
 */

@Getter
public class Post {
    private final Long id;
    private final String content;
    private final Long createdAt;
    private final Long modifiedAt;
    private final UserEntity writer;

    @Builder
    public Post(Long id, String content, Long createdAt, Long modifiedAt, UserEntity writer) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.writer = writer;
    }

    public static Post from(User writer, PostCreate postCreate) {
        return Post.builder()
            .content(postCreate.getContent())
            .writer(UserEntity.fromModel(writer))
            .createdAt(Clock.systemUTC().millis())
            .build();
    }

    public Post update(PostUpdate postUpdate) {
        return Post.builder()
            .id(id)
            .content(postUpdate.getContent())
            .createdAt(createdAt)
            .modifiedAt(Clock.systemUTC().millis())
            .writer(writer)
            .build();
    }
}
