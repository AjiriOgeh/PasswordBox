package com.passwordbox.services;

import com.passwordbox.data.models.Note;
import com.passwordbox.data.models.Vault;
import com.passwordbox.data.repositories.NoteRepository;
import com.passwordbox.dataTransferObjects.requests.CreateNoteRequest;
import com.passwordbox.dataTransferObjects.requests.DeleteNoteRequest;
import com.passwordbox.dataTransferObjects.requests.EditNoteRequest;
import com.passwordbox.dataTransferObjects.responses.DeleteNoteResponse;
import com.passwordbox.exceptions.NoteNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public Note editNote(EditNoteRequest editNoteRequest, Vault vault) {
        //refactor this part of the code make sure it matches existing object(The title)
        // change the validateLoginInformation
        //refactor this part of the code make sure it matches existing object(The title)
        //validate title
        //validateLoginInformationTitle(editNoteRequest.getEditedTitle(), vault);
        Note note = findNoteInVault(editNoteRequest.getTitle(), vault);
        editNoteRequestMap(editNoteRequest, note);
        noteRepository.save(note);
        return note;
    }

    @Override
    public DeleteNoteResponse deleteNote(DeleteNoteRequest deleteNoteRequest, Vault vault) {
        Note note = findNoteInVault(deleteNoteRequest.getTitle(), vault);
        DeleteNoteResponse deleteNoteResponse = deleteNoteResponseMap(note);
        noteRepository.delete(note);
        return deleteNoteResponse;
    }

    private Note findNoteInVault(String title, Vault vault) {
        for(int count = 0; count < vault.getNotes().size(); count++){
            if (vault.getNotes().get(count).getTitle().equals(title))
                return vault.getNotes().get(count);
        }
        throw new NoteNotFoundException("Note does not Exist. Please Try Again");
    }
}
