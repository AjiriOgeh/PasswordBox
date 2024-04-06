package com.passwordbox.data.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Document("Users")
public class User {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String masterPassword;
    private boolean isLocked;
    @DBRef
    private Vault vault;
    private LocalDate dateOfRegistration = LocalDate.now();

}
