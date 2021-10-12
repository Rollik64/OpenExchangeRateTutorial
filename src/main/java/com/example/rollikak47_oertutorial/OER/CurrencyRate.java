package com.example.rollikak47_oertutorial.OER;

import java.math.BigDecimal;

import com.example.rollikak47_oertutorial.OER.Exception.UnavailableExchangeRateException;

public class CurrencyRate {
    public static void main(String[] args) throws UnavailableExchangeRateException {
        OpenExchangeRates oer = new OpenExchangeRates("6b805125a85e455492a949a2f3c161f7");
        BigDecimal CurrencyValue = oer.currency("RUB");
        System.out.println(CurrencyValue);
    }
}