package com.passwordbox.services;

import com.passwordbox.data.models.Note;
import com.passwordbox.data.models.Vault;
import com.passwordbox.data.repositories.NoteRepository;
import com.passwordbox.dataTransferObjects.requests.CreateNoteRequest;
import com.passwordbox.dataTransferObjects.requests.DeleteNoteRequest;
import com.passwordbox.dataTransferObjects.requests.EditNoteRequest;
import com.passwordbox.dataTransferObjects.responses.DeleteNoteResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.passwordbox.utilities.FindDetails.findNoteInVault;
import static com.passwordbox.utilities.Mappers.*;
import static com.passwordbox.utilities.ValidateInputs.validateNoteTitle;

@Service
public class NoteServiceImplementation implements NoteService{

    @Autowired
    private NoteRepository noteRepository;
    @Override
    public Note createNote(CreateNoteRequest createNoteRequest, Vault vault) {
        validateNoteTitle(createNoteRequest.getTitle(), vault);
        Note note = createNoteRequestMap(createNoteRequest);
        noteRepository.save(note);
        return note;
    }

    @Override
    public Note editNote(EditNoteRequest editNoteRequest, Vault vault) throws Exception {
        Note note = findNoteInVault(editNoteRequest.getTitle().toLowerCase(), vault);
        validateNoteTitle(editNoteRequest.getEditedTitle(), vault);
        Note updatedNote = editNoteRequestMap(editNoteRequest, note);
        noteRepository.save(updatedNote);
        return updatedNote;
    }

    @Override
    public DeleteNoteResponse deleteNote(DeleteNoteRequest deleteNoteRequest, Vault vault) {
        Note note = findNoteInVault(deleteNoteRequest.getTitle().toLowerCase(), vault);
        DeleteNoteResponse deleteNoteResponse = deleteNoteResponseMap(note);
        noteRepository.delete(note);
        return deleteNoteResponse;
    }

}
