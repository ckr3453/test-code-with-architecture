package com.example.demo.common.infrastructure;

import com.example.demo.common.service.port.UuidHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * packageName : com.example.demo.common.infrastructure
 * fileName    : SystemUuidHolder
 * author      : ckr
 * date        : 24. 9. 20.
 * description :
 */

@Component
public class SystemUuidHolder implements UuidHolder {

    @Override
    public String generateUuid() {
        return UUID.randomUUID().toString();
    }
}
