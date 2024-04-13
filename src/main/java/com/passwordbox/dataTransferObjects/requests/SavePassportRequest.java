package com.passwordbox.dataTransferObjects.requests;

import lombok.Data;

@Data
public class SavePassportRequest {
    private String username;
    private String title;
    private String surname;
    private String givenNames;
    private String nationality;
    private String placeOfBirth;
    private String dateOfBirth;
    private String passportNumber;
    private String issueDate;
    private String expiryDate;
    private String additionalInformation;
}
