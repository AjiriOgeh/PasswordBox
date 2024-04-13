package com.passwordbox.services;

import com.passwordbox.data.models.*;
import com.passwordbox.dataTransferObjects.requests.*;
import com.passwordbox.dataTransferObjects.responses.DeleteCreditCardResponse;
import com.passwordbox.dataTransferObjects.responses.DeleteLoginInfoResponse;
import com.passwordbox.dataTransferObjects.responses.DeleteNoteResponse;
import com.passwordbox.dataTransferObjects.responses.DeletePassportResponse;

public interface VaultService {

    Vault createVault();

    LoginInfo saveNewLoginInfo(SaveNewLoginInfoRequest saveNewLoginInfoRequest, Vault vault);

    LoginInfo editLoginInfo(EditLoginInfoRequest editLoginInfoRequest, Vault vault);

    DeleteLoginInfoResponse deleteLoginInfo(DeleteLoginInfoRequest deleteLoginInfoRequest, Vault vault);

    Note createNote(CreateNoteRequest createNoteRequest, Vault vault);

    Note editNote(EditNoteRequest editNoteRequest, Vault vault);

    DeleteNoteResponse deleteNote(DeleteNoteRequest deleteNoteRequest, Vault vault);

    CreditCard saveCreditCard(SaveCreditCardRequest saveCreditCardRequest, Vault vault);

    CreditCard editCreditCard(EditCreditCardRequest editCreditCardRequest, Vault vault);

    DeleteCreditCardResponse deleteCreditCard(DeleteCreditCardRequest deleteCreditCardRequest, Vault vault);

    Passport savePassport(SavePassportRequest savePassportRequest, Vault vault);

    DeletePassportResponse deletePassport(DeletePassportRequest deletePassportRequest, Vault vault);
}
