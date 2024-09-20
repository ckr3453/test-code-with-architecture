package com.example.demo.common.infrastructure;

import com.example.demo.common.service.port.ClockHolder;
import org.springframework.stereotype.Component;

import java.time.Clock;

/**
 * packageName : com.example.demo.common.infrastructure
 * fileName    : SystemClockHolder
 * author      : ckr
 * date        : 24. 9. 20.
 * description :
 */

@Component
public class SystemClockHolder implements ClockHolder {

    @Override
    public long currentTimeMillis() {
        return Clock.systemUTC().millis();
    }
}
