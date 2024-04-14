package com.passwordbox.utilities;

import com.passwordbox.data.models.Vault;

import static com.passwordbox.utilities.CreditCardValidator.isCreditCardInvalid;

public class ValidateInputs {

    public static  void validateLoginInfoTitle(String title, Vault vault) {
        validateTitleInput(title);
        if (doesLoginInfoTitleExist(title, vault)) throw new IllegalArgumentException("Title already exists. Please enter a different title");
    }

    private static boolean doesLoginInfoTitleExist(String title, Vault vault) {
        for(int count = 0; count < vault.getLoginInfos().size(); count++){
            if (vault.getLoginInfos().get(count).getTitle().equals(title.toLowerCase()))
                return true;
        }
        return false;
    }

    public static void validateNoteTitle(String title, Vault vault) {
        validateTitleInput(title);
        if (doesNoteTitleExist(title, vault)) throw new IllegalArgumentException("Title Already Exists. Please enter a different title");
    }

    private static boolean doesNoteTitleExist(String title, Vault vault) {
        for(int count = 0; count < vault.getNotes().size(); count++){
            if (vault.getNotes().get(count).getTitle().equals(title.toLowerCase()))
                return true;
        }
        return false;
    }

    public static void validateCreditCardTitle(String title, Vault vault) {
        validateTitleInput(title);
        if (doesCreditCardTitleExist(title, vault)) throw new IllegalArgumentException("Title Already Exists. Please enter a different title");
    }

    private static boolean doesCreditCardTitleExist(String title, Vault vault) {
        for(int count = 0; count < vault.getCreditCards().size(); count++){
            if (vault.getCreditCards().get(count).getTitle().equals(title.toLowerCase()))
                return true;
        }
        return false;
    }

    private static void validateTitleInput(String title) {
        if (title == null) throw new IllegalArgumentException("Title field cannot be null. Please enter a valid title.");
        if (title.isEmpty()) throw new IllegalArgumentException("Title field cannot be empty. Please enter a valid title.");
        if (title.length() > 30) throw new IllegalArgumentException("Title cannot be more than 30 Characters. Please enter a valid title");
    }

    public static void validateCreditCardPin(String creditCardPin) {
        if (creditCardPin!= null && !creditCardPin.matches("\\d+")) throw new IllegalArgumentException("Please enter a valid pin");
    }

    public static void validateCreditCardCVV(String cVV) {
        if (cVV != null && !cVV.matches("\\d+")) throw new IllegalArgumentException("Please enter a valid cvv");
    }

    public static void validateCreditCardNumber(String cardNumber) {
        if (cardNumber!= null && !cardNumber.matches("\\d+")) throw new IllegalArgumentException("Please enter a valid card number");
        if (cardNumber != null && cardNumber.length() > 19) throw new IllegalArgumentException("Please enter a valid card number");
        if (cardNumber != null && isCreditCardInvalid(cardNumber)) throw new IllegalArgumentException("Please enter a valid card number");
    }


}
