package com.lambdatest.ecommerce.utils;

import java.security.SecureRandom;
import java.util.*;

/**
 * Lightweight test-data factory. Avoids an external Faker dependency so the
 * framework has a minimal footprint, while still producing unique data per
 * test run (important for registration flows that require unique emails).
 */

public class TestDataFactory {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL_CHARS = "!@#$%^&*";

    private TestDataFactory() {
    }

    private static String randomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(LOWER.charAt(RANDOM.nextInt(LOWER.length())));
        }
        return sb.toString();
    }

    private static String randomDigits(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(DIGITS.charAt(RANDOM.nextInt(DIGITS.length())));
        }
        return sb.toString();
    }

    public static String uniqueEmail(String prefix) {
        return prefix + "." + randomString(6) + "." + System.currentTimeMillis() + "@testmail.dev";
    }

    public static String uniqueEmail() {
        return uniqueEmail("qa");
    }

    /**
     * Generates a random password that always contains at least one
     * uppercase letter, one lowercase letter, one digit, and one special
     * character, with a minimum total length of 8.
     */

    public static String randomPassword(int length) {
        int targetLength = Math.max(length, 8);
        List<Character> password = new ArrayList<>();
        password.add(UPPER.charAt(RANDOM.nextInt(UPPER.length())));
        password.add(LOWER.charAt(RANDOM.nextInt(LOWER.length())));
        password.add(DIGITS.charAt(RANDOM.nextInt(DIGITS.length())));
        password.add(SPECIAL_CHARS.charAt(RANDOM.nextInt(SPECIAL_CHARS.length())));
        String allChars = UPPER + LOWER + DIGITS + SPECIAL_CHARS;
        for (int i = password.size(); i < targetLength; i++) {
            password.add(allChars.charAt(RANDOM.nextInt(allChars.length())));
        }
        Collections.shuffle(password);
        StringBuilder sb = new StringBuilder(length);
        for (char c : password) {
            sb.append(c);
        }
        return sb.toString();
    }

    public static String randomPassword() {
        return randomPassword(12);
    }

    public static String randomPhone() {
        return "01" + randomDigits(9);
    }

    public static String randomEmain() {
        return "not-an-email" + randomString(4);
    }

    // Immutable bundle of data for the registration form. 

    public record RegistrationData(
        String firstName,
        String lastName,
        String email,
        String telephone, 
        String password
    ){}

    public static RegistrationData generateRegistrationData(){
        String first = randomString(6);
        String last = randomString(6);
        return new RegistrationData(
            capitalize(first),
            capitalize(last),
            uniqueEmail(),
            randomPhone(),
            randomPassword()
        );
    }

    private static String capitalize(String str){
        if(str.isEmpty()){
            return str;
        }
        return Character.toUpperCase(str.charAt(0))+str.substring(1);
    }
}
