package com.example.MiniProject.Utils;

import com.example.MiniProject.Entity.Currency;

public final class CurrencyHelper {

    private CurrencyHelper() {}

    public static double convertToInr(double amount, String currencyCode, Currency currency) {

        double convertedAmount;

        if ("INR".equalsIgnoreCase(currencyCode)) {
            convertedAmount = amount;
        } else {
            convertedAmount = amount * currency.getValueInr();
        }

        return roundToTwoDecimals(convertedAmount);
    }

    public static double roundToTwoDecimals(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
