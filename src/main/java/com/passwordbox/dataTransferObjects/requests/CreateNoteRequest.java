package com.passwordbox.dataTransferObjects.requests;

import lombok.Data;

@Data
public class CreateNoteRequest {
    private String username;
    private String title;
    private String content;
}
