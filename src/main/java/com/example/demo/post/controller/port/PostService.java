package com.example.demo.post.controller.port;

import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.post.domain.PostUpdate;

/**
 * packageName : com.example.demo.post.controller.port
 * fileName    : PostService
 * author      : ckr
 * date        : 24. 9. 23.
 * description :
 */
public interface PostService {

    Post getById(long id);

    Post create(PostCreate postCreate);

    Post update(long id, PostUpdate postUpdate);
}
