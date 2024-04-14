package com.passwordbox.services;

import com.passwordbox.data.models.*;
import com.passwordbox.dataTransferObjects.requests.*;
import com.passwordbox.dataTransferObjects.responses.DeleteCreditCardResponse;
import com.passwordbox.dataTransferObjects.responses.DeleteLoginInfoResponse;
import com.passwordbox.dataTransferObjects.responses.DeleteNoteResponse;

public interface VaultService {

    Vault createVault();

    LoginInfo saveNewLoginInfo(SaveNewLoginInfoRequest saveNewLoginInfoRequest, Vault vault) throws Exception;

    LoginInfo editLoginInfo(EditLoginInfoRequest editLoginInfoRequest, Vault vault) throws Exception;

    DeleteLoginInfoResponse deleteLoginInfo(DeleteLoginInfoRequest deleteLoginInfoRequest, Vault vault);

    Note createNote(CreateNoteRequest createNoteRequest, Vault vault);

    Note editNote(EditNoteRequest editNoteRequest, Vault vault) throws Exception;

    DeleteNoteResponse deleteNote(DeleteNoteRequest deleteNoteRequest, Vault vault);

    CreditCard saveCreditCard(SaveCreditCardRequest saveCreditCardRequest, Vault vault) throws Exception;

    CreditCard editCreditCard(EditCreditCardRequest editCreditCardRequest, Vault vault) throws Exception;

    DeleteCreditCardResponse deleteCreditCard(DeleteCreditCardRequest deleteCreditCardRequest, Vault vault);

}
