package com.passwordbox.data.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("Notes")
public class Note {
    @Id
    private String id;
    private String title;
    private String content;
}
