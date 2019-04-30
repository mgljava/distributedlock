package com.github.mgljava.distributedlock.config;

import com.github.mgljava.distributedlock.define.DistributedLock;
import com.github.mgljava.distributedlock.service.RedisDistributedLock;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class DistributedLockAutoConfiguration {

  @Bean
  @ConditionalOnBean(StringRedisTemplate.class)
  public DistributedLock redisDistributedLock(StringRedisTemplate redisTemplate) {
    return new RedisDistributedLock(redisTemplate);
  }
}
