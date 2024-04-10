package com.passwordbox.dataTransferObjects.responses;

import lombok.Data;

@Data
public class SaveNewLoginInfoResponse {
    private String id;
    private String title;
    private String website;
    private String loginId;
    private String password;
}
