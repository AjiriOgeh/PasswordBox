package com.passwordbox.dataTransferObjects.requests;

import lombok.Data;

@Data
public class ViewNoteRequest {
    private String username;
    private String title;
}
