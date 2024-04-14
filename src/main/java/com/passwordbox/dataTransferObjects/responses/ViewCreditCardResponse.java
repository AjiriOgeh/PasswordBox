package com.passwordbox.dataTransferObjects.responses;

import com.passwordbox.data.models.CreditCard;
import lombok.Data;

@Data
public class ViewCreditCardResponse {
    private String id;
    private String title;
    private String creditCardNumber;
    private String Pin;
    private String cVV;
}
