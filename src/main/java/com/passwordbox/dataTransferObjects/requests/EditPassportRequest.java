package com.passwordbox.dataTransferObjects.requests;

import lombok.Data;

@Data
public class EditPassportRequest {
    private String username;
    private String title;
    private String updatedTitle;
    private String updatedSurname;
    private String updatedGivenNames;
    private String updatedNationality;
    private String updatedPlaceOfBirth;
    private String updatedDateOfBirth;
    private String updatedPassportNumber;
    private String updatedIssueDate;
    private String updatedExpiryDate;
}
