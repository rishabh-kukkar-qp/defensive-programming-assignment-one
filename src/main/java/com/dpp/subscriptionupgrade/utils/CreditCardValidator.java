package com.dpp.subscriptionupgrade.utils;

import com.dpp.subscriptionupgrade.exception.BadRequestException;
import com.dpp.subscriptionupgrade.model.request.UpgradeUserSubscriptionRequestModel;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public class CreditCardValidator {

    private static void validateExpirationDate(String expiryDate, List<String> errors) {
        try {
            String[] parts = expiryDate.split("/");
            if (parts.length != 2) {
                errors.add("Invalid expiry date format. Use MM/YY.");
                return;
            }
            int month = Integer.parseInt(parts[0]);
            int year = 2000 + Integer.parseInt(parts[1]);
            if (month < 1 || month > 12) {
                errors.add("Invalid expiry month. It must be between 01 and 12.");
            }

            YearMonth currentYearMonth = YearMonth.now();
            YearMonth cardYearMonth = YearMonth.of(year, month);
            if (!cardYearMonth.isAfter(currentYearMonth)) {
                errors.add("Expiry date must be in the future.");
            }
        } catch (NumberFormatException e) {
            errors.add("Invalid expiry date format. Use MM/YY.");
        }
    }

    private static void validateCardNumber(String cardNumber, List<String> errors) {
        if (cardNumber == null || cardNumber.isEmpty()) {
            errors.add("Card number cannot be empty.");
            return;
        }
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
        if (total % 10 != 0) {
            errors.add("Invalid card number. Failed Luhn check.");
        }
    }

    private static void validateCVV(String cvv, List<String> errors) {
        if (cvv == null || cvv.isEmpty()) {
            errors.add("CVV cannot be empty.");
            return;
        }
        if (!cvv.matches("\\d{3}")) {
            errors.add("CVV must be a 3-digit number.");
        }
    }

    private static void isValidName(String name, List<String> errors) {
        if (name == null || name.trim().isEmpty()) {
            errors.add("Name cannot be empty.");
            return;
        }

        String regex = "^[a-zA-Z]+([ '-][a-zA-Z]+)*$";
        if (!name.matches(regex)) {
            errors.add("Name contains invalid characters.");
        }
        String[] words = name.trim().split("\\s+");
        if (words.length < 2) {
            errors.add("Name must contain at least two words.");
        }
    }

    // Master method to validate all components
    public static void validateCreditCardDetails(UpgradeUserSubscriptionRequestModel requestModel) {
        List<String> errors = new ArrayList<>();

        isValidName(requestModel.getName(), errors);
        validateCardNumber(requestModel.getCardNumber(), errors);
        validateExpirationDate(requestModel.getExpiryDate(), errors);
        validateCVV(requestModel.getCvv(), errors);

        if (!errors.isEmpty()) {
            throw new BadRequestException(String.join("; ", errors));
        }
    }

}
