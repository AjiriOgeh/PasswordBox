package com.passwordbox.dataTransferObjects.requests;

import lombok.Data;

@Data
public class SaveCreditCardRequest {
    private String username;
    private String title;
    private String cardNumber;
    private String cardName;
    private String cVV;
    private String pin;
    private String cardType;
    private String expiryDate;
    private String additionalInformation;
}
