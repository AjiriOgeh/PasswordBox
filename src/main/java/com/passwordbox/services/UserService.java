package com.passwordbox.services;

import com.passwordbox.dataTransferObjects.requests.*;
import com.passwordbox.dataTransferObjects.responses.*;

public interface UserService {
    RegisterResponse signUp(RegisterRequest registerRequest);

    LogoutResponse logout(LogoutRequest logoutRequest);

    LoginResponse login(LoginRequest loginRequest);

    SaveNewLoginInfoResponse saveNewLoginInfo(SaveNewLoginInfoRequest saveNewLoginInfoRequest);

    EditLoginInfoResponse editLoginInfo(EditLoginInfoRequest editLoginInfoRequest);

    ViewLoginInfoResponse viewLoginInfo(ViewLoginInfoRequest viewLoginInfoRequest);

    DeleteLoginInfoResponse deleteLoginInfo(DeleteLoginInfoRequest deleteLoginInfoRequest);

    CreateNoteResponse createNote(CreateNoteRequest createNoteRequest);

    EditNoteResponse editNote(EditNoteRequest editNoteRequest);

    ViewNoteResponse viewNote(ViewNoteRequest viewNoteRequest);

    DeleteNoteResponse deleteNote(DeleteNoteRequest deleteNoteRequest);

    GeneratePasswordResponse generatePassword(GeneratePasswordRequest generatePasswordRequest);

    GeneratePinResponse generatePin(GeneratePinRequest generatePinRequest);

    SaveCreditCardResponse saveCreditCard(SaveCreditCardRequest saveCreditCardRequest);

    EditCreditCardResponse editCreditCard(EditCreditCardRequest editCreditCardRequest);

    ViewCreditCardResponse viewCreditCard(ViewCreditCardRequest viewCreditCardRequest);

    DeleteCreditCardResponse deleteCreditCard(DeleteCreditCardRequest deleteCreditCardRequest);

    SavePassportResponse savePassport(SavePassportRequest savePassportRequest);

    ViewPassportResponse viewPassport(ViewPassportRequest viewPassportRequest);

    DeletePassportResponse deletePassport(DeletePassportRequest deletePassportRequest);
}
