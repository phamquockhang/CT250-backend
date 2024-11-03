package com.dvk.ct250backend.infrastructure.service;

import com.dvk.ct250backend.domain.common.service.LockService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LockServiceImpl implements LockService {

    RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean acquireLock(String key, long timeout, TimeUnit unit) {
        return Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(key, "locked", timeout, unit));
    }

    @Override
    public void releaseLock(String key) {
        redisTemplate.delete(key);
    }
}
