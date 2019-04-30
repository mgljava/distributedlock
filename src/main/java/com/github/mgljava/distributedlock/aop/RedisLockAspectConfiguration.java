package com.github.mgljava.distributedlock.aop;

import com.github.mgljava.distributedlock.config.DistributedLockAutoConfiguration;
import com.github.mgljava.distributedlock.define.DistributedLock;
import com.github.mgljava.distributedlock.service.RedisDistributedLock;
import java.lang.reflect.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;

@Slf4j
@RequiredArgsConstructor
@Aspect
@Configuration
@ConditionalOnClass(DistributedLock.class)
@AutoConfigureAfter(DistributedLockAutoConfiguration.class)
public class RedisLockAspectConfiguration {

  private final RedisDistributedLock distributedLock;

  @Pointcut(value = "@annotation(com.github.mgljava.distributedlock.aop.RedisLock)")
  private void lockPoint() {
  }

  @Around("lockPoint()")
  public Object around(ProceedingJoinPoint joinPoint) {
    Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
    RedisLock redisLock = method.getAnnotation(RedisLock.class);
    String key = redisLock.key();
    String value = redisLock.value();
    long timeout = redisLock.timeout();
    final boolean acquire = distributedLock.acquire(key, value, timeout);
    if (!acquire) {
      log.info("get lock failed : " + key);
      return null;
    }
    log.debug("get lock success : " + key);
    try {
      return joinPoint.proceed();
    } catch (Throwable throwable) {
      log.error("execute locked method throw an exception :{}", throwable.getMessage());
    } finally {
      distributedLock.release(key);
    }
    return null;
  }
}
