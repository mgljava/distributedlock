package com.github.mgljava.distributedlock.service;

import com.github.mgljava.distributedlock.define.DistributedLock;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisDistributedLock implements DistributedLock {

  private StringRedisTemplate redisTemplate;

  public RedisDistributedLock(StringRedisTemplate redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  @Override
  public boolean acquire(String key, String value, long timeout) {
    return this.acquire(key, value, timeout, TimeUnit.MINUTES);
  }

  /**
   * set key. if set success and return true else false.
   *
   * @param key redis key.
   * @param value redis value.
   * @param timeout timeout.
   * @param unit unit.
   * @return true or false.
   */
  @Override
  public boolean acquire(String key, String value, long timeout, TimeUnit unit) {
    return Optional.ofNullable(redisTemplate.opsForValue()
        .setIfAbsent(key, value, timeout, unit)).orElse(false);
  }

  /**
   * delete redis key
   *
   * @param key redis key.
   */
  @Override
  public void release(String key) {
    redisTemplate.delete(key);
  }
}
