package com.github.mgljava.distributedlock.define;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁接口
 */

public interface DistributedLock {

  // 获取锁
  boolean acquire(String key, String value, long timeout);

  boolean acquire(String key, String value, long timeout, TimeUnit unit);

  // 释放锁
  void release(String key);
}
