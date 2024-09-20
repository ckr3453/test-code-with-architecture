package com.example.demo.mock;

import com.example.demo.common.service.port.UuidHolder;
import lombok.RequiredArgsConstructor;

/**
 * packageName : com.example.demo.mock
 * fileName    : TestUuidHolder
 * author      : ckr
 * date        : 24. 9. 20.
 * description :
 */

@RequiredArgsConstructor
public class TestUuidHolder implements UuidHolder {

    private final String uuid;

    @Override
    public String generateUuid() {
        return uuid;
    }
}
