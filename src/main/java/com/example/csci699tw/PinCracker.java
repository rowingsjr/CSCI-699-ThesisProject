package com.example.csci699tw;

import java.util.List;
import java.util.Arrays;

public class PinCracker
{
    public static boolean correctPin = false;
    public static double duration = 0.0;

    public static String crackPin(String targetPin) {
        long startTime = System.currentTimeMillis();

        List<String> commonPins = Arrays.asList(
                "12345678", "11111111", "00000000", "12121212", "77777777",
                "12341234", "87654321", "12345679", "99999999", "22222222"
        );

        for (String pin : commonPins) {
            if (pin.equals(targetPin)) {
                duration = (System.currentTimeMillis() - startTime) / 1000.0;
                correctPin = true;
                return String.format("PIN cracked with brute force algorithm in %.2f seconds and this was the pin: %s",
                        duration, pin);

            }
        }

        for (int i = 0; i < 100000000; i++) {
            String pin = String.format("%08d", i);
            if (commonPins.contains(pin)) {
                continue;
            }
            if (pin.equals(targetPin)) {
                duration = (System.currentTimeMillis() - startTime) / 1000.0;
                correctPin = true;
                return String.format("PIN cracked with brute force algorithm in %.2f seconds and this was the pin: %s",
                        duration, pin);
            }
        }

        duration = (System.currentTimeMillis() - startTime) / 1000.0;
        correctPin = false;
        return String.format("PIN not cracked. Total time: %.2f seconds.", duration);
    }
}
