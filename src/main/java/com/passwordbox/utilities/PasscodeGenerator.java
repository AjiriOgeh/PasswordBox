package com.passwordbox.utilities;

import java.security.SecureRandom;

public final class PasscodeGenerator {
    private static SecureRandom secureRandom = new SecureRandom();

    public static String generatePassword(int passwordLength){
        String password = "";
        String characters = "abcdefghijklmnopqrstuvwxyz0123456789~`!@#$%^&*()-=_+[{]};:',<.>/?ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        for (int count = 0; count < passwordLength; count++){
            int randomIndex = secureRandom.nextInt(characters.length());
            password += String.valueOf(characters.charAt(randomIndex));
        }
        return password;
    }

    public static String generatePin(int pinLength) {
        String pin = "";
        String numbers = "0123456789";
        for (int count = 0; count < pinLength; count++){
            int randomIndex = secureRandom.nextInt(numbers.length());
            pin += String.valueOf(numbers.charAt(randomIndex));
        }
        return pin;
    }

}
