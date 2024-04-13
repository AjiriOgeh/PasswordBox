package com.passwordbox.services;

import com.passwordbox.data.models.*;
import com.passwordbox.data.repositories.VaultRepository;
import com.passwordbox.dataTransferObjects.requests.*;
import com.passwordbox.dataTransferObjects.responses.DeleteCreditCardResponse;
import com.passwordbox.dataTransferObjects.responses.DeleteLoginInfoResponse;
import com.passwordbox.dataTransferObjects.responses.DeleteNoteResponse;
import com.passwordbox.dataTransferObjects.responses.DeletePassportResponse;
import com.passwordbox.exceptions.NoteNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.passwordbox.utilities.FindDetails.*;

@Service
public class VaultServiceImplementation implements VaultService{

    @Autowired
    private VaultRepository vaultRepository;

    @Autowired
    private LoginInfoService loginInfoService;

    @Autowired
    private NoteService noteService;

    @Autowired
    private CreditCardService creditCardService;

    @Autowired
    private PassportService passportService;

    @Override
    public Vault createVault() {
        Vault newVault = new Vault();
        vaultRepository.save(newVault);
        return newVault;
    }

    @Override
    public LoginInfo saveNewLoginInfo(SaveNewLoginInfoRequest saveNewLoginInfoRequest, Vault vault) {
        LoginInfo loginInfo = loginInfoService.saveNewLoginInfo(saveNewLoginInfoRequest, vault);
        vault.getLoginInfos().add(loginInfo);
        vaultRepository.save(vault);
        return loginInfo;
    }

    @Override
    public LoginInfo editLoginInfo(EditLoginInfoRequest editLoginInfoRequest, Vault vault) {
        LoginInfo loginInfo = loginInfoService.editLoginInfo(editLoginInfoRequest, vault);
        vaultRepository.save(vault);
        return loginInfo;
    }

    @Override
    public DeleteLoginInfoResponse deleteLoginInfo(DeleteLoginInfoRequest deleteLoginInfoRequest, Vault vault) {
        LoginInfo loginInfo = findLoginInfoInVault(deleteLoginInfoRequest.getTitle().toLowerCase(), vault);
        DeleteLoginInfoResponse deleteLoginInfoResponse = loginInfoService.deleteLoginInfo(deleteLoginInfoRequest, vault);
        vault.getLoginInfos().remove(loginInfo);
        vaultRepository.save(vault);
        return deleteLoginInfoResponse;
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
        Note note = findNoteInVault(deleteNoteRequest.getTitle().toLowerCase(), vault);
        DeleteNoteResponse deleteNoteResponse = noteService.deleteNote(deleteNoteRequest, vault);
        vault.getNotes().remove(note);
        vaultRepository.save(vault);
        return deleteNoteResponse;
    }

    @Override
    public CreditCard saveCreditCard(SaveCreditCardRequest saveCreditCardRequest, Vault vault) {
        CreditCard creditCard = creditCardService.saveCreditCard(saveCreditCardRequest);
        vault.getCreditCards().add(creditCard);
        vaultRepository.save(vault);
        return creditCard;
    }

    @Override
    public CreditCard editCreditCard(EditCreditCardRequest editCreditCardRequest, Vault vault) {
        CreditCard creditCard = creditCardService.editCreditCard(editCreditCardRequest, vault);
        vaultRepository.save(vault);
        return creditCard;
    }

    @Override
    public DeleteCreditCardResponse deleteCreditCard(DeleteCreditCardRequest deleteCreditCardRequest, Vault vault) {
        CreditCard creditCard = findCreditCardInVault(deleteCreditCardRequest.getTitle(), vault);
        DeleteCreditCardResponse deleteCreditCardResponse = creditCardService.deleteCreditCard(deleteCreditCardRequest, vault);
        vault.getCreditCards().remove(creditCard);
        vaultRepository.save(vault);
        return deleteCreditCardResponse;
    }

    @Override
    public Passport savePassport(SavePassportRequest savePassportRequest, Vault vault) {
        Passport passport = passportService.savePassport(savePassportRequest);
        vault.getPassports().add(passport);
        vaultRepository.save(vault);
        return passport;
    }

    @Override
    public DeletePassportResponse deletePassport(DeletePassportRequest deletePassportRequest, Vault vault) {
        Passport passport = findPassportInVault(deletePassportRequest.getUsername(), vault);
        DeletePassportResponse deletePassportResponse = passportService.deletePassport(deletePassportRequest, vault);
        vault.getPassports().remove(passport);
        return null;
    }

}
