package com.passwordbox.dataTransferObjects.responses;

import lombok.Data;

@Data
public class CreateNoteResponse {
    private String id;
    private String title;
    private String content;
}
