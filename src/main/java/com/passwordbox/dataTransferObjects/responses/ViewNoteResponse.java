package com.passwordbox.dataTransferObjects.responses;

import lombok.Data;

@Data
public class ViewNoteResponse {
    private String id;
    private String title;
    private String content;
}
