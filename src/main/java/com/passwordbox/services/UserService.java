package com.passwordbox.services;

import com.passwordbox.dataTransferObjects.requests.*;
import com.passwordbox.dataTransferObjects.responses.*;

public interface UserService {
    RegisterResponse signUp(RegisterRequest registerRequest);

    LogoutResponse logout(LogoutRequest logoutRequest);

    LoginResponse login(LoginRequest loginRequest);

    SaveNewLoginInfoResponse saveNewLoginInfo(SaveNewLoginInfoRequest saveNewLoginInfoRequest) throws Exception;

    EditLoginInfoResponse editLoginInfo(EditLoginInfoRequest editLoginInfoRequest) throws Exception;

    ViewLoginInfoResponse viewLoginInfo(ViewLoginInfoRequest viewLoginInfoRequest) throws Exception;

    DeleteLoginInfoResponse deleteLoginInfo(DeleteLoginInfoRequest deleteLoginInfoRequest);

    CreateNoteResponse createNote(CreateNoteRequest createNoteRequest) throws Exception;

    EditNoteResponse editNote(EditNoteRequest editNoteRequest) throws Exception;

    ViewNoteResponse viewNote(ViewNoteRequest viewNoteRequest) throws Exception;

    DeleteNoteResponse deleteNote(DeleteNoteRequest deleteNoteRequest);

    GeneratePasswordResponse generatePassword(GeneratePasswordRequest generatePasswordRequest);

    GeneratePinResponse generatePin(GeneratePinRequest generatePinRequest);

    SaveCreditCardResponse saveCreditCard(SaveCreditCardRequest saveCreditCardRequest) throws Exception;

    EditCreditCardResponse editCreditCard(EditCreditCardRequest editCreditCardRequest) throws Exception;

    ViewCreditCardResponse viewCreditCard(ViewCreditCardRequest viewCreditCardRequest) throws Exception;

    DeleteCreditCardResponse deleteCreditCard(DeleteCreditCardRequest deleteCreditCardRequest);


}