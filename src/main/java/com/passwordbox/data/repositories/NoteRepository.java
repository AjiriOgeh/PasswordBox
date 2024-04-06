package com.passwordbox.data.repositories;

import com.passwordbox.data.models.Note;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NoteRepository extends MongoRepository<Note, String> {
}
