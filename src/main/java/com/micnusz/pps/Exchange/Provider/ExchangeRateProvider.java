package com.micnusz.pps.Exchange.Provider;

import com.micnusz.pps.Exchange.Model.ExchangeRate;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class ExchangeRateProvider {

    private final Random random = new Random();

    public ExchangeRate getRate(String base, String target) {

        simulateFailure();

        double baseRate = getBaseRate(base, target);

        double fluctuation = (random.nextDouble() - 0.5) * 0.01;

        double finalRate = baseRate * (1 + fluctuation);

        return new ExchangeRate(
                base,
                target,
                finalRate,
                System.currentTimeMillis()
        );
    }

    private void simulateFailure() {
        double r = random.nextDouble();

        if (r < 0.15) {
            throw new RuntimeException("Mock provider failure");
        }

        if (r < 0.25) {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException ignored) {}
        }
    }

    private double getBaseRate(String base, String target) {
        return switch (base + "_" + target) {
            case "USD_EUR" -> 0.92;
            case "EUR_USD" -> 1.09;
            case "GBP_USD" -> 1.27;
            case "USD_GBP" -> 0.79;
            case "EUR_PLN" -> 4.35;
            case "USD_PLN" -> 4.00;
            case "GBP_PLN" -> 5.10;
            default -> 1.0;
        };
    }
}