package com.example.MiniProject.Utils;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public final class CgpaHelper {

    private CgpaHelper() {}

    public static double calculateDiscount(double courseFee, Float cgpa) {

        if (cgpa == null) {
            return round(courseFee);
        }

        double finalAmount;

        if (cgpa >= 9.0) {
            finalAmount = courseFee * 0.7;
        }
        else if (cgpa >= 8.0) {
            finalAmount = courseFee * 0.8;
        }
        else if (cgpa >= 7.0) {
            finalAmount = courseFee * 0.9;
        }
        else {
            finalAmount = courseFee;
        }

        return round(finalAmount);
    }

    public static double calculateRefund(
            double courseFee,
            Float cgpa,
            LocalDate deadline) {

        double paidFee = calculateDiscount(courseFee, cgpa);

        LocalDate today = LocalDate.now();

        if (today.isBefore(deadline) || today.isEqual(deadline)) {
            return round(paidFee);
        }

        long daysAfterDeadline =
                ChronoUnit.DAYS.between(deadline, today);

        if (daysAfterDeadline >= 1 && daysAfterDeadline <= 30) {
            return round(paidFee * 0.5);
        }

        if (daysAfterDeadline > 30) {
            throw new IllegalStateException(
                    "Unenrollment not allowed after 30 days from deadline"
            );
        }

        return 0;
    }


    private static double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}