package com.example.csci699tw;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.TimeUnit;

public class PasswordCracker {
    private static final AtomicBoolean found = new AtomicBoolean(false);

    public static void main(String[] args) {
        String targetPassword = "PaxPlus2024";
        System.out.println("Application Started");
        System.out.println(crackPassword(targetPassword));
    }

    private static String crackPassword(String targetPassword) {
        long startTime = System.currentTimeMillis();
        ExecutorService executor = Executors.newFixedThreadPool(4); // Number of threads

        // Check common passwords in parallel
        List<String> commonPasswords = Arrays.asList(
                "PaxPlus2024", "PaxPlus123", "Welcome123", "Password1!",
                "Admin1234", "PaxPlus!", "JohnPax", "PaxPlus@2024"
        );
        commonPasswords.parallelStream().forEach(password -> {
            if (!found.get() && isValidPassword(password) && password.equals(targetPassword)) {
                found.set(true);
                System.out.println(String.format("Password cracked with common password: %s in %.2f seconds",
                        password, (System.currentTimeMillis() - startTime) / 1000.0));
            }
        });

        if (!found.get()) {
            // Brute-force in parallel
            String charSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()";
            char[] chars = charSet.toCharArray();
            int length = 8; // Password length

            executor.submit(() -> {
                // Generate and check passwords
                generateAndCheckPasswords(chars, new char[length], 0, length, targetPassword, startTime);
            });
        }

        executor.shutdown();
        try {
            if (!executor.awaitTermination(1, TimeUnit.HOURS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }

        if (!found.get()) {
            return String.format("Password not cracked. Total time: %.2f seconds.",
                    (System.currentTimeMillis() - startTime) / 1000.0);
        }

        return "Password checking process completed.";
    }

    private static void generateAndCheckPasswords(char[] chars, char[] current, int position, int length, String target, long startTime) {
        if (found.get()) return; // Early exit
        if (position == length) {
            String password = new String(current);
            if (isValidPassword(password) && password.equals(target)) {
                found.set(true);
                System.out.println(String.format("Password cracked with brute force: %s in %.2f seconds",
                        password, (System.currentTimeMillis() - startTime) / 1000.0));
            }
            return;
        }

        for (int i = 0; i < chars.length; i++) {
            if (found.get()) return; // Check frequently for early exit
            current[position] = chars[i];
            generateAndCheckPasswords(chars, current, position + 1, length, target, startTime);
        }
    }

    private static boolean isValidPassword(String password) {
        long countNumeric = password.chars().filter(Character::isDigit).count();
        long countSpecial = password.chars().filter(c -> !Character.isLetterOrDigit(c)).count();
        long countUpper = password.chars().filter(Character::isUpperCase).count();
        long countLower = password.chars().filter(Character::isLowerCase).count();

        return (password.length() == 8 &&
                countNumeric >= 1 &&
                countSpecial >= 0 &&
                countUpper >= 1 &&
                countLower >= 1);
    }
}
