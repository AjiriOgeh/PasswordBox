package com.passwordbox.dataTransferObjects.requests;

import lombok.Data;

@Data
public class EditNoteRequest {
    private String username;
    private String title;
    private String editedTitle;
    private String editedContent;
}
