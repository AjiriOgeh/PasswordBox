package com.passwordbox.controllers;

import com.passwordbox.data.repositories.LoginInfoRepository;
import com.passwordbox.data.repositories.NoteRepository;
import com.passwordbox.data.repositories.UserRepository;
import com.passwordbox.data.repositories.VaultRepository;
import com.passwordbox.dataTransferObjects.requests.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserControllerTest {

    @Autowired
    private UserController userController;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VaultRepository vaultRepository;

    @Autowired
    private LoginInfoRepository loginInfoRepository;

    @Autowired
    private NoteRepository noteRepository;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();

        vaultRepository.deleteAll();

        loginInfoRepository.deleteAll();

        noteRepository.deleteAll();

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("jack123");
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Password123.");
        userController.signUp(registerRequest);

        SaveNewLoginInfoRequest saveNewLoginInfoRequest = new SaveNewLoginInfoRequest();
        saveNewLoginInfoRequest.setUsername("jack123");
        saveNewLoginInfoRequest.setTitle("gmail login");
        saveNewLoginInfoRequest.setWebsite("www.gmail.com");
        saveNewLoginInfoRequest.setLoginId("jack123@gmail.com");
        userController.saveNewLoginInfo(saveNewLoginInfoRequest);

        CreateNoteRequest createNoteRequest = new CreateNoteRequest();
        createNoteRequest.setUsername("jack123");
        createNoteRequest.setTitle("ideas");
        createNoteRequest.setContent("Build an AI assistant for projects");
        userController.createNote(createNoteRequest);
    }

    @Test
    public void userSignUpTest() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("jim456");
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Password123.");

        var response = userController.signUp(registerRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void userSignUp_UsernameIsNullTest() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername(null);
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Password123.");

        var response = userController.signUp(registerRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void userSignUp_UsernameIsEmptyTest() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("");
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Password123.");

        var response = userController.signUp(registerRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void userSignUp_UsernameContainsSpaceCharacterTest() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("jim 456");
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Password123.");

        var response = userController.signUp(registerRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void userSignUp_PasswordDoesNotMatchTest() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("jim456");
        registerRequest.setMasterPassword("Password456.");
        registerRequest.setConfirmMasterPassword("DifferentPassword456.");

        var response = userController.signUp(registerRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void userSignUp_PasswordFieldIsNullTest() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("jim456");
        registerRequest.setMasterPassword(null);
        registerRequest.setConfirmMasterPassword(null);

        var response = userController.signUp(registerRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void userSignUp_PasswordFieldIsEmptyTest() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("jim456");
        registerRequest.setMasterPassword("");
        registerRequest.setConfirmMasterPassword("");

        var response = userController.signUp(registerRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void userLogsOutTest() {
        LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setUsername("jack123");

        var response = userController.logout(logoutRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void nonExistentUserLogsOutTest() {
        LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setUsername("jim456");

        var response = userController.logout(logoutRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
    @Test
    public void userLogsInTest() {
        LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setUsername("jack123");

        var response = userController.logout(logoutRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("jack123");
        loginRequest.setPassword("Password123.");

        response = userController.login(loginRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void nonExistentUserLogsInTest() {
        LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setUsername("jack123");

        var response = userController.logout(logoutRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("jim456");
        loginRequest.setPassword("Password123.");

        response = userController.login(loginRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void userSavesNewLoginInfoTest() {
        SaveNewLoginInfoRequest saveNewLoginInfoRequest = new SaveNewLoginInfoRequest();
        saveNewLoginInfoRequest.setUsername("jack123");
        saveNewLoginInfoRequest.setTitle("yahoo login");
        saveNewLoginInfoRequest.setWebsite("www.yahoo.com");
        saveNewLoginInfoRequest.setLoginId("jack123@yahoo.com");

        var response = userController.saveNewLoginInfo(saveNewLoginInfoRequest);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void userSavesNewLoginInfo_TitleExistsTest() {
        SaveNewLoginInfoRequest saveNewLoginInfoRequest = new SaveNewLoginInfoRequest();
        saveNewLoginInfoRequest.setUsername("jack123");
        saveNewLoginInfoRequest.setTitle("gmail login");
        saveNewLoginInfoRequest.setWebsite("www.gmail.com");
        saveNewLoginInfoRequest.setLoginId("jack123@gmail.com");

        var response = userController.saveNewLoginInfo(saveNewLoginInfoRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void userSavesNewLoginInfo_TitleIsNullTest() {
        SaveNewLoginInfoRequest saveNewLoginInfoRequest = new SaveNewLoginInfoRequest();
        saveNewLoginInfoRequest.setUsername("jack123");
        saveNewLoginInfoRequest.setTitle(null);
        saveNewLoginInfoRequest.setWebsite("www.gmail.com");
        saveNewLoginInfoRequest.setLoginId("jack123@gmail.com");

        var response = userController.saveNewLoginInfo(saveNewLoginInfoRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void userSavesNewLoginInfo_TitleIsEmptyTest() {
        SaveNewLoginInfoRequest saveNewLoginInfoRequest = new SaveNewLoginInfoRequest();
        saveNewLoginInfoRequest.setUsername("jack123");
        saveNewLoginInfoRequest.setTitle(null);
        saveNewLoginInfoRequest.setWebsite("www.gmail.com");
        saveNewLoginInfoRequest.setLoginId("jack123@gmail.com");

        var response = userController.saveNewLoginInfo(saveNewLoginInfoRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void nonExistentUserSavesNewLoginInfoTest() {
        SaveNewLoginInfoRequest saveNewLoginInfoRequest = new SaveNewLoginInfoRequest();
        saveNewLoginInfoRequest.setUsername("jim456");
        saveNewLoginInfoRequest.setTitle("yahoo login");
        saveNewLoginInfoRequest.setWebsite("www.yahoo.com");
        saveNewLoginInfoRequest.setLoginId("jack123@yahoo.com");

        var response = userController.saveNewLoginInfo(saveNewLoginInfoRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
    /*
    *
    *
    *
    *
    *
    *
    *
    *
    * */

    @Test
    public void userViewsLoginInfoTest(){
        ViewLoginInfoRequest viewLoginInfoRequest = new ViewLoginInfoRequest();
        viewLoginInfoRequest.setUsername("jack123");
        viewLoginInfoRequest.setTitle("gmail login");

        var response = userController.viewLoginInfo(viewLoginInfoRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void nonExistentUser_ViewsLoginInfoTest(){
        ViewLoginInfoRequest viewLoginInfoRequest = new ViewLoginInfoRequest();
        viewLoginInfoRequest.setUsername("jim456");
        viewLoginInfoRequest.setTitle("gmail login");

        var response = userController.viewLoginInfo(viewLoginInfoRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void userViewsNonExistentLoginInfoTest(){
        ViewLoginInfoRequest viewLoginInfoRequest = new ViewLoginInfoRequest();
        viewLoginInfoRequest.setUsername("jack123");
        viewLoginInfoRequest.setTitle("yahoo login");

        var response = userController.viewLoginInfo(viewLoginInfoRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void userDeletesLoginInfoTest(){
        DeleteLoginInfoRequest deleteLoginInfoRequest = new DeleteLoginInfoRequest();
        deleteLoginInfoRequest.setUsername("jack123");
        deleteLoginInfoRequest.setTitle("gmail login");
        deleteLoginInfoRequest.setMasterPassword("Password123.");

        var response = userController.deleteLoginInfo(deleteLoginInfoRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void nonExistentUserDeletesLoginInfoTest(){
        DeleteLoginInfoRequest deleteLoginInfoRequest = new DeleteLoginInfoRequest();
        deleteLoginInfoRequest.setUsername("jim456");
        deleteLoginInfoRequest.setTitle("gmail login");
        deleteLoginInfoRequest.setMasterPassword("Password123.");

        var response = userController.deleteLoginInfo(deleteLoginInfoRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void userDeletesNonExistentLoginInfoTest(){
        DeleteLoginInfoRequest deleteLoginInfoRequest = new DeleteLoginInfoRequest();
        deleteLoginInfoRequest.setUsername("jack123");
        deleteLoginInfoRequest.setTitle("yahoo login");
        deleteLoginInfoRequest.setMasterPassword("Password123.");

        var response = userController.deleteLoginInfo(deleteLoginInfoRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void userDeletesLoginInfo_WithInvalidPasswordTest(){
        DeleteLoginInfoRequest deleteLoginInfoRequest = new DeleteLoginInfoRequest();
        deleteLoginInfoRequest.setUsername("jack123");
        deleteLoginInfoRequest.setTitle("yahoo login");
        deleteLoginInfoRequest.setMasterPassword("InvalidPassword123.");

        var response = userController.deleteLoginInfo(deleteLoginInfoRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void userCreatesNoteTest() {
        CreateNoteRequest createNoteRequest = new CreateNoteRequest();
        createNoteRequest.setUsername("jack123");
        createNoteRequest.setTitle("designs");
        createNoteRequest.setContent("Minimalist design for my bedroom");

        var response = userController.createNote(createNoteRequest);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void nonExistentUser_CreatesNoteTest() {
        CreateNoteRequest createNoteRequest = new CreateNoteRequest();
        createNoteRequest.setUsername("jim456");
        createNoteRequest.setTitle("designs");
        createNoteRequest.setContent("Minimalist design for my bedroom");

        var response = userController.createNote(createNoteRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void userCreatesNote_TitleIsNullTest() {
        CreateNoteRequest createNoteRequest = new CreateNoteRequest();
        createNoteRequest.setUsername("jack123");
        createNoteRequest.setTitle(null);
        createNoteRequest.setContent("Minimalist design for my bedroom");

        var response = userController.createNote(createNoteRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void userCreatesNote_TitleIsEmptyTest() {
        CreateNoteRequest createNoteRequest = new CreateNoteRequest();
        createNoteRequest.setUsername("jack123");
        createNoteRequest.setTitle("");
        createNoteRequest.setContent("Minimalist design for my bedroom");

        var response = userController.createNote(createNoteRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void userCreatesNote_TitleExistsTest() {
        CreateNoteRequest createNoteRequest = new CreateNoteRequest();
        createNoteRequest.setUsername("jack123");
        createNoteRequest.setTitle("ideas");
        createNoteRequest.setContent("Build Robots for heart surgeries");

        var response = userController.createNote(createNoteRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
    /*


    @Test
    public void saveNewLoginInfo_TitleExistsTest() {
        SaveNewLoginInfoRequest saveNewLoginInfoRequest = new SaveNewLoginInfoRequest();
        saveNewLoginInfoRequest.setUsername("jack123");
        saveNewLoginInfoRequest.setTitle("gmail login");
        saveNewLoginInfoRequest.setWebsite("www.gmail.com");
        saveNewLoginInfoRequest.setLoginId("jack123@gmail.com");

        var response = userController.saveNewLoginInfo(saveNewLoginInfoRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }*/
    @Test
    public void userViewsNoteTest() {
        ViewNoteRequest viewNoteRequest = new ViewNoteRequest();
        viewNoteRequest.setUsername("jack123");
        viewNoteRequest.setTitle("ideas");

        var response = userController.viewNote(viewNoteRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void userViewsNonExistentNoteTest() {
        ViewNoteRequest viewNoteRequest = new ViewNoteRequest();
        viewNoteRequest.setUsername("jack123");
        viewNoteRequest.setTitle("new ideas");

        var response = userController.viewNote(viewNoteRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void nonExistentUserViewsNoteTest() {
        ViewNoteRequest viewNoteRequest = new ViewNoteRequest();
        viewNoteRequest.setUsername("jim456");
        viewNoteRequest.setTitle("ideas");

        var response = userController.viewNote(viewNoteRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void userDeletesNote() {
        DeleteNoteRequest deleteNoteRequest = new DeleteNoteRequest();
        deleteNoteRequest.setUsername("jack123");
        deleteNoteRequest.setTitle("ideas");
        deleteNoteRequest.setMasterPassword("Password123.");

        var response = userController.deleteNote(deleteNoteRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void nonExistentUserDeletesNoteTest() {
        DeleteNoteRequest deleteNoteRequest = new DeleteNoteRequest();
        deleteNoteRequest.setUsername("jim456");
        deleteNoteRequest.setTitle("ideas");
        deleteNoteRequest.setMasterPassword("Password123.");

        var response = userController.deleteNote(deleteNoteRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void userDeletesNonExistentNoteTest() {
        DeleteNoteRequest deleteNoteRequest = new DeleteNoteRequest();
        deleteNoteRequest.setUsername("jack123");
        deleteNoteRequest.setTitle("new ideas");
        deleteNoteRequest.setMasterPassword("Password123.");

        var response = userController.deleteNote(deleteNoteRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }


}