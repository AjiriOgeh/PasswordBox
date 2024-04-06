package com.passwordbox.utilities;

import com.passwordbox.data.models.LoginInfo;
import com.passwordbox.data.models.Note;
import com.passwordbox.data.models.User;
import com.passwordbox.data.models.Vault;
import com.passwordbox.dataTransferObjects.requests.*;
import com.passwordbox.dataTransferObjects.responses.*;

import java.time.format.DateTimeFormatter;

import static com.passwordbox.utilities.PasswordGenerator.generatePassword;

public class Mappers {
    public static User registerRequestMap(RegisterRequest registerRequest, Vault vault) {
        User newUser = new User();
        newUser.setUsername(registerRequest.getUsername());
        newUser.setMasterPassword(registerRequest.getMasterPassword());
        newUser.setVault(vault);
        return newUser;
    }

    public static RegisterResponse registerResponseMap(User user) {
        RegisterResponse registerResponse = new RegisterResponse();
        registerResponse.setId(user.getId());
        registerResponse.setUsername(user.getUsername());
        registerResponse.setDateOfRegistration(user.getDateOfRegistration().format(DateTimeFormatter.ofPattern("MMM yyyy")));
        return registerResponse;
    }

    public static LogoutResponse logoutResponseMap(User user) {
        LogoutResponse logoutResponse = new LogoutResponse();
        logoutResponse.setId(user.getId());
        logoutResponse.setUsername(user.getUsername());
        return logoutResponse;
    }

    public static LoginResponse loginResponseMap(User user) {
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setId(user.getId());
        loginResponse.setUsername(user.getUsername());
        return loginResponse;
    }

    public static LoginInfo saveNewLoginInfoRequestMap(SaveNewLoginInfoRequest saveNewLoginInfoRequest) {
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setTitle(saveNewLoginInfoRequest.getTitle());
        loginInfo.setWebsite(saveNewLoginInfoRequest.getWebsite());
        loginInfo.setLoginId(saveNewLoginInfoRequest.getLoginId());
        loginInfo.setPassword(generatePassword());
        return loginInfo;
    }

    public static SaveNewLoginInfoResponse saveNewLoginInfoResponseMap(LoginInfo loginInfo){
        SaveNewLoginInfoResponse saveNewLoginInfoResponse = new SaveNewLoginInfoResponse();
        saveNewLoginInfoResponse.setId(loginInfo.getId());
        saveNewLoginInfoResponse.setTitle(loginInfo.getTitle());
        saveNewLoginInfoResponse.setWebsite(loginInfo.getWebsite());
        return saveNewLoginInfoResponse;
    }

    public static LoginInfo editLoginInfoRequestMap(EditLoginInfoRequest editLoginInfoRequest, LoginInfo loginInfo) {
        loginInfo.setTitle(editLoginInfoRequest.getEditedTitle());
        loginInfo.setLoginId(editLoginInfoRequest.getEditedLoginId());
        loginInfo.setWebsite(editLoginInfoRequest.getEditedWebsite());
        loginInfo.setPassword(editLoginInfoRequest.getEditedPassword());
        return loginInfo;
    }

    public static EditLoginInfoResponse editLoginInfoResponseMap(LoginInfo loginInfo){
        EditLoginInfoResponse editLoginInfoResponse = new EditLoginInfoResponse();
        editLoginInfoResponse.setId(loginInfo.getId());
        editLoginInfoResponse.setTitle(loginInfo.getTitle());
        editLoginInfoResponse.setWebsite(loginInfo.getWebsite());
        return editLoginInfoResponse;
    }

    public static ViewLoginInfoResponse viewLoginInfoResponseMap(LoginInfo loginInfo){
        ViewLoginInfoResponse viewLoginInfoResponse = new ViewLoginInfoResponse();
        viewLoginInfoResponse.setLoginId(loginInfo.getLoginId());
        viewLoginInfoResponse.setTitle(loginInfo.getTitle());
        viewLoginInfoResponse.setWebsite(loginInfo.getWebsite());
        viewLoginInfoResponse.setLoginId(loginInfo.getLoginId());
        viewLoginInfoResponse.setPassword(loginInfo.getPassword());
        return viewLoginInfoResponse;
    }

    public static DeleteLoginInfoResponse deleteLoginInfoResponseMap(LoginInfo loginInfo) {
        DeleteLoginInfoResponse deleteLoginInfoResponse = new DeleteLoginInfoResponse();
        deleteLoginInfoResponse.setId(loginInfo.getId());
        deleteLoginInfoResponse.setTitle(loginInfo.getTitle());
        return deleteLoginInfoResponse;
    }

    public static Note createNoteRequestMap(CreateNoteRequest createNoteRequest) {
        Note newNote = new Note();
        newNote.setTitle(createNoteRequest.getTitle());
        newNote.setContent(createNoteRequest.getContent());
        return newNote;
    }

    public static CreateNoteResponse createNoteResponseMap(Note note) {
        CreateNoteResponse createNoteResponse = new CreateNoteResponse();
        createNoteResponse.setId(note.getId());
        createNoteResponse.setTitle(note.getTitle());
        createNoteResponse.setContent(note.getContent());
        return createNoteResponse;
    }

    public static Note editNoteRequestMap(EditNoteRequest editNoteRequest, Note note) {
        note.setTitle(editNoteRequest.getEditedTitle());
        note.setContent(editNoteRequest.getEditedContent());
        return note;
    }

    public static EditNoteResponse editNoteResponseMap(Note note) {
        EditNoteResponse editNoteResponse = new EditNoteResponse();
        editNoteResponse.setId(note.getId());
        editNoteResponse.setTitle(note.getTitle());
        editNoteResponse.setContent(note.getContent());
        return editNoteResponse;
    }

    public static ViewNoteResponse viewNoteResponseMap(Note note) {
        ViewNoteResponse viewNoteResponse = new ViewNoteResponse();
        viewNoteResponse.setId(note.getId());
        viewNoteResponse.setTitle(note.getTitle());
        viewNoteResponse.setContent(note.getContent());
        return viewNoteResponse;
    }

    public static DeleteNoteResponse deleteNoteResponseMap(Note note) {
        DeleteNoteResponse deleteNoteResponse = new DeleteNoteResponse();
        deleteNoteResponse.setId(note.getId());
        deleteNoteResponse.setTitle(note.getTitle());
        return deleteNoteResponse;
    }

}