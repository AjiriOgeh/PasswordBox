package com.passwordbox.services;

import com.passwordbox.data.models.Note;
import com.passwordbox.data.models.Vault;
import com.passwordbox.dataTransferObjects.requests.CreateNoteRequest;
import com.passwordbox.dataTransferObjects.requests.DeleteNoteRequest;
import com.passwordbox.dataTransferObjects.requests.EditNoteRequest;
import com.passwordbox.dataTransferObjects.responses.DeleteNoteResponse;

public interface NoteService {
    Note createNote(CreateNoteRequest createNoteRequest, Vault vault);

    Note editNote(EditNoteRequest editNoteRequest, Vault vault);

    DeleteNoteResponse deleteNote(DeleteNoteRequest deleteNoteRequest, Vault vault);
}
