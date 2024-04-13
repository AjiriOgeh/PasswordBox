package com.passwordbox.dataTransferObjects.requests;

import lombok.Data;

@Data
public class EditCreditCardRequest {
    private String username;
    private String title;
    private String updateTitle;
    private String updatedCardNumber;
    private String updatedCVV;
    private String updatedPin;
    private String updatedCardType;
    private String updatedExpiryDate;
    private String updatedAdditionalInformation;
}
