package com.passwordbox.dataTransferObjects.requests;

import lombok.Data;

@Data
public class SaveNewLoginInfoRequest {
    private String username;
    private String title;
    private String website;
    private String loginId;
}
