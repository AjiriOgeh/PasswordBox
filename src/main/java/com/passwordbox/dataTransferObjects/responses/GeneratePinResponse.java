package com.passwordbox.dataTransferObjects.responses;

import lombok.Data;

@Data
public class GeneratePinResponse {
    private String pin;
    private int length;
}
