package com.micnusz.pps.Exchange.Service;

import com.micnusz.pps.Exchange.Model.ExchangeRate;
import com.micnusz.pps.Exchange.Provider.ExchangeRateProvider;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExchangeRateService {

    private final ExchangeRateProvider exchangeRateProvider;
    private final ExchangeRateCacheService cacheService;


    @Retry(name = "exchangeRateRetry")
    @CircuitBreaker(name = "exchangeRateCB", fallbackMethod = "fallbackRate")
    public ExchangeRate getRate(String base, String target) {

        ExchangeRate rate = exchangeRateProvider.getRate(base, target);

        cacheService.save(rate);

        return rate;
    }

    // fallback (Redis → last known value)
    public ExchangeRate fallbackRate(String base, String target, Throwable ex) {

        return cacheService.get(base, target)
                .map(r -> new ExchangeRate(base, target, r, System.currentTimeMillis()))
                .orElseGet(() -> new ExchangeRate(base, target, 1.0, System.currentTimeMillis()));
    }
}