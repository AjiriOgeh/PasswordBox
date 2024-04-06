package com.passwordbox.services;

import com.passwordbox.data.models.LoginInfo;
import com.passwordbox.data.models.Note;
import com.passwordbox.data.models.Vault;
import com.passwordbox.data.repositories.VaultRepository;
import com.passwordbox.dataTransferObjects.requests.*;
import com.passwordbox.dataTransferObjects.responses.DeleteLoginInfoResponse;
import com.passwordbox.dataTransferObjects.responses.DeleteNoteResponse;
import com.passwordbox.exceptions.LoginInfoNotFoundException;
import com.passwordbox.exceptions.NoteNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VaultServiceImplementation implements VaultService{

    @Autowired
    private VaultRepository vaultRepository;

    @Autowired
    private LoginInfoService loginInfoService;

    @Autowired
    private NoteService noteService;

    @Override
    public Vault createVault() {
        Vault newVault = new Vault();
        vaultRepository.save(newVault);
        return newVault;
    }

    @Override
    public LoginInfo saveNewLogin(SaveNewLoginInfoRequest saveNewLoginInfoRequest, Vault vault) {
        LoginInfo loginInfo = loginInfoService.saveNewLoginInfo(saveNewLoginInfoRequest, vault);
        vault.getLoginInfos().add(loginInfo);
        vaultRepository.save(vault);
        return loginInfo;
    }

    @Override
    public LoginInfo editLoginInformation(EditLoginInfoRequest editLoginInfoRequest, Vault vault) {
        LoginInfo loginInfo = loginInfoService.editLoginInfo(editLoginInfoRequest, vault);
        vaultRepository.save(vault);
        return loginInfo;
    }

    @Override
    public DeleteLoginInfoResponse deleteLoginInformation(DeleteLoginInfoRequest deleteLoginInfoRequest, Vault vault) {
        LoginInfo loginInfo = findLoginInfoInVault(deleteLoginInfoRequest.getTitle(), vault);
        DeleteLoginInfoResponse deleteLoginInfoResponse = loginInfoService.deleteLoginInfo(deleteLoginInfoRequest, vault);
        vault.getLoginInfos().remove(loginInfo);
        vaultRepository.save(vault);
        return deleteLoginInfoResponse;
    }

    private LoginInfo findLoginInfoInVault(String title, Vault vault) {
        for(int count = 0; count < vault.getLoginInfos().size(); count++){
            if (vault.getLoginInfos().get(count).getTitle().equals(title))
                return vault.getLoginInfos().get(count);
        }
        throw new LoginInfoNotFoundException("Login Info does not Exist. Please Try Again");
    }

    @Override
    public Note createNote(CreateNoteRequest createNoteRequest, Vault vault) {
        Note note = noteService.createNote(createNoteRequest, vault);
        vault.getNotes().add(note);
        vaultRepository.save(vault);
        return note;
    }

    @Override
    public Note editNote(EditNoteRequest editNoteRequest, Vault vault) {
        Note note = noteService.editNote(editNoteRequest, vault);
        vaultRepository.save(vault);
        return note;
    }

    @Override
    public DeleteNoteResponse deleteNote(DeleteNoteRequest deleteNoteRequest, Vault vault) {
        Note note = findNoteInVault(deleteNoteRequest.getTitle(), vault);
        DeleteNoteResponse deleteNoteResponse = noteService.deleteNote(deleteNoteRequest, vault);
        vault.getNotes().remove(note);
        vaultRepository.save(vault);
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
