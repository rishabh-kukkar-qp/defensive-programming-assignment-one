package com.dpp.subscriptionupgrade.utils;

import java.time.YearMonth;

public class CreditCardValidator {

    private static boolean validateExpirationDate(String expiryDate) {
        try {
            String[] parts = expiryDate.split("/");
            if (parts.length != 2) {
                return false;
            }
            int month = Integer.parseInt(parts[0]);
            int year = 2000 + Integer.parseInt(parts[1]);
            if (month < 1 || month > 12) {
                return false;
            }

            YearMonth currentYearMonth = YearMonth.now();
            YearMonth cardYearMonth = YearMonth.of(year, month);
            return !cardYearMonth.isBefore(currentYearMonth);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean validateCardNumber(String cardNumber) {
        int total = 0;
        boolean doubleDigit = false;
        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int digit = Character.getNumericValue(cardNumber.charAt(i));
            if (doubleDigit) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }
            total += digit;
            doubleDigit = !doubleDigit;
        }

        return total % 10 == 0;
    }

    private static boolean validateCVV(String cvv) {
        if (cvv == null || cvv.isEmpty()) {
            return false;
        }
        return cvv.matches("\\d{3}");
    }

    private static boolean isValidName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }

        String regex = "^[a-zA-Z]+([ '-][a-zA-Z]+)*$";
        if (!name.matches(regex)) {
            return false;
        }

        String[] words = name.trim().split("\\s+");
        return words.length >= 2;
    }


    // Master method to validate all components
    public static boolean validateCreditCardDetails(String cardNumber, String expiryDate, String cvv, String cardHolderName) {
        return validateCardNumber(cardNumber) &&
                validateExpirationDate(expiryDate) &&
                validateCVV(cvv) &&
                isValidName(cardHolderName);
    }

}
