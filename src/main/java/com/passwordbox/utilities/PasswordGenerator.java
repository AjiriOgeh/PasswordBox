package com.passwordbox.utilities;

import java.security.SecureRandom;

public final class PasswordGenerator {
    private static String passwordCharacters = "abcdefghijklmnopqrstuvwxyz0123456789~`!@#$%^&*()-=_+[{]};:',<.>/?ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static SecureRandom secureRandom = new SecureRandom();

    public static String generatePassword(){
        String password = "";
        for (int count = 0; count < 16; count++){
            int randomIndex = secureRandom.nextInt(passwordCharacters.length());
            password += String.valueOf(passwordCharacters.charAt(randomIndex));
        }
        return password;
    }


    public static void main(String[] args) {
        System.out.println(generatePassword());
    }
}
