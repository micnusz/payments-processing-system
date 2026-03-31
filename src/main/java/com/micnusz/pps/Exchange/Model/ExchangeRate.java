package com.micnusz.pps.Exchange.Model;

public record ExchangeRate(
        String base,
        String target,
        double rate,
        long timestamp
) {}