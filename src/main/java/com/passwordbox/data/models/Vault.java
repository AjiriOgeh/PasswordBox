package com.passwordbox.data.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document("Vaults")
public class Vault {
    @Id
    private String id;
    @DBRef
    private List<LoginInfo> loginInfos = new ArrayList<>();
    @DBRef
    private List<Note> notes = new ArrayList<>();
}
