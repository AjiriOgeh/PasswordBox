package com.passwordbox.utilities;

public class CreditCardValidator {
    private static String creditCardNumber;
    private static long[] creditCardNumbersArray;
    private static long doubleNumbersTotal;
    private static long oddPlaceNumbersTotal;
    public static void setCreditCardNumber(String creditCardNumber){
        CreditCardValidator.creditCardNumber = creditCardNumber;
    }
//    public static String getCreditCardNumber() {
//        return creditCardNumber;
//    }
//    public static int getCreditCardNumberLength() {
//        return creditCardNumber.length();
//    }

    public static void setCreditCardNumberArray() {
        long cardNumber = Long.parseLong(creditCardNumber);
        creditCardNumbersArray = new long[creditCardNumber.length()];
        for (int count = creditCardNumber.length() - 1; count >= 0 ; count--) {
            creditCardNumbersArray[count] = cardNumber % 10;
            cardNumber /= 10;
        }
    }

//    public static String getCardType() {
//        String cardType = "Invalid";
//        if (creditCardNumbersArray[0] == 4) {
//            cardType = "Visa card";
//        }
//        if (creditCardNumbersArray[0] == 5){
//            cardType = "MasterCard";
//        }
//        if (creditCardNumbersArray[0] == 6) {
//            cardType = "Discover card";
//        }
//        if (creditCardNumbersArray[0] == 3 && creditCardNumbersArray[1] == 7) {
//            cardType = "American Express Card";
//        }
//        return cardType;
//    }

    public static void getDoubleDigitsTotal() {
        for (int count = creditCardNumbersArray.length - 2; count >= 0; count -= 2) {
            if ((creditCardNumbersArray[count] * 2) > 9) {
                long splitDigit1 = (creditCardNumbersArray[count] * 2) % 10;
                long splitDigit2 = (creditCardNumbersArray[count] * 2) / 10;
                doubleNumbersTotal += splitDigit1 + splitDigit2;
            }

            else {
                doubleNumbersTotal += creditCardNumbersArray[count] * 2;
            }
        }
    }

    public static void getOddPlaceNumbersTotal() {
        for (int count = creditCardNumbersArray.length - 1; count >= 0; count -=2) {
            oddPlaceNumbersTotal += creditCardNumbersArray[count];
        }
    }

    public static boolean getValidityStatus() {
        long total = oddPlaceNumbersTotal + doubleNumbersTotal;
        return total % 10 != 0;
    }

	public static boolean isCreditCardInvalid(String creditCardNumber) {
		CreditCardValidator.setCreditCardNumber(creditCardNumber);
		CreditCardValidator.setCreditCardNumberArray();
		CreditCardValidator.getDoubleDigitsTotal();
		CreditCardValidator.getOddPlaceNumbersTotal();
        return getValidityStatus();
	}

    public static void main(String[] args) {
        System.out.println(isCreditCardInvalid("5061190609476240365"));
                                                            //        "5061230173341883662"
    }

}
