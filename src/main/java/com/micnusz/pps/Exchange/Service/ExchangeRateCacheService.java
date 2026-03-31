package com.micnusz.pps.Exchange.Service;

import com.micnusz.pps.Exchange.Model.ExchangeRate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
public class ExchangeRateCacheService {

    private final StringRedisTemplate redisTemplate;

    public ExchangeRateCacheService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void save(ExchangeRate rate) {
        String key = key(rate.base(), rate.target());

        redisTemplate.opsForValue().set(
                key,
                String.valueOf(rate.rate()),
                Duration.ofSeconds(30)
        );
    }

    public Optional<Double> get(String base, String target) {
        String value = redisTemplate.opsForValue().get(key(base, target));

        if (value == null) return Optional.empty();

        return Optional.of(Double.parseDouble(value));
    }

    private String key(String base, String target) {
        return "rate:" + base + "_" + target;
    }
}