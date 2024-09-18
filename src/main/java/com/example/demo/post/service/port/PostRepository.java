package com.example.demo.post.service.port;

import com.example.demo.post.domain.Post;

import java.util.Optional;

/**
 * packageName : com.example.demo.post.infrastructure
 * fileName    : PostRepository
 * author      : ckr
 * date        : 24. 9. 18.
 * description :
 */
public interface PostRepository {
    Optional<Post> findById(long id);

    Post save(Post post);

}
