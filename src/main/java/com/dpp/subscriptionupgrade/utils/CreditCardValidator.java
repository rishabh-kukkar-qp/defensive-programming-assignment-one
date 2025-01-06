package com.dpp.subscriptionupgrade.utils;

import com.dpp.subscriptionupgrade.exception.BadRequestException;
import com.dpp.subscriptionupgrade.model.request.UpgradeUserSubscriptionRequestModel;

import java.time.YearMonth;

public class CreditCardValidator {

    private static void validateExpirationDate(String expiryDate) {
        try {
            String[] parts = expiryDate.split("/");
            if (parts.length != 2) {
                throw new BadRequestException("Please enter Valid expiry Date");
            }
            int month = Integer.parseInt(parts[0]);
            int year = 2000 + Integer.parseInt(parts[1]);
            if (month < 1 || month > 12) {
                throw new BadRequestException("Please enter Valid expiry Date");
            }

            YearMonth currentYearMonth = YearMonth.now();
            YearMonth cardYearMonth = YearMonth.of(year, month);
            if (!cardYearMonth.isBefore(currentYearMonth)) {
                throw new BadRequestException("Please enter Valid expiry Date");
            }
        } catch (NumberFormatException e) {
            throw new BadRequestException("Please enter Valid expiry Date");
        }
    }

    private static void validateCardNumber(String cardNumber) {
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
            throw new BadRequestException("Please enter Valid Card Number");
        }
    }

    private static void validateCVV(String cvv) {
        if (cvv == null || cvv.isEmpty()) {
            throw new BadRequestException("Please enter Valid CVV");
        }
        if (!cvv.matches("\\d{3}")) {
            throw new BadRequestException("Please enter Valid CVV");
        }
    }

    private static void isValidName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new BadRequestException("Please enter Valid Name");
        }

        String regex = "^[a-zA-Z]+([ '-][a-zA-Z]+)*$";
        if (!name.matches(regex)) {
            throw new BadRequestException("Please enter Valid Name");
        }
        String[] words = name.trim().split("\\s+");
        if (!(words.length >= 2)) {
            throw new BadRequestException("Please enter Valid Name");
        }
    }

    // Master method to validate all components
    public static void validateCreditCardDetails(UpgradeUserSubscriptionRequestModel requestModel) {
        isValidName(requestModel.getName());
        validateCardNumber(requestModel.getCardNumber());
        validateExpirationDate(requestModel.getExpiryDate());
        validateCVV(requestModel.getCvv());
    }
}
