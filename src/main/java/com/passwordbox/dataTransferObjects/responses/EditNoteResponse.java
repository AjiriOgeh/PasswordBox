package com.passwordbox.dataTransferObjects.responses;

import lombok.Data;

@Data
public class EditNoteResponse {
    private String id;
    private String title;
    private String content;
}
