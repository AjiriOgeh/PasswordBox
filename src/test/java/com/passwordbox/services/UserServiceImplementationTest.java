package com.passwordbox.services;

import com.passwordbox.data.models.User;
import com.passwordbox.data.repositories.*;
import com.passwordbox.dataTransferObjects.requests.*;
import com.passwordbox.dataTransferObjects.responses.*;
import com.passwordbox.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceImplementationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VaultRepository vaultRepository;

    @Autowired
    private LoginInfoRepository loginInfoRepository;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private CreditCardRepository creditCardRepository;

    @BeforeEach
    public void setUp() throws Exception {
        userRepository.deleteAll();
        vaultRepository.deleteAll();
        loginInfoRepository.deleteAll();
        noteRepository.deleteAll();
        creditCardRepository.deleteAll();

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("jack123");
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Password123.");
        userService.signUp(registerRequest);

        SaveNewLoginInfoRequest saveNewLoginInfoRequest = new SaveNewLoginInfoRequest();
        saveNewLoginInfoRequest.setUsername("jack123");
        saveNewLoginInfoRequest.setTitle("gmail login");
        saveNewLoginInfoRequest.setWebsite("www.gmail.com");
        saveNewLoginInfoRequest.setLoginId("jack123@gmail.com");
        userService.saveNewLoginInfo(saveNewLoginInfoRequest);

        CreateNoteRequest createNoteRequest = new CreateNoteRequest();
        createNoteRequest.setUsername("jack123");
        createNoteRequest.setTitle("ideas");
        createNoteRequest.setContent("build an AI assistant for projects");
        userService.createNote(createNoteRequest);

        SaveCreditCardRequest saveCreditCardRequest = new SaveCreditCardRequest();
        saveCreditCardRequest.setUsername("jack123");
        saveCreditCardRequest.setTitle("gtb savings card");
        saveCreditCardRequest.setCardNumber("5399831619690403");
        saveCreditCardRequest.setExpiryDate("01/2025");
        saveCreditCardRequest.setPin("1234");
        saveCreditCardRequest.setCVV("567");
        saveCreditCardRequest.setAdditionalInformation("for personal use");
        userService.saveCreditCard(saveCreditCardRequest);
    }

    @Test
    public void userSignsUp_UserIsSavedTest() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("jim456");
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Password123.");
        RegisterResponse jackRegisterResponse = userService.signUp(registerRequest);

        assertEquals(2, userRepository.count());
        assertEquals("jim456", jackRegisterResponse.getUsername());
    }

    @Test
    public void multipleUsersSignUp_UsersAreSavedTest() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("jim123");
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Password123.");
        RegisterResponse jimRegisterResponse = userService.signUp(registerRequest);

        registerRequest.setUsername("james456");
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Password123.");
        RegisterResponse jamesRegisterResponse = userService.signUp(registerRequest);

        assertEquals(3, userRepository.count());
        assertEquals("jim123", jimRegisterResponse.getUsername());
        assertEquals("james456", jamesRegisterResponse.getUsername());
    }

    @Test
    public void userSignsUp_UsernameFieldIsNull_ThrowsExceptionTest() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername(null);
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Password123.");

        assertThrows(IllegalArgumentException.class, ()->userService.signUp(registerRequest));
    }

    @Test
    public void userSignsUp_UsernameFieldIsEmpty_ThrowsExceptionTest() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("");
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Password123.");

        assertThrows(IllegalArgumentException.class, ()->userService.signUp(registerRequest));
    }

    @Test
    public void userSignsUp_UsernameField_ContainsSpaceCharacter_ThrowsExceptionTest() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("jim 123");
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Password123.");

        assertThrows(IllegalArgumentException.class, ()->userService.signUp(registerRequest));
    }

    @Test
    public void userSignsUp_UsernameExists_ThrowsException() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("jack123");
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Password123.");

        assertThrows(UsernameExistsException.class, ()->userService.signUp(registerRequest));
    }

    @Test
    public void userSignsUp_MasterPasswordFieldIsNull_ThrowsExceptionTest() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("jim123");
        registerRequest.setMasterPassword(null);
        registerRequest.setConfirmMasterPassword(null);

        assertThrows(IllegalArgumentException.class, ()->userService.signUp(registerRequest));
    }

    @Test
    public void userSignsUp_MasterPasswordFieldIsEmpty_ThrowsExceptionTest() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("jim123");
        registerRequest.setMasterPassword("");
        registerRequest.setConfirmMasterPassword("");

        assertThrows(IllegalArgumentException.class, ()->userService.signUp(registerRequest));
    }

    @Test
    public void userSignsUp_MasterPassword_DoesNotMatch_ConfirmPassword_ThrowsExceptionTest() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("jim123");
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Non matching password 456.");

        assertThrows(IllegalArgumentException.class, ()->userService.signUp(registerRequest));
    }

    @Test
    public void userSignsUp_PasswordLengthIsLessThan10Characters_ThrowsExceptionTest() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("jim123");
        registerRequest.setMasterPassword("Word123.");
        registerRequest.setConfirmMasterPassword("Word123.");

        assertThrows(IllegalArgumentException.class, ()->userService.signUp(registerRequest));
    }

    @Test
    public void uerLogsOutTest() {
        LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setUsername("jack123");
        LogoutResponse jackLogoutResponse = userService.logout(logoutRequest);

        User jackSafeBox = userRepository.findByUsername("jack123");

        assertTrue(jackSafeBox.isLocked());
        assertEquals("jack123", jackLogoutResponse.getUsername());
    }

    @Test
    public void nonExistentUserLogsOut_ThrowsExceptionTest() {
        LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setUsername("jim456");

        assertThrows(UserNotFoundException.class, ()->userService.logout(logoutRequest));
    }

    @Test
    public void userLogsOut_UserLogsInTest() {
        LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setUsername("jack123");
        LogoutResponse jackLogoutResponse = userService.logout(logoutRequest);

        User jackSafeBox = userRepository.findByUsername("jack123");

        assertTrue(jackSafeBox.isLocked());
        assertEquals("jack123", jackLogoutResponse.getUsername());

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("jack123");
        loginRequest.setPassword("Password123.");
        LoginResponse jackLoginResponse = userService.login(loginRequest);

        jackSafeBox = userRepository.findByUsername("jack123");

        assertFalse(jackSafeBox.isLocked());
        assertEquals("jack123", jackLoginResponse.getUsername());
    }

    @Test
    public void nonExistentUserLogsIn_ThrowsExceptionTest() {
        LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setUsername("jack123");
        LogoutResponse jackLogoutResponse = userService.logout(logoutRequest);

        User jackSafeBox = userRepository.findByUsername("jack123");

        assertTrue(jackSafeBox.isLocked());
        assertEquals("jack123", jackLogoutResponse.getUsername());

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("jim456");
        loginRequest.setPassword("Password123.");

        assertThrows(UserNotFoundException.class, ()->userService.login(loginRequest));
    }

    @Test
    public void userLogsInWith_InvalidPassword_ThrowsExceptionTest() {
        LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setUsername("jack123");
        LogoutResponse jackLogoutResponse = userService.logout(logoutRequest);

        User jackSafeBox = userRepository.findByUsername("jack123");

        assertTrue(jackSafeBox.isLocked());
        assertEquals("jack123", jackLogoutResponse.getUsername());

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("jack123");
        loginRequest.setPassword("invalid password");

        assertThrows(InvalidPasswordException.class, ()->userService.login(loginRequest));
    }

    @Test
    public void userSavesLoginInfoTest() throws Exception {
        SaveNewLoginInfoRequest saveNewLoginInfoRequest = new SaveNewLoginInfoRequest();
        saveNewLoginInfoRequest.setUsername("jack123");
        saveNewLoginInfoRequest.setTitle("yahoo login");
        saveNewLoginInfoRequest.setWebsite("www.yahoo.com");
        saveNewLoginInfoRequest.setLoginId("jack123@yahoo.com");
        SaveNewLoginInfoResponse saveNewLoginInfoResponse = userService.saveNewLoginInfo(saveNewLoginInfoRequest);

        User jackSafeBox = userRepository.findByUsername("jack123");

        assertEquals(1, vaultRepository.count());
        assertEquals(2, loginInfoRepository.count());
        assertEquals(2, jackSafeBox.getVault().getLoginInfos().size());
        assertEquals("jack123@yahoo.com", jackSafeBox.getVault().getLoginInfos().get(1).getLoginId());
        assertEquals("yahoo login", saveNewLoginInfoResponse.getTitle());
        assertEquals("www.yahoo.com", saveNewLoginInfoResponse.getWebsite());
        assertEquals("jack123@yahoo.com", loginInfoRepository.findAll().get(1).getLoginId());
    }

    @Test
    public void userLogsOut_SavesLoginInfo_ThrowsExceptionTest() {
        LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setUsername("jack123");
        LogoutResponse jackLogoutResponse = userService.logout(logoutRequest);

        User jackSafeBox = userRepository.findByUsername("jack123");

        assertTrue(jackSafeBox.isLocked());
        assertEquals("jack123", jackLogoutResponse.getUsername());

        SaveNewLoginInfoRequest saveNewLoginInfoRequest = new SaveNewLoginInfoRequest();
        saveNewLoginInfoRequest.setUsername("jack123");
        saveNewLoginInfoRequest.setTitle("yahoo login");
        saveNewLoginInfoRequest.setWebsite("www.yahoo.com");
        saveNewLoginInfoRequest.setLoginId("jack123@yahoo.com");

        assertThrows(ProfileLockStateException.class, ()->userService.saveNewLoginInfo(saveNewLoginInfoRequest));
    }


    @Test
    public void nonExistentUser_SavesLoginInfo_ThrowsExceptionTest() {
        SaveNewLoginInfoRequest saveNewLoginInfoRequest = new SaveNewLoginInfoRequest();
        saveNewLoginInfoRequest.setUsername("jim123");
        saveNewLoginInfoRequest.setTitle("gmail login");
        saveNewLoginInfoRequest.setWebsite("www.gmail.com");
        saveNewLoginInfoRequest.setLoginId("jim123@gmail.com");

        assertThrows(UserNotFoundException.class, ()->userService.saveNewLoginInfo(saveNewLoginInfoRequest));
    }


    @Test
    public void userSavesLoginInfo_TitleFieldIsNull_ThrowsExceptionTest() {
        SaveNewLoginInfoRequest saveNewLoginInfoRequest = new SaveNewLoginInfoRequest();
        saveNewLoginInfoRequest.setUsername("jack123");
        saveNewLoginInfoRequest.setTitle(null);
        saveNewLoginInfoRequest.setWebsite("www.gmail.com");
        saveNewLoginInfoRequest.setLoginId("jack123@gmail.com");

        assertThrows(IllegalArgumentException.class, ()->userService.saveNewLoginInfo(saveNewLoginInfoRequest));
    }

    @Test
    public void userSavesLoginInfo_TitleFieldIsEmptyTest() {
        SaveNewLoginInfoRequest saveNewLoginInfoRequest = new SaveNewLoginInfoRequest();
        saveNewLoginInfoRequest.setUsername("jack123");
        saveNewLoginInfoRequest.setTitle("");
        saveNewLoginInfoRequest.setWebsite("www.gmail.com");
        saveNewLoginInfoRequest.setLoginId("jack123@gmail.com");

        assertThrows(IllegalArgumentException.class, ()->userService.saveNewLoginInfo(saveNewLoginInfoRequest));
    }

    @Test
    public void userSavesLoginInfo_TitleExistsInUserList_ThrowsExceptionTest() {
        SaveNewLoginInfoRequest saveNewLoginInfoRequest = new SaveNewLoginInfoRequest();
        saveNewLoginInfoRequest.setUsername("jack123");
        saveNewLoginInfoRequest.setTitle("gmail login");
        saveNewLoginInfoRequest.setWebsite("www.gmail.com");
        saveNewLoginInfoRequest.setLoginId("jack123@gmail.com");

        assertThrows(IllegalArgumentException.class, ()->userService.saveNewLoginInfo(saveNewLoginInfoRequest));
    }

    @Test
    public void userEditLoginInfoTest() throws Exception {
        EditLoginInfoRequest editLoginInfoRequest = new EditLoginInfoRequest();
        editLoginInfoRequest.setUsername("jack123");
        editLoginInfoRequest.setTitle("gmail login");
        editLoginInfoRequest.setEditedTitle("yahoo login");
        editLoginInfoRequest.setEditedWebsite("www.yahoo.com");
        editLoginInfoRequest.setEditedLoginId("jack123@yahoo.com");
        editLoginInfoRequest.setEditedPassword("password");
        EditLoginInfoResponse editLoginInfoResponse = userService.editLoginInfo(editLoginInfoRequest);

        User jackSafeBox = userRepository.findByUsername("jack123");

        assertEquals(1, loginInfoRepository.count());
        assertEquals(1, jackSafeBox.getVault().getLoginInfos().size());
        assertEquals("jack123@yahoo.com", jackSafeBox.getVault().getLoginInfos().getFirst().getLoginId());
        assertEquals("yahoo login", editLoginInfoResponse.getTitle());
        assertEquals("jack123@yahoo.com", loginInfoRepository.findAll().getFirst().getLoginId());
    }

    @Test
    public void nonExistentUser_EditsLoginInfo_ThrowsExceptionTest() {
        EditLoginInfoRequest editLoginInfoRequest = new EditLoginInfoRequest();
        editLoginInfoRequest.setUsername("jim123456");
        editLoginInfoRequest.setTitle("gmail login");
        editLoginInfoRequest.setEditedTitle("yahoo mail");
        editLoginInfoRequest.setEditedWebsite("www.yahoo.com");
        editLoginInfoRequest.setEditedLoginId("jim123@yahoo.com");
        editLoginInfoRequest.setEditedPassword("password");

        assertThrows(UserNotFoundException.class, ()->userService.editLoginInfo(editLoginInfoRequest));
    }

    @Test
    public void userLogsOut_EditsLoginInfo_ThrowExceptionTest() {        LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setUsername("jack123");
        LogoutResponse jackLogoutResponse = userService.logout(logoutRequest);

        User jackSafeBox = userRepository.findByUsername("jack123");

        assertTrue(jackSafeBox.isLocked());
        assertEquals("jack123", jackLogoutResponse.getUsername());

        EditLoginInfoRequest editLoginInfoRequest = new EditLoginInfoRequest();
        editLoginInfoRequest.setUsername("jack123");
        editLoginInfoRequest.setTitle("gmail login");
        editLoginInfoRequest.setEditedTitle("yahoo login");
        editLoginInfoRequest.setEditedWebsite("www.yahoo.com");
        editLoginInfoRequest.setEditedLoginId("jack123@yahoo.com");
        editLoginInfoRequest.setEditedPassword("password");

        assertThrows(ProfileLockStateException.class, ()->userService.editLoginInfo(editLoginInfoRequest));
    }

    @Test
    public void userEdits_NonExistentLoginInfo_ThrowExceptionTest() {
        EditLoginInfoRequest editLoginInfoRequest = new EditLoginInfoRequest();
        editLoginInfoRequest.setUsername("jack123");
        editLoginInfoRequest.setTitle("hotmail login");
        editLoginInfoRequest.setEditedTitle("yahoo mail Login");
        editLoginInfoRequest.setEditedWebsite("www.yahoo.com");
        editLoginInfoRequest.setEditedLoginId("jack123@yahoo.com");
        editLoginInfoRequest.setEditedPassword("password");

        assertThrows(LoginInfoNotFoundException.class, ()->userService.editLoginInfo(editLoginInfoRequest));
    }

//    @Test
//    public void userViewsLoginInfoTest() throws Exception {
//        ViewLoginInfoRequest viewLoginInfoRequest = new ViewLoginInfoRequest();
//        viewLoginInfoRequest.setUsername("jack123");
//        viewLoginInfoRequest.setTitle("gmail login");
//        ViewLoginInfoResponse viewLoginInfoResponse = userService.viewLoginInfo(viewLoginInfoRequest);
//
//        assertEquals("jack123@gmail.com", viewLoginInfoResponse.getLoginId());
//        assertEquals("www.gmail.com", viewLoginInfoResponse.getWebsite());
//    }

    @Test
    public void nonExistentUser_ViewsLoginInfo_ThrowsExceptionTest() {
        ViewLoginInfoRequest viewLoginInfoRequest = new ViewLoginInfoRequest();
        viewLoginInfoRequest.setUsername("jim456");
        viewLoginInfoRequest.setTitle("gmail login");

        assertThrows(UserNotFoundException.class, ()->userService.viewLoginInfo(viewLoginInfoRequest));
    }

    @Test
    public void userViews_NonExistentLoginInfo_ThrowsExceptionTest() {
        ViewLoginInfoRequest viewLoginInfoRequest = new ViewLoginInfoRequest();
        viewLoginInfoRequest.setUsername("jack123");
        viewLoginInfoRequest.setTitle("yahoo login");
        assertThrows(LoginInfoNotFoundException.class, ()->userService.viewLoginInfo(viewLoginInfoRequest));

    }

    @Test
    public void userLogsOut_UserViewsLoginInfo_ThrowsExceptionTest() {
        LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setUsername("jack123");
        LogoutResponse jackLogoutResponse = userService.logout(logoutRequest);

        User jackSafeBox = userRepository.findByUsername("jack123");

        assertTrue(jackSafeBox.isLocked());
        assertEquals("jack123", jackLogoutResponse.getUsername());

        ViewLoginInfoRequest viewLoginInfoRequest = new ViewLoginInfoRequest();
        viewLoginInfoRequest.setUsername("jack123");
        viewLoginInfoRequest.setTitle("gmail login");

        assertThrows(ProfileLockStateException.class, ()->userService.viewLoginInfo(viewLoginInfoRequest));
    }

    @Test
    public void userSavesLoginInfo_UserDeletesLoginInfoTest() {
        DeleteLoginInfoRequest deleteLoginInfoRequest = new DeleteLoginInfoRequest();
        deleteLoginInfoRequest.setUsername("jack123");
        deleteLoginInfoRequest.setTitle("gmail login");
        deleteLoginInfoRequest.setMasterPassword("Password123.");
        DeleteLoginInfoResponse deleteLoginInfoResponse = userService.deleteLoginInfo(deleteLoginInfoRequest);

        User jackSafeBox = userRepository.findByUsername("jack123");

        assertEquals(0, loginInfoRepository.count());
        assertEquals(0, jackSafeBox.getVault().getLoginInfos().size());
        assertEquals(0, vaultRepository.findAll().getFirst().getLoginInfos().size());
        assertEquals("gmail login", deleteLoginInfoResponse.getTitle());
    }

        @Test
    public void userLogsOut_DeletesLoginInfo_ThrowExceptionTest() {
        LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setUsername("jack123");
        LogoutResponse jackLogoutResponse = userService.logout(logoutRequest);

        User jackSafeBox = userRepository.findByUsername("jack123");

        assertTrue(jackSafeBox.isLocked());
        assertEquals("jack123", jackLogoutResponse.getUsername());

        DeleteLoginInfoRequest deleteLoginInfoRequest = new DeleteLoginInfoRequest();
        deleteLoginInfoRequest.setUsername("jack123");
        deleteLoginInfoRequest.setTitle("gmail login");
        deleteLoginInfoRequest.setMasterPassword("Password123.");

        assertThrows(ProfileLockStateException.class, ()->userService.deleteLoginInfo(deleteLoginInfoRequest));
    }

    @Test
    public void nonExistentUser_DeletesLoginInfo_ThrowExceptionTest() {
        DeleteLoginInfoRequest deleteLoginInfoRequest = new DeleteLoginInfoRequest();
        deleteLoginInfoRequest.setUsername("jim456");
        deleteLoginInfoRequest.setTitle("gmail login");
        deleteLoginInfoRequest.setMasterPassword("Password123.");

        assertThrows(UserNotFoundException.class, ()->userService.deleteLoginInfo(deleteLoginInfoRequest));
    }

    @Test
    public void userDeletesLoginInfo_PasswordIsInvalid_ThrowExceptionTest() {
        DeleteLoginInfoRequest deleteLoginInfoRequest = new DeleteLoginInfoRequest();
        deleteLoginInfoRequest.setUsername("jack123");
        deleteLoginInfoRequest.setTitle("gmail login");
        deleteLoginInfoRequest.setMasterPassword("invalid password");

        assertThrows(InvalidPasswordException.class, ()->userService.deleteLoginInfo(deleteLoginInfoRequest));
    }

    @Test
    public void userDeletes_NonExistentLoginInfo_ThrowExceptionTest() {
        DeleteLoginInfoRequest deleteLoginInfoRequest = new DeleteLoginInfoRequest();
        deleteLoginInfoRequest.setUsername("jack123");
        deleteLoginInfoRequest.setTitle("yahoo login");
        deleteLoginInfoRequest.setMasterPassword("Password123.");

        assertThrows(LoginInfoNotFoundException.class, ()->userService.deleteLoginInfo(deleteLoginInfoRequest));
    }

    @Test
    public void userCreatesNoteTest() throws Exception {
        CreateNoteRequest createNoteRequest = new CreateNoteRequest();
        createNoteRequest.setUsername("jack123");
        createNoteRequest.setTitle("secrets");
        createNoteRequest.setContent("i cannot tell anyone.");
        CreateNoteResponse createNoteResponse = userService.createNote(createNoteRequest);

        User jackSafeBox = userRepository.findByUsername("jack123");

        assertEquals(1, vaultRepository.count());
        assertEquals(2, noteRepository.count());
        assertEquals(2, jackSafeBox.getVault().getNotes().size());
        assertEquals("secrets", jackSafeBox.getVault().getNotes().get(1).getTitle());
        assertEquals("i cannot tell anyone.", createNoteResponse.getContent());
        assertEquals("secrets", noteRepository.findAll().get(1).getTitle());
    }


    @Test
    public void userSignsUp_UserLogsOuts_CreatesNote_ThrowsExceptionTest() {
        LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setUsername("jack123");
        LogoutResponse jackLogoutResponse = userService.logout(logoutRequest);

        User jackSafeBox = userRepository.findByUsername("jack123");

        assertTrue(jackSafeBox.isLocked());
        assertEquals("jack123", jackLogoutResponse.getUsername());

        CreateNoteRequest createNoteRequest = new CreateNoteRequest();
        createNoteRequest.setUsername("jack123");
        createNoteRequest.setTitle("secrets");
        createNoteRequest.setContent("i cannot tell anyone");

        assertThrows(ProfileLockStateException.class, ()->userService.createNote(createNoteRequest));
    }

    @Test
    public void nonExistentUser_CreatesNote_ThrowsExceptionTest() {
        CreateNoteRequest createNoteRequest = new CreateNoteRequest();
        createNoteRequest.setUsername("jim456");
        createNoteRequest.setTitle("ideas");
        createNoteRequest.setContent("Build an AI assistant for projects");

        assertThrows(UserNotFoundException.class, ()->userService.createNote(createNoteRequest));
    }

    @Test
    public void userCreatesNote_TitleFieldIsNull_ThrowsExceptionTest() {
        CreateNoteRequest createNoteRequest = new CreateNoteRequest();
        createNoteRequest.setUsername("jack123");
        createNoteRequest.setTitle(null);
        createNoteRequest.setContent("Build an AI assistant for projects");

        assertThrows(IllegalArgumentException.class, ()->userService.createNote(createNoteRequest));
    }

    @Test
    public void userCreatesNote_TitleFieldIsEmpty_ThrowsExceptionTest() {
        CreateNoteRequest createNoteRequest = new CreateNoteRequest();
        createNoteRequest.setUsername("jack123");
        createNoteRequest.setTitle("");
        createNoteRequest.setContent("Build an AI assistant for projects");

        assertThrows(IllegalArgumentException.class, ()->userService.createNote(createNoteRequest));
    }

    @Test
    public void userSignsUp_UserCreatesNote_WithExistingTitle_ThrowsExceptionTest() {
        CreateNoteRequest createNoteRequest = new CreateNoteRequest();
        createNoteRequest.setUsername("jack123");
        createNoteRequest.setTitle("ideas");
        createNoteRequest.setContent("Create algorithm for flying robot");

        assertThrows(IllegalArgumentException.class, ()->userService.createNote(createNoteRequest));
    }

    @Test
    public void userSignsUp_UserCreatesNote_UserEditsNoteTest() throws Exception {
        EditNoteRequest editNoteRequest = new EditNoteRequest();
        editNoteRequest.setUsername("jack123");
        editNoteRequest.setTitle("ideas");
        editNoteRequest.setEditedTitle("new ideas");
        editNoteRequest.setEditedContent("Build Robots for heart surgeries");
        EditNoteResponse editNoteResponse = userService.editNote(editNoteRequest);

        User jackSafeBox = userRepository.findByUsername("jack123");

        assertEquals(1, vaultRepository.count());
        assertEquals(1, noteRepository.count());
        assertEquals(1, jackSafeBox.getVault().getNotes().size());
        assertEquals("new ideas", jackSafeBox.getVault().getNotes().getFirst().getTitle());
        assertEquals("Build Robots for heart surgeries", editNoteResponse.getContent());
        assertEquals("new ideas", noteRepository.findAll().getFirst().getTitle());
    }

    @Test
    public void userLogsOut_UserEditsNote_ThrowsExceptionTest() {
        LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setUsername("jack123");
        LogoutResponse jackLogoutResponse = userService.logout(logoutRequest);

        User jackSafeBox = userRepository.findByUsername("jack123");

        assertTrue(jackSafeBox.isLocked());
        assertEquals("jack123", jackLogoutResponse.getUsername());

        EditNoteRequest editNoteRequest = new EditNoteRequest();
        editNoteRequest.setUsername("jack123");
        editNoteRequest.setTitle("ideas");
        editNoteRequest.setEditedTitle("new ideas");
        editNoteRequest.setEditedContent("Build Robots for heart surgeries");

        assertThrows(ProfileLockStateException.class, () -> userService.editNote(editNoteRequest));
    }

    @Test
    public void userSignsUp_CreatesNote_NonExistentUserEditsNote_ThrowsExceptionTest() {
        EditNoteRequest editNoteRequest = new EditNoteRequest();
        editNoteRequest.setUsername("jim456");
        editNoteRequest.setTitle("ideas");
        editNoteRequest.setEditedTitle("new ideas");
        editNoteRequest.setEditedContent("Build Robots for heart surgeries");

        assertThrows(UserNotFoundException.class, ()-> userService.editNote(editNoteRequest));
    }

    @Test
    public void userCreatesNote_UserEditsNote_TitleIsEmpty_ThrowsExceptionTest() {
        EditNoteRequest editNoteRequest = new EditNoteRequest();
        editNoteRequest.setUsername("jack123");
        editNoteRequest.setTitle("ideas");
        editNoteRequest.setEditedTitle("");
        editNoteRequest.setEditedContent("Build Robots for heart surgeries");

        assertThrows(IllegalArgumentException.class, ()-> userService.editNote(editNoteRequest));
    }

    @Test
    public void userCreatesNote_UserEditsNote_TitleIsNull_ThrowsExceptionTest() {
        EditNoteRequest editNoteRequest = new EditNoteRequest();
        editNoteRequest.setUsername("jack123");
        editNoteRequest.setTitle("ideas");
        editNoteRequest.setEditedTitle(null);
        editNoteRequest.setEditedContent("Build Robots for heart surgeries");

        assertThrows(IllegalArgumentException.class, ()-> userService.editNote(editNoteRequest));
    }

    @Test
    public void userCreatesNote_UserViewsNoteTest() throws Exception {
        ViewNoteRequest viewNoteRequest = new ViewNoteRequest();
        viewNoteRequest.setUsername("jack123");
        viewNoteRequest.setTitle("ideas");
        ViewNoteResponse viewNoteResponse = userService.viewNote(viewNoteRequest);

        assertEquals("ideas", viewNoteResponse.getTitle());
        assertEquals("build an AI assistant for projects", viewNoteResponse.getContent());
    }

    @Test
    public void userLogsOut_UserViewsNote_ThrowsExceptionTestTest() {
        LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setUsername("jack123");
        LogoutResponse jackLogoutResponse = userService.logout(logoutRequest);

        User jackSafeBox = userRepository.findByUsername("jack123");

        assertTrue(jackSafeBox.isLocked());
        assertEquals("jack123", jackLogoutResponse.getUsername());

        ViewNoteRequest viewNoteRequest = new ViewNoteRequest();
        viewNoteRequest.setUsername("jack123");
        viewNoteRequest.setTitle("ideas");

        assertThrows(ProfileLockStateException.class, ()->userService.viewNote(viewNoteRequest));
    }

    @Test
    public void nonExistentUserViewsNote_ThrowsExceptionTest() {
        ViewNoteRequest viewNoteRequest = new ViewNoteRequest();
        viewNoteRequest.setUsername("jim123");
        viewNoteRequest.setTitle("ideas");

        assertThrows(UserNotFoundException.class, ()->userService.viewNote(viewNoteRequest));
    }

    @Test
    public void userViewsNonExistentNote_ThrowsExceptionTest() {
        ViewNoteRequest viewNoteRequest = new ViewNoteRequest();
        viewNoteRequest.setUsername("jack123");
        viewNoteRequest.setTitle("new ideas");

        assertThrows(NoteNotFoundException.class, ()->userService.viewNote(viewNoteRequest));
    }

    @Test
    public void userCreatesNote_UserDeletesNoteTest() {
        DeleteNoteRequest deleteNoteRequest = new DeleteNoteRequest();
        deleteNoteRequest.setUsername("jack123");
        deleteNoteRequest.setTitle("ideas");
        deleteNoteRequest.setMasterPassword("Password123.");
        DeleteNoteResponse deleteNoteResponse = userService.deleteNote(deleteNoteRequest);

        User jackSafeBox = userRepository.findByUsername("jack123");

        assertEquals(0, noteRepository.count());
        assertEquals(0, jackSafeBox.getVault().getNotes().size());
        assertEquals(0, vaultRepository.findAll().getFirst().getNotes().size());
        assertEquals("ideas", deleteNoteResponse.getTitle());
    }

    @Test
    public void userCreatesNote_UserLogsOutDeletesNote_ThrowsExceptionTest() {
        LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setUsername("jack123");
        LogoutResponse jackLogoutResponse = userService.logout(logoutRequest);

        User jackSafeBox = userRepository.findByUsername("jack123");

        assertTrue(jackSafeBox.isLocked());
        assertEquals("jack123", jackLogoutResponse.getUsername());

        DeleteNoteRequest deleteNoteRequest = new DeleteNoteRequest();
        deleteNoteRequest.setUsername("jack123");
        deleteNoteRequest.setTitle("ideas");
        deleteNoteRequest.setMasterPassword("Password123.");

        assertThrows(ProfileLockStateException.class, ()->userService.deleteNote(deleteNoteRequest));
    }

    @Test
    public void userDeletesNonExistentNote_ThrowsExceptionTest() {
        DeleteNoteRequest deleteNoteRequest = new DeleteNoteRequest();
        deleteNoteRequest.setUsername("jack123");
        deleteNoteRequest.setTitle("new ideas");
        deleteNoteRequest.setMasterPassword("Password123.");

        assertThrows(NoteNotFoundException.class, ()->userService.deleteNote(deleteNoteRequest));
    }

    @Test
    public void userDeletesNote_PasswordIsInvalid_ThrowsExceptionTest() {
        DeleteNoteRequest deleteNoteRequest = new DeleteNoteRequest();
        deleteNoteRequest.setUsername("jack123");
        deleteNoteRequest.setTitle("ideas");
        deleteNoteRequest.setMasterPassword("invalid password");

        assertThrows(InvalidPasswordException.class, ()->userService.deleteNote(deleteNoteRequest));
    }

    @Test
    public void userGeneratesPasswordTest() {
        GeneratePasswordRequest generatePasswordRequest = new GeneratePasswordRequest();
        generatePasswordRequest.setLength("12");
        GeneratePasswordResponse generatePasswordResponse = userService.generatePassword(generatePasswordRequest);

        assertEquals(12, generatePasswordResponse.getLength());
        assertNotNull(generatePasswordResponse.getPassword());
    }

    @Test
    public void userGeneratesPassword_LengthIsNotANumber_ThrowsExceptionTest() {
        GeneratePasswordRequest generatePasswordRequest = new GeneratePasswordRequest();
        generatePasswordRequest.setLength("A");

        assertThrows(InvalidPasscodeLengthException.class, ()->userService.generatePassword(generatePasswordRequest));
    }

    @Test
    public void userGeneratesPassword_NumberIsNegative_ThrowsException() {
        GeneratePasswordRequest generatePasswordRequest = new GeneratePasswordRequest();
        generatePasswordRequest.setLength("-1");

        assertThrows(InvalidPasscodeLengthException.class, ()->userService.generatePassword(generatePasswordRequest));
    }

    @Test
    public void userGeneratesPassword_NumberIsGreaterThan30_ThrowsException() {
        GeneratePasswordRequest generatePasswordRequest = new GeneratePasswordRequest();
        generatePasswordRequest.setLength("31");

        assertThrows(InvalidPasscodeLengthException.class, ()->userService.generatePassword(generatePasswordRequest));
    }

    @Test
    public void userGeneratesPinTest() {
        GeneratePinRequest generatePinRequest = new GeneratePinRequest();
        generatePinRequest.setLength("8");
        GeneratePinResponse generatePinResponse = userService.generatePin(generatePinRequest);

        assertEquals(8, generatePinResponse.getLength());
        assertNotNull(generatePinResponse.getPin());
    }

    @Test
    public void userGeneratesPin_LengthIsNegative_ThrowsExceptionTest() {
        GeneratePinRequest generatePinRequest = new GeneratePinRequest();
        generatePinRequest.setLength("-1");

        assertThrows(InvalidPasscodeLengthException.class, ()->userService.generatePin(generatePinRequest));
    }

    @Test
    public void userGeneratesPin_LengthIsNotANumber_ThrowsExceptionTest() {
        GeneratePinRequest generatePinRequest = new GeneratePinRequest();
        generatePinRequest.setLength("B");

        assertThrows(InvalidPasscodeLengthException.class, ()->userService.generatePin(generatePinRequest));
    }

    @Test
    public void userGeneratesPin_LengthIsLessThan1_ThrowsExceptionTest() {
        GeneratePinRequest generatePinRequest = new GeneratePinRequest();
        generatePinRequest.setLength("0");

        assertThrows(InvalidPasscodeLengthException.class, ()->userService.generatePin(generatePinRequest));
    }

    @Test
    public void userSavesCreditCardTest() throws Exception {
        SaveCreditCardRequest saveCreditCardRequest = new SaveCreditCardRequest();
        saveCreditCardRequest.setUsername("jack123");
        saveCreditCardRequest.setTitle("zenith savings card");
        saveCreditCardRequest.setCardNumber("5399831619690403");
        saveCreditCardRequest.setExpiryDate("01/2026");
        saveCreditCardRequest.setPin("0987");
        saveCreditCardRequest.setCVV("123");
        saveCreditCardRequest.setAdditionalInformation("for personal use");
        SaveCreditCardResponse jackSaveCreditCardResponse = userService.saveCreditCard(saveCreditCardRequest);

        User jackSafeBox = userRepository.findByUsername("jack123");

        assertEquals(2, jackSafeBox.getVault().getCreditCards().size());
        assertEquals(2, creditCardRepository.count());
        assertEquals("zenith savings card", jackSafeBox.getVault().getCreditCards().get(1).getTitle());
        assertEquals("zenith savings card", jackSaveCreditCardResponse.getTitle());
    }

    @Test
    public void nonExistentUserSavesCreditCardTest() {
        SaveCreditCardRequest saveCreditCardRequest = new SaveCreditCardRequest();
        saveCreditCardRequest.setUsername("jim123");
        saveCreditCardRequest.setTitle("zenith savings card");
        saveCreditCardRequest.setCardNumber("5399831619690403");
        saveCreditCardRequest.setExpiryDate("01/2026");
        saveCreditCardRequest.setPin("0987");
        saveCreditCardRequest.setCVV("123");
        saveCreditCardRequest.setAdditionalInformation("for personal use");

        assertThrows(UserNotFoundException.class,()->userService.saveCreditCard(saveCreditCardRequest));
    }

    @Test
    public void userSavesCreditCard_TitleIsEmptyTest() {
        SaveCreditCardRequest saveCreditCardRequest = new SaveCreditCardRequest();
        saveCreditCardRequest.setUsername("jack123");
        saveCreditCardRequest.setTitle("");
        saveCreditCardRequest.setCardNumber("5399831619690403");
        saveCreditCardRequest.setExpiryDate("01/2026");
        saveCreditCardRequest.setPin("0987");
        saveCreditCardRequest.setCVV("123");
        saveCreditCardRequest.setAdditionalInformation("for personal use");

        assertThrows(IllegalArgumentException.class,()->userService.saveCreditCard(saveCreditCardRequest));
    }

    @Test
    public void userSavesCreditCard_CreditCardIsInvalidTest() {
        SaveCreditCardRequest saveCreditCardRequest = new SaveCreditCardRequest();
        saveCreditCardRequest.setUsername("jack123");
        saveCreditCardRequest.setTitle("");
        saveCreditCardRequest.setCardNumber("103831619690403");
        saveCreditCardRequest.setExpiryDate("01/2026");
        saveCreditCardRequest.setPin("0987");
        saveCreditCardRequest.setCVV("123");
        saveCreditCardRequest.setAdditionalInformation("for personal use");

        assertThrows(IllegalArgumentException.class,()->userService.saveCreditCard(saveCreditCardRequest));
    }

    @Test
    public void userEditsCreditCardTest() throws Exception {
        EditCreditCardRequest editCreditCardRequest = new EditCreditCardRequest();
        editCreditCardRequest.setUsername("jack123");
        editCreditCardRequest.setTitle("gtb savings card");
        editCreditCardRequest.setUpdateTitle("gtb current card");
        editCreditCardRequest.setUpdatedCardNumber("5399834719278582");
        editCreditCardRequest.setUpdatedCardNumber("01/2028");
        editCreditCardRequest.setUpdatedAdditionalInformation("for office use");
        EditCreditCardResponse jackEditCreditCardResponse = userService.editCreditCard(editCreditCardRequest);

        User jackSafeBox = userRepository.findByUsername("jack123");

        assertEquals(1, jackSafeBox.getVault().getCreditCards().size());
        assertEquals("gtb current card", jackSafeBox.getVault().getCreditCards().getFirst().getTitle());
        assertEquals(1, creditCardRepository.count());
        assertEquals(jackSafeBox.getVault().getCreditCards().getFirst().getId(), jackEditCreditCardResponse.getId());
    }

    @Test
    public void nonExistentUserEditsCreditCardTest() {
        EditCreditCardRequest editCreditCardRequest = new EditCreditCardRequest();
        editCreditCardRequest.setUsername("jim123");
        editCreditCardRequest.setTitle("gtb savings card");
        editCreditCardRequest.setUpdateTitle("gtb current card");
        editCreditCardRequest.setUpdatedCardNumber("5399831619690403");
        editCreditCardRequest.setUpdatedCardNumber("01/2028");
        editCreditCardRequest.setUpdatedAdditionalInformation("for office use");

        assertThrows(UserNotFoundException.class, ()->userService.editCreditCard(editCreditCardRequest));
    }
    @Test
    public void userEditsCreditCard_CardNumberIsInvalidTest() {
        EditCreditCardRequest editCreditCardRequest = new EditCreditCardRequest();
        editCreditCardRequest.setUsername("jim123");
        editCreditCardRequest.setTitle("gtb savings card");
        editCreditCardRequest.setUpdateTitle("gtb current card");
        editCreditCardRequest.setUpdatedCardNumber("012831619690403");
        editCreditCardRequest.setUpdatedCardNumber("01/2028");
        editCreditCardRequest.setUpdatedAdditionalInformation("for office use");

        assertThrows(UserNotFoundException.class, ()->userService.editCreditCard(editCreditCardRequest));
    }

    @Test
    public void userViewsCreditCardTest() throws Exception {
        ViewCreditCardRequest viewCreditCardRequest = new ViewCreditCardRequest();
        viewCreditCardRequest.setUsername("jack123");
        viewCreditCardRequest.setTitle("gtb savings card");

        ViewCreditCardResponse jackViewCreditCardResponse = userService.viewCreditCard(viewCreditCardRequest);
        assertEquals(1, creditCardRepository.count());
        assertEquals("gtb savings card", jackViewCreditCardResponse.getTitle());
    }

    @Test
    public void userViews_NonExistentCreditCardTest() {
        ViewCreditCardRequest viewCreditCardRequest = new ViewCreditCardRequest();
        viewCreditCardRequest.setUsername("jack123");
        viewCreditCardRequest.setTitle("uba savings card");

        assertThrows(CreditCardNotFoundException.class,()->userService.viewCreditCard(viewCreditCardRequest));
    }

    @Test
    public void userDeletesCreditCardTest(){
        DeleteCreditCardRequest deleteCreditCardRequest = new DeleteCreditCardRequest();
        deleteCreditCardRequest.setUsername("jack123");
        deleteCreditCardRequest.setTitle("gtb savings card");
        deleteCreditCardRequest.setMasterPassword("Password123.");
        DeleteCreditCardResponse jackDeleteCreditCardResponse = userService.deleteCreditCard(deleteCreditCardRequest);

        User jackSafeBox = userRepository.findByUsername("jack123");

        assertEquals(0, jackSafeBox.getVault().getCreditCards().size());
        assertEquals(0, vaultRepository.findAll().getFirst().getCreditCards().size());
        assertEquals(0, creditCardRepository.count());
        assertEquals("gtb savings card", jackDeleteCreditCardResponse.getTitle());
    }

    @Test
    public void nonExistentUserDeletesCreditCardTest(){
        DeleteCreditCardRequest deleteCreditCardRequest = new DeleteCreditCardRequest();
        deleteCreditCardRequest.setUsername("jim123");
        deleteCreditCardRequest.setTitle("gtb savings card");
        deleteCreditCardRequest.setMasterPassword("Password123.");

        assertThrows(UserNotFoundException.class, ()->userService.deleteCreditCard(deleteCreditCardRequest));
    }


}