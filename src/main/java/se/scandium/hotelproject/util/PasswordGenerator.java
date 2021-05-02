package se.scandium.hotelproject.util;

import java.security.SecureRandom;

public class PasswordGenerator {

    private static final String CHAR_LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final int PASSWORD_LENGTH = 20;
    private static final SecureRandom random = new SecureRandom();

    public static String generate() {
        StringBuilder result = new StringBuilder(PASSWORD_LENGTH);
        for (int i = 0; i < 6; i++) {
            // produce a random order
            int index = random.nextInt(PasswordGenerator.CHAR_LOWERCASE.length());
            result.append(PasswordGenerator.CHAR_LOWERCASE.charAt(index));
        }
        return result.toString();
    }

        /*public static void main(String[] args) {
        System.out.println(generate());
    }*/


}