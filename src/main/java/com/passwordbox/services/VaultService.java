package com.passwordbox.services;

import com.passwordbox.data.models.LoginInfo;
import com.passwordbox.data.models.Note;
import com.passwordbox.data.models.Vault;
import com.passwordbox.dataTransferObjects.requests.*;
import com.passwordbox.dataTransferObjects.responses.DeleteLoginInfoResponse;
import com.passwordbox.dataTransferObjects.responses.DeleteNoteResponse;

public interface VaultService {

    Vault createVault();

    LoginInfo saveNewLoginInfo(SaveNewLoginInfoRequest saveNewLoginInfoRequest, Vault vault);

    LoginInfo editLoginInformation(EditLoginInfoRequest editLoginInfoRequest, Vault vault);

    DeleteLoginInfoResponse deleteLoginInformation(DeleteLoginInfoRequest deleteLoginInfoRequest, Vault vault);

    Note createNote(CreateNoteRequest createNoteRequest, Vault vault);

    Note editNote(EditNoteRequest editNoteRequest, Vault vault);

    DeleteNoteResponse deleteNote(DeleteNoteRequest deleteNoteRequest, Vault vault);
}
