package com.example.demo.mock;

import com.example.demo.common.service.port.ClockHolder;
import lombok.RequiredArgsConstructor;

/**
 * packageName : com.example.demo.mock
 * fileName    : TestClockHolder
 * author      : ckr
 * date        : 24. 9. 20.
 * description :
 */

@RequiredArgsConstructor
public class TestClockHolder implements ClockHolder {

    private final long millis;

    @Override
    public long currentTimeMillis() {
        return millis;
    }
}
