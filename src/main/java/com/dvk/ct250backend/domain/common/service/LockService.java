package com.dvk.ct250backend.domain.common.service;

import java.util.concurrent.TimeUnit;

public interface LockService {
    boolean acquireLock(String key, long timeout, TimeUnit unit);
    void releaseLock(String key);
}
