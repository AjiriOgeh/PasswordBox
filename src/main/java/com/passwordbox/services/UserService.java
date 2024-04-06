package com.passwordbox.services;

import com.passwordbox.dataTransferObjects.requests.*;
import com.passwordbox.dataTransferObjects.responses.*;

public interface UserService {
    RegisterResponse signUp(RegisterRequest registerRequest);

    LogoutResponse logout(LogoutRequest logoutRequest);

    LoginResponse login(LoginRequest loginRequest);

    SaveNewLoginInfoResponse saveNewLoginInfo(SaveNewLoginInfoRequest saveNewLoginInfoRequest);

    EditLoginInfoResponse editLoginInformation(EditLoginInfoRequest editLoginInfoRequest);

    ViewLoginInfoResponse viewLoginInfo(ViewLoginInfoRequest viewLoginInfoRequest);

    DeleteLoginInfoResponse deleteLoginInfo(DeleteLoginInfoRequest deleteLoginInfoRequest);

    CreateNoteResponse createNote(CreateNoteRequest createNoteRequest);

    EditNoteResponse editNote(EditNoteRequest editNoteRequest);

    ViewNoteResponse viewNote(ViewNoteRequest viewNoteRequest);

    DeleteNoteResponse deleteNote(DeleteNoteRequest deleteNoteRequest);
}
