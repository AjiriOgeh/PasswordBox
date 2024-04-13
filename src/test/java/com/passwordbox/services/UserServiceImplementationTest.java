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

    @Autowired
    private PassportRepository passportRepository;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
        vaultRepository.deleteAll();
        loginInfoRepository.deleteAll();
        noteRepository.deleteAll();
        creditCardRepository.deleteAll();
        passportRepository.deleteAll();
    }

    @Test
    public void userSignsUp_UserIsSavedTest() {
        assertEquals(0, userRepository.count());

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("jack123");
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Password123.");
        RegisterResponse jackRegisterResponse = userService.signUp(registerRequest);

        assertEquals(1, userRepository.count());
        assertEquals("jack123", jackRegisterResponse.getUsername());
    }

    @Test
    public void twoSignsUp_UsersAreSavedTest() {
        assertEquals(0, userRepository.count());

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("jack123");
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Password123.");
        RegisterResponse jackRegisterResponse = userService.signUp(registerRequest);

        registerRequest.setUsername("jim456");
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Password123.");
        RegisterResponse jimRegisterResponse = userService.signUp(registerRequest);

        assertEquals(2, userRepository.count());
        assertEquals("jack123", jackRegisterResponse.getUsername());
        assertEquals("jim456", jimRegisterResponse.getUsername());
    }

    @Test
    public void userSignsUp_UsernameFieldIsNull_ThrowsExceptionTest() {
        assertEquals(0, userRepository.count());

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername(null);
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Password123.");

        assertThrows(IllegalArgumentException.class, ()->userService.signUp(registerRequest));
    }

    @Test
    public void userSignsUp_UsernameFieldIsEmpty_ThrowsExceptionTest() {
        assertEquals(0, userRepository.count());

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("");
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Password123.");

        assertThrows(IllegalArgumentException.class, ()->userService.signUp(registerRequest));
    }

    @Test
    public void userSignsUp_UsernameField_ContainsSpaceCharacter_ThrowsExceptionTest() {
        assertEquals(0, userRepository.count());

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("jack 123");
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Password123.");

        assertThrows(IllegalArgumentException.class, ()->userService.signUp(registerRequest));
    }

    @Test
    public void userSignsUp_UsernameExists_ThrowsException() {
        assertEquals(0, userRepository.count());

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("jack123");
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Password123.");
        RegisterResponse jackRegisterResponse = userService.signUp(registerRequest);

        assertEquals(1, userRepository.count());
        assertEquals("jack123", jackRegisterResponse.getUsername());

        registerRequest.setUsername("jack123");
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Password123.");

        assertThrows(UsernameExistsException.class, ()->userService.signUp(registerRequest));
    }

    @Test
    public void userSignsUp_MasterPasswordFieldIsNull_ThrowsExceptionTest() {
        assertEquals(0, userRepository.count());

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("jack123");
        registerRequest.setMasterPassword(null);
        registerRequest.setConfirmMasterPassword(null);

        assertThrows(IllegalArgumentException.class, ()->userService.signUp(registerRequest));
    }

    @Test
    public void userSignsUp_MasterPasswordFieldIsEmpty_ThrowsExceptionTest() {
        assertEquals(0, userRepository.count());

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("jack123");
        registerRequest.setMasterPassword("");
        registerRequest.setConfirmMasterPassword("");

        assertThrows(IllegalArgumentException.class, ()->userService.signUp(registerRequest));
    }

    @Test
    public void userSignsUp_MasterPassword_DoesNotMatch_ConfirmPassword_ThrowsExceptionTest() {
        assertEquals(0, userRepository.count());

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("jack123");
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Non matching password 456.");

        assertThrows(IllegalArgumentException.class, ()->userService.signUp(registerRequest));
    }

    @Test
    public void userSignsUp_PasswordLengthIsLessThan10Characters_ThrowsExceptionTest() {
        assertEquals(0, userRepository.count());

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("jack123");
        registerRequest.setMasterPassword("Word123.");
        registerRequest.setConfirmMasterPassword("Word123.");

        assertThrows(IllegalArgumentException.class, ()->userService.signUp(registerRequest));
    }

    @Test
    public void userSignsUp_UserLogsOutTest() {
        assertEquals(0, userRepository.count());

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("jack123");
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Password123.");
        RegisterResponse jackRegisterResponse = userService.signUp(registerRequest);

        assertEquals(1, userRepository.count());
        assertEquals("jack123", jackRegisterResponse.getUsername());

        LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setUsername("jack123");
        LogoutResponse jackLogoutResponse = userService.logout(logoutRequest);

        User jackSafeBox = userRepository.findByUsername("jack123");

        assertTrue(jackSafeBox.isLocked());
        assertEquals("jack123", jackLogoutResponse.getUsername());
    }

    @Test
    public void userSignsUp_NonExistentUserLogsOut_ThrowsExceptionTest() {
        assertEquals(0, userRepository.count());

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("jack123");
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Password123.");
        RegisterResponse jackRegisterResponse = userService.signUp(registerRequest);

        assertEquals(1, userRepository.count());
        assertEquals("jack123", jackRegisterResponse.getUsername());

        LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setUsername("jim456");

        assertThrows(UserNotFoundException.class, ()->userService.logout(logoutRequest));
    }

    @Test
    public void userSignsUp_UserLogsOut_UserLogsInTest() {
        assertEquals(0, userRepository.count());

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("jack123");
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Password123.");
        RegisterResponse jackRegisterResponse = userService.signUp(registerRequest);

        assertEquals(1, userRepository.count());
        assertEquals("jack123", jackRegisterResponse.getUsername());

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
        assertEquals(0, userRepository.count());

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("jack123");
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Password123.");
        RegisterResponse jackRegisterResponse = userService.signUp(registerRequest);

        assertEquals(1, userRepository.count());
        assertEquals("jack123", jackRegisterResponse.getUsername());

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
    public void registeredUserLogsInWithInvalidPassword_ThrowsExceptionTest() {
        assertEquals(0, userRepository.count());

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("jack123");
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Password123.");
        RegisterResponse jackRegisterResponse = userService.signUp(registerRequest);

        assertEquals(1, userRepository.count());
        assertEquals("jack123", jackRegisterResponse.getUsername());

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
    public void userSignsUp_UserSavesLoginInfoTest() {
        assertEquals(0, userRepository.count());

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("jack123");
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Password123.");
        RegisterResponse jackRegisterResponse = userService.signUp(registerRequest);

        assertEquals(1, userRepository.count());
        assertEquals("jack123", jackRegisterResponse.getUsername());

        SaveNewLoginInfoRequest saveNewLoginInfoRequest = new SaveNewLoginInfoRequest();
        saveNewLoginInfoRequest.setUsername("jack123");
        saveNewLoginInfoRequest.setTitle("gmail login");
        saveNewLoginInfoRequest.setWebsite("www.gmail.com");
        saveNewLoginInfoRequest.setLoginId("jack123@gmail.com");
        SaveNewLoginInfoResponse saveNewLoginInfoResponse = userService.saveNewLoginInfo(saveNewLoginInfoRequest);

        User jackSafeBox = userRepository.findByUsername("jack123");

        assertEquals(1, vaultRepository.count());
        assertEquals(1, loginInfoRepository.count());
        assertEquals(1, jackSafeBox.getVault().getLoginInfos().size());
        assertEquals("jack123@gmail.com", jackSafeBox.getVault().getLoginInfos().getFirst().getLoginId());
        assertEquals("gmail login", saveNewLoginInfoResponse.getTitle());
        assertEquals("www.gmail.com", saveNewLoginInfoResponse.getWebsite());
        assertEquals("jack123@gmail.com", loginInfoRepository.findAll().getFirst().getLoginId());
    }

    @Test
    public void userLogsOut_SavesLoginInfo_ThrowExceptionTest() {
        assertEquals(0, userRepository.count());

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("jack123");
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Password123.");
        RegisterResponse jackRegisterResponse = userService.signUp(registerRequest);

        assertEquals(1, userRepository.count());
        assertEquals("jack123", jackRegisterResponse.getUsername());

        LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setUsername("jack123");
        LogoutResponse jackLogoutResponse = userService.logout(logoutRequest);

        User jackSafeBox = userRepository.findByUsername("jack123");

        assertTrue(jackSafeBox.isLocked());
        assertEquals("jack123", jackLogoutResponse.getUsername());

        SaveNewLoginInfoRequest saveNewLoginInfoRequest = new SaveNewLoginInfoRequest();
        saveNewLoginInfoRequest.setUsername("jack123");
        saveNewLoginInfoRequest.setTitle("gmail login");
        saveNewLoginInfoRequest.setWebsite("www.gmail.com");
        saveNewLoginInfoRequest.setLoginId("jack123@gmail.com");

        assertThrows(ProfileLockStateException.class, ()->userService.saveNewLoginInfo(saveNewLoginInfoRequest));
        assertEquals(1, vaultRepository.count());
        assertEquals(0, loginInfoRepository.count());
    }

    @Test
    public void twoUsersSignsUp_UsersSaveLoginInfoTest() {
        assertEquals(0, userRepository.count());

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("jack123");
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Password123.");
        RegisterResponse jackRegisterResponse = userService.signUp(registerRequest);

        registerRequest.setUsername("jim456");
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Password123.");
        RegisterResponse jimRegisterResponse = userService.signUp(registerRequest);

        assertEquals(2, userRepository.count());
        assertEquals("jack123", jackRegisterResponse.getUsername());
        assertEquals("jim456", jimRegisterResponse.getUsername());

        SaveNewLoginInfoRequest saveNewLoginInfoRequest = new SaveNewLoginInfoRequest();
        saveNewLoginInfoRequest.setUsername("jack123");
        saveNewLoginInfoRequest.setTitle("gmail login");
        saveNewLoginInfoRequest.setWebsite("www.gmail.com");
        saveNewLoginInfoRequest.setLoginId("jack123@gmail.com");
        SaveNewLoginInfoResponse jackSaveNewLoginInfoResponse = userService.saveNewLoginInfo(saveNewLoginInfoRequest);

        saveNewLoginInfoRequest.setUsername("jim456");
        saveNewLoginInfoRequest.setTitle("yahoo login");
        saveNewLoginInfoRequest.setWebsite("www.yahoo.com");
        saveNewLoginInfoRequest.setLoginId("jim456@yahoo.com");
        SaveNewLoginInfoResponse jimSaveNewLoginInfoResponse = userService.saveNewLoginInfo(saveNewLoginInfoRequest);

        User jackSafeBox = userRepository.findByUsername("jack123");
        User jimSafeBox = userRepository.findByUsername("jim456");

        assertEquals(2, vaultRepository.count());
        assertEquals(2, loginInfoRepository.count());

        assertEquals(1, jackSafeBox.getVault().getLoginInfos().size());
        assertEquals(1, jimSafeBox.getVault().getLoginInfos().size());

        assertEquals("jack123@gmail.com", jackSafeBox.getVault().getLoginInfos().getFirst().getLoginId());
        assertEquals("jim456@yahoo.com", jimSafeBox.getVault().getLoginInfos().getFirst().getLoginId());

        assertEquals("gmail login", jackSaveNewLoginInfoResponse.getTitle());
        assertEquals("yahoo login", jimSaveNewLoginInfoResponse.getTitle());

        assertEquals("www.gmail.com", jackSaveNewLoginInfoResponse.getWebsite());
        assertEquals("www.yahoo.com", jimSaveNewLoginInfoResponse.getWebsite());

        assertEquals("jack123@gmail.com", loginInfoRepository.findAll().getFirst().getLoginId());
        assertEquals("jim456@yahoo.com", loginInfoRepository.findAll().get(1).getLoginId());
    }

    @Test
    public void nonExistentUser_SavesLoginInfo_ThrowsExceptionTest() {
        assertEquals(0, userRepository.count());

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("jack123");
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Password123.");
        RegisterResponse jackRegisterResponse = userService.signUp(registerRequest);

        assertEquals(1, userRepository.count());
        assertEquals("jack123", jackRegisterResponse.getUsername());

        SaveNewLoginInfoRequest saveNewLoginInfoRequest = new SaveNewLoginInfoRequest();
        saveNewLoginInfoRequest.setUsername("jim456");
        saveNewLoginInfoRequest.setTitle("gmail login");
        saveNewLoginInfoRequest.setWebsite("www.gmail.com");
        saveNewLoginInfoRequest.setLoginId("jim456@gmail.com");

        assertEquals(0, loginInfoRepository.count());
        assertThrows(UserNotFoundException.class, ()->userService.saveNewLoginInfo(saveNewLoginInfoRequest));
    }

    @Test
    public void usersSignUp_SecondUser_SavesLoginInfoTest() {
        assertEquals(0, userRepository.count());

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("jack123");
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Password123.");
        RegisterResponse jackRegisterResponse = userService.signUp(registerRequest);

        registerRequest.setUsername("jim456");
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Password123.");
        RegisterResponse jimRegisterResponse = userService.signUp(registerRequest);

        assertEquals(2, userRepository.count());
        assertEquals("jack123", jackRegisterResponse.getUsername());
        assertEquals("jim456", jimRegisterResponse.getUsername());

        SaveNewLoginInfoRequest saveNewLoginInfoRequest = new SaveNewLoginInfoRequest();
        saveNewLoginInfoRequest.setUsername("jim456");
        saveNewLoginInfoRequest.setTitle("yahoo login");
        saveNewLoginInfoRequest.setWebsite("www.yahoo.com");
        saveNewLoginInfoRequest.setLoginId("jim456@yahoo.com");
        SaveNewLoginInfoResponse jimSaveNewLoginInfoResponse = userService.saveNewLoginInfo(saveNewLoginInfoRequest);

        User jimSafeBox = userRepository.findByUsername("jim456");

        assertEquals(2, vaultRepository.count());
        assertEquals(1, loginInfoRepository.count());
        assertEquals(1, jimSafeBox.getVault().getLoginInfos().size());
        assertEquals("jim456@yahoo.com", jimSafeBox.getVault().getLoginInfos().getFirst().getLoginId());
        assertEquals("yahoo login", jimSaveNewLoginInfoResponse.getTitle());
        assertEquals("www.yahoo.com", jimSaveNewLoginInfoResponse.getWebsite());
        assertEquals("jim456@yahoo.com", loginInfoRepository.findAll().getFirst().getLoginId());
    }

    @Test
    public void userSavesLoginInfo_TitleFieldIsNull_ThrowsExceptionTest() {
        assertEquals(0, userRepository.count());

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("jack123");
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Password123.");
        RegisterResponse jackRegisterResponse = userService.signUp(registerRequest);

        assertEquals(1, userRepository.count());
        assertEquals("jack123", jackRegisterResponse.getUsername());

        SaveNewLoginInfoRequest saveNewLoginInfoRequest = new SaveNewLoginInfoRequest();
        saveNewLoginInfoRequest.setUsername("jack123");
        saveNewLoginInfoRequest.setTitle(null);
        saveNewLoginInfoRequest.setWebsite("www.gmail.com");
        saveNewLoginInfoRequest.setLoginId("jim456@gmail.com");

        assertEquals(0, loginInfoRepository.count());
        assertThrows(IllegalArgumentException.class, ()->userService.saveNewLoginInfo(saveNewLoginInfoRequest));
    }

    @Test
    public void userSavesLoginInfo_TitleFieldIsEmptyTest() {
        assertEquals(0, userRepository.count());

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("jack123");
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Password123.");
        RegisterResponse jackRegisterResponse = userService.signUp(registerRequest);

        assertEquals(1, userRepository.count());
        assertEquals("jack123", jackRegisterResponse.getUsername());

        SaveNewLoginInfoRequest saveNewLoginInfoRequest = new SaveNewLoginInfoRequest();
        saveNewLoginInfoRequest.setUsername("jack123");
        saveNewLoginInfoRequest.setTitle("");
        saveNewLoginInfoRequest.setWebsite("www.gmail.com");
        saveNewLoginInfoRequest.setLoginId("jim456@gmail.com");

        assertEquals(0, loginInfoRepository.count());
        assertThrows(IllegalArgumentException.class, ()->userService.saveNewLoginInfo(saveNewLoginInfoRequest));
    }

    @Test
    public void userSavesLoginInfo_TitleExistsInUserList_ThrowsExceptionTest() {
        assertEquals(0, userRepository.count());

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("jack123");
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Password123.");
        RegisterResponse jackRegisterResponse = userService.signUp(registerRequest);

        assertEquals(1, userRepository.count());
        assertEquals("jack123", jackRegisterResponse.getUsername());

        SaveNewLoginInfoRequest saveNewLoginInfoRequest = new SaveNewLoginInfoRequest();
        saveNewLoginInfoRequest.setUsername("jack123");
        saveNewLoginInfoRequest.setTitle("gmail login");
        saveNewLoginInfoRequest.setWebsite("www.gmail.com");
        saveNewLoginInfoRequest.setLoginId("jack123@gmail.com");
        SaveNewLoginInfoResponse saveNewLoginInfoResponse = userService.saveNewLoginInfo(saveNewLoginInfoRequest);

        User jackSafeBox = userRepository.findByUsername("jack123");

        assertEquals(1, vaultRepository.count());
        assertEquals(1, loginInfoRepository.count());
        assertEquals(1, jackSafeBox.getVault().getLoginInfos().size());
        assertEquals("jack123@gmail.com", jackSafeBox.getVault().getLoginInfos().getFirst().getLoginId());
        assertEquals("gmail login", saveNewLoginInfoResponse.getTitle());
        assertEquals("www.gmail.com", saveNewLoginInfoResponse.getWebsite());
        assertEquals("jack123@gmail.com", loginInfoRepository.findAll().getFirst().getLoginId());

        saveNewLoginInfoRequest.setUsername("jack123");
        saveNewLoginInfoRequest.setTitle("gmail login");
        saveNewLoginInfoRequest.setWebsite("www.facebook.com");
        saveNewLoginInfoRequest.setLoginId("jack123@gmail.com");

        assertEquals(1, loginInfoRepository.count());
        assertThrows(IllegalArgumentException.class, ()->userService.saveNewLoginInfo(saveNewLoginInfoRequest));
    }

    @Test
    public void userSavesLoginInformation_UserEditLoginInformationTest() {
        assertEquals(0, userRepository.count());

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("jack123");
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Password123.");
        RegisterResponse jackRegisterResponse = userService.signUp(registerRequest);

        assertEquals(1, userRepository.count());
        assertEquals("jack123", jackRegisterResponse.getUsername());

        SaveNewLoginInfoRequest saveNewLoginInfoRequest = new SaveNewLoginInfoRequest();
        saveNewLoginInfoRequest.setUsername("jack123");
        saveNewLoginInfoRequest.setTitle("gmail login");
        saveNewLoginInfoRequest.setWebsite("www.gmail.com");
        saveNewLoginInfoRequest.setLoginId("jack123@gmail.com");
        SaveNewLoginInfoResponse saveNewLoginInfoResponse = userService.saveNewLoginInfo(saveNewLoginInfoRequest);

        User jackSafeBox = userRepository.findByUsername("jack123");

        assertEquals(1, vaultRepository.count());
        assertEquals(1, loginInfoRepository.count());
        assertEquals(1, jackSafeBox.getVault().getLoginInfos().size());
        assertEquals("jack123@gmail.com", jackSafeBox.getVault().getLoginInfos().getFirst().getLoginId());
        assertEquals("gmail login", saveNewLoginInfoResponse.getTitle());
        assertEquals("www.gmail.com", saveNewLoginInfoResponse.getWebsite());
        assertEquals("jack123@gmail.com", loginInfoRepository.findAll().getFirst().getLoginId());

        EditLoginInfoRequest editLoginInfoRequest = new EditLoginInfoRequest();
        editLoginInfoRequest.setUsername("jack123");
        editLoginInfoRequest.setTitle("gmail login");
        editLoginInfoRequest.setEditedTitle("yahoo login");
        editLoginInfoRequest.setEditedWebsite("www.yahoo.com");
        editLoginInfoRequest.setEditedLoginId("jack123@yahoo.com");
        editLoginInfoRequest.setEditedPassword("password");
        EditLoginInfoResponse editLoginInfoResponse = userService.editLoginInfo(editLoginInfoRequest);

        jackSafeBox = userRepository.findByUsername("jack123");

        assertEquals(1, vaultRepository.count());
        assertEquals(1, loginInfoRepository.count());
        assertEquals(1, jackSafeBox.getVault().getLoginInfos().size());
        assertEquals("jack123@yahoo.com", jackSafeBox.getVault().getLoginInfos().getFirst().getLoginId());
        assertEquals("password", jackSafeBox.getVault().getLoginInfos().getFirst().getPassword());
        assertEquals("www.yahoo.com", vaultRepository.findAll().getFirst().getLoginInfos().getFirst().getWebsite());
        assertEquals("yahoo login", editLoginInfoResponse.getTitle());
        assertEquals("www.yahoo.com", editLoginInfoResponse.getWebsite());
        assertEquals("jack123@yahoo.com", loginInfoRepository.findAll().getFirst().getLoginId());
    }

    @Test
    public void userSavesLoginInformation_NonExistentUser_EditsLoginInformation_ThrowsExceptionTest() {
        assertEquals(0, userRepository.count());

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("jack123");
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Password123.");
        RegisterResponse jackRegisterResponse = userService.signUp(registerRequest);

        assertEquals(1, userRepository.count());
        assertEquals("jack123", jackRegisterResponse.getUsername());

        SaveNewLoginInfoRequest saveNewLoginInfoRequest = new SaveNewLoginInfoRequest();
        saveNewLoginInfoRequest.setUsername("jack123");
        saveNewLoginInfoRequest.setTitle("gmail login");
        saveNewLoginInfoRequest.setWebsite("www.gmail.com");
        saveNewLoginInfoRequest.setLoginId("jack123@gmail.com");
        SaveNewLoginInfoResponse saveNewLoginInfoResponse = userService.saveNewLoginInfo(saveNewLoginInfoRequest);

        User jackSafeBox = userRepository.findByUsername("jack123");

        assertEquals(1, vaultRepository.count());
        assertEquals(1, loginInfoRepository.count());
        assertEquals(1, jackSafeBox.getVault().getLoginInfos().size());
        assertEquals("jack123@gmail.com", jackSafeBox.getVault().getLoginInfos().getFirst().getLoginId());
        assertEquals("gmail login", saveNewLoginInfoResponse.getTitle());
        assertEquals("www.gmail.com", saveNewLoginInfoResponse.getWebsite());
        assertEquals("jack123@gmail.com", loginInfoRepository.findAll().getFirst().getLoginId());

        EditLoginInfoRequest editLoginInfoRequest = new EditLoginInfoRequest();
        editLoginInfoRequest.setUsername("jim456");
        editLoginInfoRequest.setTitle("gmail login");
        editLoginInfoRequest.setEditedTitle("yahoo mail");
        editLoginInfoRequest.setEditedWebsite("www.yahoo.com");
        editLoginInfoRequest.setEditedLoginId("jim456@yahoo.com");
        editLoginInfoRequest.setEditedPassword("password");

        assertThrows(UserNotFoundException.class, ()->userService.editLoginInfo(editLoginInfoRequest));
    }

    @Test
    public void userSavesLoginInformation_UserLogsOut_EditsLoginInformation_ThrowExceptionTest() {
        assertEquals(0, userRepository.count());

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("jack123");
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Password123.");
        RegisterResponse jackRegisterResponse = userService.signUp(registerRequest);

        assertEquals(1, userRepository.count());
        assertEquals("jack123", jackRegisterResponse.getUsername());

        SaveNewLoginInfoRequest saveNewLoginInfoRequest = new SaveNewLoginInfoRequest();
        saveNewLoginInfoRequest.setUsername("jack123");
        saveNewLoginInfoRequest.setTitle("gmail login");
        saveNewLoginInfoRequest.setWebsite("www.gmail.com");
        saveNewLoginInfoRequest.setLoginId("jack123@gmail.com");
        SaveNewLoginInfoResponse saveNewLoginInfoResponse = userService.saveNewLoginInfo(saveNewLoginInfoRequest);

        User jackSafeBox = userRepository.findByUsername("jack123");

        assertEquals(1, vaultRepository.count());
        assertEquals(1, loginInfoRepository.count());
        assertEquals(1, jackSafeBox.getVault().getLoginInfos().size());
        assertEquals("jack123@gmail.com", jackSafeBox.getVault().getLoginInfos().getFirst().getLoginId());
        assertEquals("gmail login", saveNewLoginInfoResponse.getTitle());
        assertEquals("www.gmail.com", saveNewLoginInfoResponse.getWebsite());
        assertEquals("jack123@gmail.com", loginInfoRepository.findAll().getFirst().getLoginId());

        LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setUsername("jack123");
        LogoutResponse jackLogoutResponse = userService.logout(logoutRequest);

        jackSafeBox = userRepository.findByUsername("jack123");

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
    public void userSavesLoginInformation_UserEditsNonExistentLoginInformation_ThrowExceptionTest() {
        assertEquals(0, userRepository.count());

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("jack123");
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Password123.");
        RegisterResponse jackRegisterResponse = userService.signUp(registerRequest);

        assertEquals(1, userRepository.count());
        assertEquals("jack123", jackRegisterResponse.getUsername());

        SaveNewLoginInfoRequest saveNewLoginInfoRequest = new SaveNewLoginInfoRequest();
        saveNewLoginInfoRequest.setUsername("jack123");
        saveNewLoginInfoRequest.setTitle("gmail login");
        saveNewLoginInfoRequest.setWebsite("www.gmail.com");
        saveNewLoginInfoRequest.setLoginId("jack123@gmail.com");
        SaveNewLoginInfoResponse saveNewLoginInfoResponse = userService.saveNewLoginInfo(saveNewLoginInfoRequest);

        User jackSafeBox = userRepository.findByUsername("jack123");

        assertEquals(1, vaultRepository.count());
        assertEquals(1, loginInfoRepository.count());
        assertEquals(1, jackSafeBox.getVault().getLoginInfos().size());
        assertEquals("jack123@gmail.com", jackSafeBox.getVault().getLoginInfos().getFirst().getLoginId());
        assertEquals("gmail login", saveNewLoginInfoResponse.getTitle());
        assertEquals("www.gmail.com", saveNewLoginInfoResponse.getWebsite());
        assertEquals("jack123@gmail.com", loginInfoRepository.findAll().getFirst().getLoginId());

        EditLoginInfoRequest editLoginInfoRequest = new EditLoginInfoRequest();
        editLoginInfoRequest.setUsername("jack123");
        editLoginInfoRequest.setTitle("hotmail login");
        editLoginInfoRequest.setEditedTitle("yahoo mail Login");
        editLoginInfoRequest.setEditedWebsite("www.yahoo.com");
        editLoginInfoRequest.setEditedLoginId("jack123@yahoo.com");
        editLoginInfoRequest.setEditedPassword("password");

        assertThrows(LoginInfoNotFoundException.class, ()->userService.editLoginInfo(editLoginInfoRequest));
    }
    @Test
    public void userSavesLoginInfo_UserViewsLoginInfoTest() {
        assertEquals(0, userRepository.count());

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("jack123");
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Password123.");
        RegisterResponse jackRegisterResponse = userService.signUp(registerRequest);

        assertEquals(1, userRepository.count());
        assertEquals("jack123", jackRegisterResponse.getUsername());

        SaveNewLoginInfoRequest saveNewLoginInfoRequest = new SaveNewLoginInfoRequest();
        saveNewLoginInfoRequest.setUsername("jack123");
        saveNewLoginInfoRequest.setTitle("gmail login");
        saveNewLoginInfoRequest.setWebsite("www.gmail.com");
        saveNewLoginInfoRequest.setLoginId("jack123@gmail.com");
        SaveNewLoginInfoResponse saveNewLoginInfoResponse = userService.saveNewLoginInfo(saveNewLoginInfoRequest);

        User jackSafeBox = userRepository.findByUsername("jack123");

        assertEquals(1, vaultRepository.count());
        assertEquals(1, loginInfoRepository.count());
        assertEquals(1, jackSafeBox.getVault().getLoginInfos().size());
        assertEquals("jack123@gmail.com", jackSafeBox.getVault().getLoginInfos().getFirst().getLoginId());
        assertEquals("gmail login", saveNewLoginInfoResponse.getTitle());
        assertEquals("www.gmail.com", saveNewLoginInfoResponse.getWebsite());
        assertEquals("jack123@gmail.com", loginInfoRepository.findAll().getFirst().getLoginId());

        ViewLoginInfoRequest viewLoginInfoRequest = new ViewLoginInfoRequest();
        viewLoginInfoRequest.setUsername("jack123");
        viewLoginInfoRequest.setTitle("gmail login");
        ViewLoginInfoResponse viewLoginInfoResponse = userService.viewLoginInfo(viewLoginInfoRequest);

        assertEquals("jack123@gmail.com", viewLoginInfoResponse.getLoginId());
        assertEquals("www.gmail.com", viewLoginInfoResponse.getWebsite());
    }

    @Test
    public void nonExistentUserViewsLoginInfo_ThrowsExceptionTest() {
        assertEquals(0, userRepository.count());

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("jack123");
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Password123.");
        RegisterResponse jackRegisterResponse = userService.signUp(registerRequest);

        assertEquals(1, userRepository.count());
        assertEquals("jack123", jackRegisterResponse.getUsername());

        SaveNewLoginInfoRequest saveNewLoginInfoRequest = new SaveNewLoginInfoRequest();
        saveNewLoginInfoRequest.setUsername("jack123");
        saveNewLoginInfoRequest.setTitle("gmail login");
        saveNewLoginInfoRequest.setWebsite("www.gmail.com");
        saveNewLoginInfoRequest.setLoginId("jack123@gmail.com");
        SaveNewLoginInfoResponse saveNewLoginInfoResponse = userService.saveNewLoginInfo(saveNewLoginInfoRequest);

        User jackSafeBox = userRepository.findByUsername("jack123");

        assertEquals(1, vaultRepository.count());
        assertEquals(1, loginInfoRepository.count());
        assertEquals(1, jackSafeBox.getVault().getLoginInfos().size());
        assertEquals("jack123@gmail.com", jackSafeBox.getVault().getLoginInfos().getFirst().getLoginId());
        assertEquals("gmail login", saveNewLoginInfoResponse.getTitle());
        assertEquals("www.gmail.com", saveNewLoginInfoResponse.getWebsite());
        assertEquals("jack123@gmail.com", loginInfoRepository.findAll().getFirst().getLoginId());

        ViewLoginInfoRequest viewLoginInfoRequest = new ViewLoginInfoRequest();
        viewLoginInfoRequest.setUsername("jim456");
        viewLoginInfoRequest.setTitle("gmail login");

        assertThrows(UserNotFoundException.class, ()->userService.viewLoginInfo(viewLoginInfoRequest));
    }

    @Test
    public void userViewsNonExistentLoginInfo_ThrowsExceptionTest() {
        assertEquals(0, userRepository.count());

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("jack123");
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Password123.");
        RegisterResponse jackRegisterResponse = userService.signUp(registerRequest);

        assertEquals(1, userRepository.count());
        assertEquals("jack123", jackRegisterResponse.getUsername());

        SaveNewLoginInfoRequest saveNewLoginInfoRequest = new SaveNewLoginInfoRequest();
        saveNewLoginInfoRequest.setUsername("jack123");
        saveNewLoginInfoRequest.setTitle("gmail login");
        saveNewLoginInfoRequest.setWebsite("www.gmail.com");
        saveNewLoginInfoRequest.setLoginId("jack123@gmail.com");
        SaveNewLoginInfoResponse saveNewLoginInfoResponse = userService.saveNewLoginInfo(saveNewLoginInfoRequest);

        User jackSafeBox = userRepository.findByUsername("jack123");

        assertEquals(1, vaultRepository.count());
        assertEquals(1, loginInfoRepository.count());
        assertEquals(1, jackSafeBox.getVault().getLoginInfos().size());
        assertEquals("jack123@gmail.com", jackSafeBox.getVault().getLoginInfos().getFirst().getLoginId());
        assertEquals("gmail login", saveNewLoginInfoResponse.getTitle());
        assertEquals("www.gmail.com", saveNewLoginInfoResponse.getWebsite());
        assertEquals("jack123@gmail.com", loginInfoRepository.findAll().getFirst().getLoginId());

        ViewLoginInfoRequest viewLoginInfoRequest = new ViewLoginInfoRequest();
        viewLoginInfoRequest.setUsername("jack123");
        viewLoginInfoRequest.setTitle("yahoo login");
        assertThrows(LoginInfoNotFoundException.class, ()->userService.viewLoginInfo(viewLoginInfoRequest));

    }

    @Test
    public void userLogsOut_UserViewsLoginInfo_ThrowsExceptionTest() {
        assertEquals(0, userRepository.count());

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("jack123");
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Password123.");
        RegisterResponse jackRegisterResponse = userService.signUp(registerRequest);

        assertEquals(1, userRepository.count());
        assertEquals("jack123", jackRegisterResponse.getUsername());

        SaveNewLoginInfoRequest saveNewLoginInfoRequest = new SaveNewLoginInfoRequest();
        saveNewLoginInfoRequest.setUsername("jack123");
        saveNewLoginInfoRequest.setTitle("gmail login");
        saveNewLoginInfoRequest.setWebsite("www.gmail.com");
        saveNewLoginInfoRequest.setLoginId("jack123@gmail.com");
        SaveNewLoginInfoResponse saveNewLoginInfoResponse = userService.saveNewLoginInfo(saveNewLoginInfoRequest);

        User jackSafeBox = userRepository.findByUsername("jack123");

        assertEquals(1, vaultRepository.count());
        assertEquals(1, loginInfoRepository.count());
        assertEquals(1, jackSafeBox.getVault().getLoginInfos().size());
        assertEquals("jack123@gmail.com", jackSafeBox.getVault().getLoginInfos().getFirst().getLoginId());
        assertEquals("gmail login", saveNewLoginInfoResponse.getTitle());
        assertEquals("www.gmail.com", saveNewLoginInfoResponse.getWebsite());
        assertEquals("jack123@gmail.com", loginInfoRepository.findAll().getFirst().getLoginId());

        LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setUsername("jack123");
        LogoutResponse jackLogoutResponse = userService.logout(logoutRequest);

        jackSafeBox = userRepository.findByUsername("jack123");

        assertTrue(jackSafeBox.isLocked());
        assertEquals("jack123", jackLogoutResponse.getUsername());

        ViewLoginInfoRequest viewLoginInfoRequest = new ViewLoginInfoRequest();
        viewLoginInfoRequest.setUsername("jack123");
        viewLoginInfoRequest.setTitle("gmail login");

        assertThrows(ProfileLockStateException.class, ()->userService.viewLoginInfo(viewLoginInfoRequest));
    }

    @Test
    public void userSavesLoginInfo_UserDeletesLoginInfoTest() {
        assertEquals(0, userRepository.count());

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("jack123");
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Password123.");
        RegisterResponse jackRegisterResponse = userService.signUp(registerRequest);

        assertEquals(1, userRepository.count());
        assertEquals("jack123", jackRegisterResponse.getUsername());

        SaveNewLoginInfoRequest saveNewLoginInfoRequest = new SaveNewLoginInfoRequest();
        saveNewLoginInfoRequest.setUsername("jack123");
        saveNewLoginInfoRequest.setTitle("gmail login");
        saveNewLoginInfoRequest.setWebsite("www.gmail.com");
        saveNewLoginInfoRequest.setLoginId("jack123@gmail.com");
        SaveNewLoginInfoResponse saveNewLoginInfoResponse = userService.saveNewLoginInfo(saveNewLoginInfoRequest);

        User jackSafeBox = userRepository.findByUsername("jack123");

        assertEquals(1, vaultRepository.count());
        assertEquals(1, loginInfoRepository.count());
        assertEquals(1, jackSafeBox.getVault().getLoginInfos().size());
        assertEquals("jack123@gmail.com", jackSafeBox.getVault().getLoginInfos().getFirst().getLoginId());
        assertEquals("gmail login", saveNewLoginInfoResponse.getTitle());
        assertEquals("www.gmail.com", saveNewLoginInfoResponse.getWebsite());
        assertEquals("jack123@gmail.com", loginInfoRepository.findAll().getFirst().getLoginId());

        DeleteLoginInfoRequest deleteLoginInfoRequest = new DeleteLoginInfoRequest();
        deleteLoginInfoRequest.setUsername("jack123");
        deleteLoginInfoRequest.setTitle("gmail login");
        deleteLoginInfoRequest.setMasterPassword("Password123.");
        DeleteLoginInfoResponse deleteLoginInfoResponse = userService.deleteLoginInfo(deleteLoginInfoRequest);

        jackSafeBox = userRepository.findByUsername("jack123");

        assertEquals(0, loginInfoRepository.count());
        assertEquals(0, jackSafeBox.getVault().getLoginInfos().size());
        assertEquals(0, vaultRepository.findAll().getFirst().getLoginInfos().size());
        assertEquals("gmail login", deleteLoginInfoResponse.getTitle());
    }

    @Test
    public void twoUsersSaveLoginInfo_SecondUserDeletesInfoTest() {
        assertEquals(0, userRepository.count());

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("jack123");
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Password123.");
        RegisterResponse jackRegisterResponse = userService.signUp(registerRequest);

        registerRequest.setUsername("jim456");
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Password123.");
        RegisterResponse jimRegisterResponse = userService.signUp(registerRequest);

        assertEquals(2, userRepository.count());
        assertEquals("jack123", jackRegisterResponse.getUsername());
        assertEquals("jim456", jimRegisterResponse.getUsername());

        SaveNewLoginInfoRequest saveNewLoginInfoRequest = new SaveNewLoginInfoRequest();
        saveNewLoginInfoRequest.setUsername("jack123");
        saveNewLoginInfoRequest.setTitle("gmail login");
        saveNewLoginInfoRequest.setWebsite("www.gmail.com");
        saveNewLoginInfoRequest.setLoginId("jack123@gmail.com");
        SaveNewLoginInfoResponse jackSaveNewLoginInfoResponse = userService.saveNewLoginInfo(saveNewLoginInfoRequest);

        saveNewLoginInfoRequest.setUsername("jim456");
        saveNewLoginInfoRequest.setTitle("gmail login");
        saveNewLoginInfoRequest.setWebsite("www.gmail.com");
        saveNewLoginInfoRequest.setLoginId("jim456@gmail.com");
        SaveNewLoginInfoResponse jimSaveNewLoginInfoResponse = userService.saveNewLoginInfo(saveNewLoginInfoRequest);

        User jackSafeBox = userRepository.findByUsername("jack123");
        User jimSafeBox = userRepository.findByUsername("jim456");

        assertEquals(2, vaultRepository.count());
        assertEquals(2, loginInfoRepository.count());

        assertEquals(1, jackSafeBox.getVault().getLoginInfos().size());
        assertEquals(1, jimSafeBox.getVault().getLoginInfos().size());

        assertEquals("jack123@gmail.com", jackSafeBox.getVault().getLoginInfos().getFirst().getLoginId());
        assertEquals("jim456@gmail.com", jimSafeBox.getVault().getLoginInfos().getFirst().getLoginId());

        assertEquals("gmail login", jackSaveNewLoginInfoResponse.getTitle());
        assertEquals("gmail login", jimSaveNewLoginInfoResponse.getTitle());

        assertEquals("www.gmail.com", jackSaveNewLoginInfoResponse.getWebsite());
        assertEquals("www.gmail.com", jimSaveNewLoginInfoResponse.getWebsite());

        assertEquals("jack123@gmail.com", loginInfoRepository.findAll().getFirst().getLoginId());
        assertEquals("jim456@gmail.com", loginInfoRepository.findAll().get(1).getLoginId());

        DeleteLoginInfoRequest deleteLoginInfoRequest = new DeleteLoginInfoRequest();
        deleteLoginInfoRequest.setUsername("jim456");
        deleteLoginInfoRequest.setTitle("gmail login");
        deleteLoginInfoRequest.setMasterPassword("Password123.");
        DeleteLoginInfoResponse deleteLoginInfoResponse = userService.deleteLoginInfo(deleteLoginInfoRequest);

        jimSafeBox = userRepository.findByUsername("jim456");

        assertEquals(1, loginInfoRepository.count());
        assertEquals(0, jimSafeBox.getVault().getLoginInfos().size());
        assertEquals(1, vaultRepository.findAll().getFirst().getLoginInfos().size());
        assertEquals("gmail login", deleteLoginInfoResponse.getTitle());
    }


    @Test
    public void userLogsOut_DeletesLoginInfo_ThrowExceptionTest() {
        assertEquals(0, userRepository.count());

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("jack123");
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Password123.");
        RegisterResponse jackRegisterResponse = userService.signUp(registerRequest);

        assertEquals(1, userRepository.count());
        assertEquals("jack123", jackRegisterResponse.getUsername());

        SaveNewLoginInfoRequest saveNewLoginInfoRequest = new SaveNewLoginInfoRequest();
        saveNewLoginInfoRequest.setUsername("jack123");
        saveNewLoginInfoRequest.setTitle("gmail login");
        saveNewLoginInfoRequest.setWebsite("www.gmail.com");
        saveNewLoginInfoRequest.setLoginId("jack123@gmail.com");
        SaveNewLoginInfoResponse saveNewLoginInfoResponse = userService.saveNewLoginInfo(saveNewLoginInfoRequest);

        User jackSafeBox = userRepository.findByUsername("jack123");

        assertEquals(1, vaultRepository.count());
        assertEquals(1, loginInfoRepository.count());
        assertEquals(1, jackSafeBox.getVault().getLoginInfos().size());
        assertEquals("jack123@gmail.com", jackSafeBox.getVault().getLoginInfos().getFirst().getLoginId());
        assertEquals("gmail login", saveNewLoginInfoResponse.getTitle());
        assertEquals("www.gmail.com", saveNewLoginInfoResponse.getWebsite());
        assertEquals("jack123@gmail.com", loginInfoRepository.findAll().getFirst().getLoginId());

        LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setUsername("jack123");
        LogoutResponse jackLogoutResponse = userService.logout(logoutRequest);

        jackSafeBox = userRepository.findByUsername("jack123");

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
        assertEquals(0, userRepository.count());

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("jack123");
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Password123.");
        RegisterResponse jackRegisterResponse = userService.signUp(registerRequest);

        assertEquals(1, userRepository.count());
        assertEquals("jack123", jackRegisterResponse.getUsername());

        SaveNewLoginInfoRequest saveNewLoginInfoRequest = new SaveNewLoginInfoRequest();
        saveNewLoginInfoRequest.setUsername("jack123");
        saveNewLoginInfoRequest.setTitle("gmail login");
        saveNewLoginInfoRequest.setWebsite("www.gmail.com");
        saveNewLoginInfoRequest.setLoginId("jack123@gmail.com");
        SaveNewLoginInfoResponse saveNewLoginInfoResponse = userService.saveNewLoginInfo(saveNewLoginInfoRequest);

        User jackSafeBox = userRepository.findByUsername("jack123");

        assertEquals(1, vaultRepository.count());
        assertEquals(1, loginInfoRepository.count());
        assertEquals(1, jackSafeBox.getVault().getLoginInfos().size());
        assertEquals("jack123@gmail.com", jackSafeBox.getVault().getLoginInfos().getFirst().getLoginId());
        assertEquals("gmail login", saveNewLoginInfoResponse.getTitle());
        assertEquals("www.gmail.com", saveNewLoginInfoResponse.getWebsite());
        assertEquals("jack123@gmail.com", loginInfoRepository.findAll().getFirst().getLoginId());

        LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setUsername("jack123");
        LogoutResponse jackLogoutResponse = userService.logout(logoutRequest);

        jackSafeBox = userRepository.findByUsername("jack123");

        assertTrue(jackSafeBox.isLocked());
        assertEquals("jack123", jackLogoutResponse.getUsername());

        DeleteLoginInfoRequest deleteLoginInfoRequest = new DeleteLoginInfoRequest();
        deleteLoginInfoRequest.setUsername("jim456");
        deleteLoginInfoRequest.setTitle("gmail login");
        deleteLoginInfoRequest.setMasterPassword("Password123.");

        assertThrows(UserNotFoundException.class, ()->userService.deleteLoginInfo(deleteLoginInfoRequest));
    }

    @Test
    public void userDeletesLoginInfo_PasswordIsInvalid_ThrowExceptionTest() {
        assertEquals(0, userRepository.count());

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("jack123");
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Password123.");
        RegisterResponse jackRegisterResponse = userService.signUp(registerRequest);

        assertEquals(1, userRepository.count());
        assertEquals("jack123", jackRegisterResponse.getUsername());

        SaveNewLoginInfoRequest saveNewLoginInfoRequest = new SaveNewLoginInfoRequest();
        saveNewLoginInfoRequest.setUsername("jack123");
        saveNewLoginInfoRequest.setTitle("gmail login");
        saveNewLoginInfoRequest.setWebsite("www.gmail.com");
        saveNewLoginInfoRequest.setLoginId("jack123@gmail.com");
        SaveNewLoginInfoResponse saveNewLoginInfoResponse = userService.saveNewLoginInfo(saveNewLoginInfoRequest);

        User jackSafeBox = userRepository.findByUsername("jack123");

        assertEquals(1, vaultRepository.count());
        assertEquals(1, loginInfoRepository.count());
        assertEquals(1, jackSafeBox.getVault().getLoginInfos().size());
        assertEquals("jack123@gmail.com", jackSafeBox.getVault().getLoginInfos().getFirst().getLoginId());
        assertEquals("gmail login", saveNewLoginInfoResponse.getTitle());
        assertEquals("www.gmail.com", saveNewLoginInfoResponse.getWebsite());
        assertEquals("jack123@gmail.com", loginInfoRepository.findAll().getFirst().getLoginId());

        DeleteLoginInfoRequest deleteLoginInfoRequest = new DeleteLoginInfoRequest();
        deleteLoginInfoRequest.setUsername("jack123");
        deleteLoginInfoRequest.setTitle("gmail login");
        deleteLoginInfoRequest.setMasterPassword("invalid password");

        assertThrows(InvalidPasswordException.class, ()->userService.deleteLoginInfo(deleteLoginInfoRequest));
    }

    @Test
    public void userDeletesNonExistentLoginInfo_ThrowExceptionTest() {
        assertEquals(0, userRepository.count());

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("jack123");
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Password123.");
        RegisterResponse jackRegisterResponse = userService.signUp(registerRequest);

        assertEquals(1, userRepository.count());
        assertEquals("jack123", jackRegisterResponse.getUsername());

        SaveNewLoginInfoRequest saveNewLoginInfoRequest = new SaveNewLoginInfoRequest();
        saveNewLoginInfoRequest.setUsername("jack123");
        saveNewLoginInfoRequest.setTitle("gmail login");
        saveNewLoginInfoRequest.setWebsite("www.gmail.com");
        saveNewLoginInfoRequest.setLoginId("jack123@gmail.com");
        SaveNewLoginInfoResponse saveNewLoginInfoResponse = userService.saveNewLoginInfo(saveNewLoginInfoRequest);

        User jackSafeBox = userRepository.findByUsername("jack123");

        assertEquals(1, vaultRepository.count());
        assertEquals(1, loginInfoRepository.count());
        assertEquals(1, jackSafeBox.getVault().getLoginInfos().size());
        assertEquals("jack123@gmail.com", jackSafeBox.getVault().getLoginInfos().getFirst().getLoginId());
        assertEquals("gmail login", saveNewLoginInfoResponse.getTitle());
        assertEquals("www.gmail.com", saveNewLoginInfoResponse.getWebsite());
        assertEquals("jack123@gmail.com", loginInfoRepository.findAll().getFirst().getLoginId());

        DeleteLoginInfoRequest deleteLoginInfoRequest = new DeleteLoginInfoRequest();
        deleteLoginInfoRequest.setUsername("jack123");
        deleteLoginInfoRequest.setTitle("yahoo login");
        deleteLoginInfoRequest.setMasterPassword("Password123.");

        assertThrows(LoginInfoNotFoundException.class, ()->userService.deleteLoginInfo(deleteLoginInfoRequest));
    }

    @Test
    public void userSignsUp_UserCreatesNoteTest() {
        assertEquals(0, userRepository.count());

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("jack123");
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Password123.");
        RegisterResponse jackRegisterResponse = userService.signUp(registerRequest);

        assertEquals(1, userRepository.count());
        assertEquals("jack123", jackRegisterResponse.getUsername());

        CreateNoteRequest createNoteRequest = new CreateNoteRequest();
        createNoteRequest.setUsername("jack123");
        createNoteRequest.setTitle("ideas");
        createNoteRequest.setContent("Build an AI assistant for projects");
        CreateNoteResponse createNoteResponse = userService.createNote(createNoteRequest);

        User jackSafeBox = userRepository.findByUsername("jack123");

        assertEquals(1, vaultRepository.count());
        assertEquals(1, noteRepository.count());
        assertEquals(1, jackSafeBox.getVault().getNotes().size());
        assertEquals("ideas", jackSafeBox.getVault().getNotes().getFirst().getTitle());
        assertEquals("Build an AI assistant for projects", createNoteResponse.getContent());
        assertEquals("ideas", noteRepository.findAll().getFirst().getTitle());
    }

    @Test
    public void twoUsersSignUp_UsersCreateNotesTest() {
        assertEquals(0, userRepository.count());

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("jack123");
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Password123.");
        RegisterResponse jackRegisterResponse = userService.signUp(registerRequest);

        registerRequest.setUsername("jim456");
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Password123.");
        RegisterResponse jimRegisterResponse = userService.signUp(registerRequest);

        assertEquals(2, userRepository.count());
        assertEquals("jack123", jackRegisterResponse.getUsername());
        assertEquals("jim456", jimRegisterResponse.getUsername());

        CreateNoteRequest createNoteRequest = new CreateNoteRequest();
        createNoteRequest.setUsername("jack123");
        createNoteRequest.setTitle("ideas");
        createNoteRequest.setContent("Build an AI assistant for projects");
        CreateNoteResponse jackCreateNoteResponse = userService.createNote(createNoteRequest);

        createNoteRequest.setUsername("jim456");
        createNoteRequest.setTitle("designs");
        createNoteRequest.setContent("Minimalist design for my bedroom");
        CreateNoteResponse jimCreateNoteResponse = userService.createNote(createNoteRequest);

        User jackSafeBox = userRepository.findByUsername("jack123");
        User jimSafeBox = userRepository.findByUsername("jim456");
        
        assertEquals(2, vaultRepository.count());
        assertEquals(2, noteRepository.count());

        assertEquals(1, jackSafeBox.getVault().getNotes().size());
        assertEquals(1, jimSafeBox.getVault().getNotes().size());

        assertEquals("ideas", jackSafeBox.getVault().getNotes().getFirst().getTitle());
        assertEquals("designs", jimSafeBox.getVault().getNotes().getFirst().getTitle());

        assertEquals("Build an AI assistant for projects", jackCreateNoteResponse.getContent());
        assertEquals("Minimalist design for my bedroom", jimCreateNoteResponse.getContent());

        assertEquals("ideas", noteRepository.findAll().getFirst().getTitle());
        assertEquals("designs", noteRepository.findAll().get(1).getTitle());
    }

    @Test
    public void userSignsUp_UserLogsOuts_CreatesNote_ThrowsExceptionTest() {
        assertEquals(0, userRepository.count());

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("jack123");
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Password123.");
        RegisterResponse jackRegisterResponse = userService.signUp(registerRequest);

        assertEquals(1, userRepository.count());
        assertEquals("jack123", jackRegisterResponse.getUsername());

        LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setUsername("jack123");
        LogoutResponse jackLogoutResponse = userService.logout(logoutRequest);

        User jackSafeBox = userRepository.findByUsername("jack123");

        assertTrue(jackSafeBox.isLocked());
        assertEquals("jack123", jackLogoutResponse.getUsername());

        CreateNoteRequest createNoteRequest = new CreateNoteRequest();
        createNoteRequest.setUsername("jack123");
        createNoteRequest.setTitle("ideas");
        createNoteRequest.setContent("Build an AI assistant for projects");

        assertThrows(ProfileLockStateException.class, ()->userService.createNote(createNoteRequest));
    }

    @Test
    public void nonExistentUser_CreatesNote_ThrowsExceptionTest() {
        assertEquals(0, userRepository.count());

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("jack123");
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Password123.");
        RegisterResponse jackRegisterResponse = userService.signUp(registerRequest);

        assertEquals(1, userRepository.count());
        assertEquals("jack123", jackRegisterResponse.getUsername());

        CreateNoteRequest createNoteRequest = new CreateNoteRequest();
        createNoteRequest.setUsername("jim456");
        createNoteRequest.setTitle("ideas");
        createNoteRequest.setContent("Build an AI assistant for projects");

        assertThrows(UserNotFoundException.class, ()->userService.createNote(createNoteRequest));
    }

    @Test
    public void userCreatesNote_TitleFieldIsNull_ThrowsExceptionTest() {
        assertEquals(0, userRepository.count());

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("jack123");
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Password123.");
        RegisterResponse jackRegisterResponse = userService.signUp(registerRequest);

        assertEquals(1, userRepository.count());
        assertEquals("jack123", jackRegisterResponse.getUsername());

        CreateNoteRequest createNoteRequest = new CreateNoteRequest();
        createNoteRequest.setUsername("jack123");
        createNoteRequest.setTitle(null);
        createNoteRequest.setContent("Build an AI assistant for projects");

        assertThrows(IllegalArgumentException.class, ()->userService.createNote(createNoteRequest));
    }

    @Test
    public void userCreatesNote_TitleFieldIsEmpty_ThrowsExceptionTest() {
        assertEquals(0, userRepository.count());

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("jack123");
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Password123.");
        RegisterResponse jackRegisterResponse = userService.signUp(registerRequest);

        assertEquals(1, userRepository.count());
        assertEquals("jack123", jackRegisterResponse.getUsername());

        CreateNoteRequest createNoteRequest = new CreateNoteRequest();
        createNoteRequest.setUsername("jack123");
        createNoteRequest.setTitle("");
        createNoteRequest.setContent("Build an AI assistant for projects");

        assertThrows(IllegalArgumentException.class, ()->userService.createNote(createNoteRequest));
    }

    @Test
    public void userSignsUp_UserCreatesNote_WithExistingTitle_ThrowsExceptionTest() {
        assertEquals(0, userRepository.count());

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("jack123");
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Password123.");
        RegisterResponse jackRegisterResponse = userService.signUp(registerRequest);

        assertEquals(1, userRepository.count());
        assertEquals("jack123", jackRegisterResponse.getUsername());

        CreateNoteRequest createNoteRequest = new CreateNoteRequest();
        createNoteRequest.setUsername("jack123");
        createNoteRequest.setTitle("ideas");
        createNoteRequest.setContent("Build an AI assistant for projects");
        CreateNoteResponse createNoteResponse = userService.createNote(createNoteRequest);

        User jackSafeBox = userRepository.findByUsername("jack123");

        assertEquals(1, vaultRepository.count());
        assertEquals(1, noteRepository.count());
        assertEquals(1, jackSafeBox.getVault().getNotes().size());
        assertEquals("ideas", jackSafeBox.getVault().getNotes().getFirst().getTitle());
        assertEquals("Build an AI assistant for projects", createNoteResponse.getContent());
        assertEquals("ideas", noteRepository.findAll().getFirst().getTitle());

        createNoteRequest.setUsername("jack123");
        createNoteRequest.setTitle("ideas");
        createNoteRequest.setContent("Create algorithm for flying robot");

        assertThrows(IllegalArgumentException.class, ()->userService.createNote(createNoteRequest));
    }

    @Test
    public void userSignsUp_UserCreatesNote_UserEditsNoteTest() {
        assertEquals(0, userRepository.count());

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("jack123");
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Password123.");
        RegisterResponse jackRegisterResponse = userService.signUp(registerRequest);

        assertEquals(1, userRepository.count());
        assertEquals("jack123", jackRegisterResponse.getUsername());

        CreateNoteRequest createNoteRequest = new CreateNoteRequest();
        createNoteRequest.setUsername("jack123");
        createNoteRequest.setTitle("ideas");
        createNoteRequest.setContent("Build an AI assistant for projects");
        CreateNoteResponse createNoteResponse = userService.createNote(createNoteRequest);

        User jackSafeBox = userRepository.findByUsername("jack123");

        assertEquals(1, vaultRepository.count());
        assertEquals(1, noteRepository.count());
        assertEquals(1, jackSafeBox.getVault().getNotes().size());
        assertEquals("ideas", jackSafeBox.getVault().getNotes().getFirst().getTitle());
        assertEquals("Build an AI assistant for projects", createNoteResponse.getContent());
        assertEquals("ideas", noteRepository.findAll().getFirst().getTitle());

        EditNoteRequest editNoteRequest = new EditNoteRequest();
        editNoteRequest.setUsername("jack123");
        editNoteRequest.setTitle("ideas");
        editNoteRequest.setEditedTitle("new ideas");
        editNoteRequest.setEditedContent("Build Robots for heart surgeries");
        EditNoteResponse editNoteResponse = userService.editNote(editNoteRequest);

        jackSafeBox = userRepository.findByUsername("jack123");

        assertEquals(1, vaultRepository.count());
        assertEquals(1, noteRepository.count());
        assertEquals(1, jackSafeBox.getVault().getNotes().size());
        assertEquals("new ideas", jackSafeBox.getVault().getNotes().getFirst().getTitle());
        assertEquals("Build Robots for heart surgeries", editNoteResponse.getContent());
        assertEquals("new ideas", noteRepository.findAll().getFirst().getTitle());
    }

    @Test
    public void userSignsUp_CreatesNote_UserLogsOut_UserEditsNote_ThrowsExceptionTest() {
        assertEquals(0, userRepository.count());

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("jack123");
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Password123.");
        RegisterResponse jackRegisterResponse = userService.signUp(registerRequest);

        assertEquals(1, userRepository.count());
        assertEquals("jack123", jackRegisterResponse.getUsername());

        CreateNoteRequest createNoteRequest = new CreateNoteRequest();
        createNoteRequest.setUsername("jack123");
        createNoteRequest.setTitle("ideas");
        createNoteRequest.setContent("Build an AI assistant for projects");
        CreateNoteResponse createNoteResponse = userService.createNote(createNoteRequest);

        User jackSafeBox = userRepository.findByUsername("jack123");

        assertEquals(1, vaultRepository.count());
        assertEquals(1, noteRepository.count());
        assertEquals(1, jackSafeBox.getVault().getNotes().size());
        assertEquals("ideas", jackSafeBox.getVault().getNotes().getFirst().getTitle());
        assertEquals("Build an AI assistant for projects", createNoteResponse.getContent());
        assertEquals("ideas", noteRepository.findAll().getFirst().getTitle());

        LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setUsername("jack123");
        LogoutResponse jackLogoutResponse = userService.logout(logoutRequest);

        jackSafeBox = userRepository.findByUsername("jack123");

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
        assertEquals(0, userRepository.count());

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("jack123");
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Password123.");
        RegisterResponse jackRegisterResponse = userService.signUp(registerRequest);

        assertEquals(1, userRepository.count());
        assertEquals("jack123", jackRegisterResponse.getUsername());

        CreateNoteRequest createNoteRequest = new CreateNoteRequest();
        createNoteRequest.setUsername("jack123");
        createNoteRequest.setTitle("ideas");
        createNoteRequest.setContent("Build an AI assistant for projects");
        CreateNoteResponse createNoteResponse = userService.createNote(createNoteRequest);

        User jackSafeBox = userRepository.findByUsername("jack123");

        assertEquals(1, vaultRepository.count());
        assertEquals(1, noteRepository.count());
        assertEquals(1, jackSafeBox.getVault().getNotes().size());
        assertEquals("ideas", jackSafeBox.getVault().getNotes().getFirst().getTitle());
        assertEquals("Build an AI assistant for projects", createNoteResponse.getContent());
        assertEquals("ideas", noteRepository.findAll().getFirst().getTitle());

        LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setUsername("jack123");
        LogoutResponse jackLogoutResponse = userService.logout(logoutRequest);

        jackSafeBox = userRepository.findByUsername("jack123");

        assertTrue(jackSafeBox.isLocked());
        assertEquals("jack123", jackLogoutResponse.getUsername());

        EditNoteRequest editNoteRequest = new EditNoteRequest();
        editNoteRequest.setUsername("jim456");
        editNoteRequest.setTitle("ideas");
        editNoteRequest.setEditedTitle("new ideas");
        editNoteRequest.setEditedContent("Build Robots for heart surgeries");

        assertThrows(UserNotFoundException.class, ()-> userService.editNote(editNoteRequest));
    }

    @Test
    public void userCreatesNote_UserEditsNote_TitleIsEmpty_ThrowsExceptionTest() {
        assertEquals(0, userRepository.count());

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("jack123");
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Password123.");
        RegisterResponse jackRegisterResponse = userService.signUp(registerRequest);

        assertEquals(1, userRepository.count());
        assertEquals("jack123", jackRegisterResponse.getUsername());

        CreateNoteRequest createNoteRequest = new CreateNoteRequest();
        createNoteRequest.setUsername("jack123");
        createNoteRequest.setTitle("ideas");
        createNoteRequest.setContent("Build an AI assistant for projects");
        CreateNoteResponse createNoteResponse = userService.createNote(createNoteRequest);

        User jackSafeBox = userRepository.findByUsername("jack123");

        assertEquals(1, vaultRepository.count());
        assertEquals(1, noteRepository.count());
        assertEquals(1, jackSafeBox.getVault().getNotes().size());
        assertEquals("ideas", jackSafeBox.getVault().getNotes().getFirst().getTitle());
        assertEquals("Build an AI assistant for projects", createNoteResponse.getContent());
        assertEquals("ideas", noteRepository.findAll().getFirst().getTitle());

        EditNoteRequest editNoteRequest = new EditNoteRequest();
        editNoteRequest.setUsername("jack123");
        editNoteRequest.setTitle("ideas");
        editNoteRequest.setEditedTitle("");
        editNoteRequest.setEditedContent("Build Robots for heart surgeries");

        assertThrows(IllegalArgumentException.class, ()-> userService.editNote(editNoteRequest));
    }

    @Test
    public void userCreatesNote_UserEditsNote_TitleIsNull_ThrowsExceptionTest() {
        assertEquals(0, userRepository.count());

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("jack123");
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Password123.");
        RegisterResponse jackRegisterResponse = userService.signUp(registerRequest);

        assertEquals(1, userRepository.count());
        assertEquals("jack123", jackRegisterResponse.getUsername());

        CreateNoteRequest createNoteRequest = new CreateNoteRequest();
        createNoteRequest.setUsername("jack123");
        createNoteRequest.setTitle("ideas");
        createNoteRequest.setContent("Build an AI assistant for projects");
        CreateNoteResponse createNoteResponse = userService.createNote(createNoteRequest);

        User jackSafeBox = userRepository.findByUsername("jack123");

        assertEquals(1, vaultRepository.count());
        assertEquals(1, noteRepository.count());
        assertEquals(1, jackSafeBox.getVault().getNotes().size());
        assertEquals("ideas", jackSafeBox.getVault().getNotes().getFirst().getTitle());
        assertEquals("Build an AI assistant for projects", createNoteResponse.getContent());
        assertEquals("ideas", noteRepository.findAll().getFirst().getTitle());

        EditNoteRequest editNoteRequest = new EditNoteRequest();
        editNoteRequest.setUsername("jack123");
        editNoteRequest.setTitle("ideas");
        editNoteRequest.setEditedTitle(null);
        editNoteRequest.setEditedContent("Build Robots for heart surgeries");

        assertThrows(IllegalArgumentException.class, ()-> userService.editNote(editNoteRequest));
    }

    @Test
    public void userCreatesNote_UserViewsNoteTest() {
        assertEquals(0, userRepository.count());

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("jack123");
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Password123.");
        RegisterResponse jackRegisterResponse = userService.signUp(registerRequest);

        assertEquals(1, userRepository.count());
        assertEquals("jack123", jackRegisterResponse.getUsername());

        CreateNoteRequest createNoteRequest = new CreateNoteRequest();
        createNoteRequest.setUsername("jack123");
        createNoteRequest.setTitle("ideas");
        createNoteRequest.setContent("Build an AI assistant for projects");
        CreateNoteResponse createNoteResponse = userService.createNote(createNoteRequest);

        User jackSafeBox = userRepository.findByUsername("jack123");

        assertEquals(1, vaultRepository.count());
        assertEquals(1, noteRepository.count());
        assertEquals(1, jackSafeBox.getVault().getNotes().size());
        assertEquals("ideas", jackSafeBox.getVault().getNotes().getFirst().getTitle());
        assertEquals("Build an AI assistant for projects", createNoteResponse.getContent());
        assertEquals("ideas", noteRepository.findAll().getFirst().getTitle());

        ViewNoteRequest viewNoteRequest = new ViewNoteRequest();
        viewNoteRequest.setUsername("jack123");
        viewNoteRequest.setTitle("ideas");
        ViewNoteResponse viewNoteResponse = userService.viewNote(viewNoteRequest);

        assertEquals("ideas", viewNoteResponse.getTitle());
        assertEquals("Build an AI assistant for projects", viewNoteResponse.getContent());
    }

    @Test
    public void userLogsOut_UserViewsNote_ThrowsExceptionTestTest() {
        assertEquals(0, userRepository.count());

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("jack123");
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Password123.");
        RegisterResponse jackRegisterResponse = userService.signUp(registerRequest);

        assertEquals(1, userRepository.count());
        assertEquals("jack123", jackRegisterResponse.getUsername());

        CreateNoteRequest createNoteRequest = new CreateNoteRequest();
        createNoteRequest.setUsername("jack123");
        createNoteRequest.setTitle("ideas");
        createNoteRequest.setContent("Build an AI assistant for projects");
        CreateNoteResponse createNoteResponse = userService.createNote(createNoteRequest);

        User jackSafeBox = userRepository.findByUsername("jack123");

        assertEquals(1, vaultRepository.count());
        assertEquals(1, noteRepository.count());
        assertEquals(1, jackSafeBox.getVault().getNotes().size());
        assertEquals("ideas", jackSafeBox.getVault().getNotes().getFirst().getTitle());
        assertEquals("Build an AI assistant for projects", createNoteResponse.getContent());
        assertEquals("ideas", noteRepository.findAll().getFirst().getTitle());

        LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setUsername("jack123");
        LogoutResponse jackLogoutResponse = userService.logout(logoutRequest);

        jackSafeBox = userRepository.findByUsername("jack123");

        assertTrue(jackSafeBox.isLocked());
        assertEquals("jack123", jackLogoutResponse.getUsername());

        ViewNoteRequest viewNoteRequest = new ViewNoteRequest();
        viewNoteRequest.setUsername("jack123");
        viewNoteRequest.setTitle("ideas");

        assertThrows(ProfileLockStateException.class, ()->userService.viewNote(viewNoteRequest));
    }

    @Test
    public void nonExistentUserViewsNote_ThrowsExceptionTest() {
        assertEquals(0, userRepository.count());

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("jack123");
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Password123.");
        RegisterResponse jackRegisterResponse = userService.signUp(registerRequest);

        assertEquals(1, userRepository.count());
        assertEquals("jack123", jackRegisterResponse.getUsername());

        CreateNoteRequest createNoteRequest = new CreateNoteRequest();
        createNoteRequest.setUsername("jack123");
        createNoteRequest.setTitle("ideas");
        createNoteRequest.setContent("Build an AI assistant for projects");
        CreateNoteResponse createNoteResponse = userService.createNote(createNoteRequest);

        User jackSafeBox = userRepository.findByUsername("jack123");

        assertEquals(1, vaultRepository.count());
        assertEquals(1, noteRepository.count());
        assertEquals(1, jackSafeBox.getVault().getNotes().size());
        assertEquals("ideas", jackSafeBox.getVault().getNotes().getFirst().getTitle());
        assertEquals("Build an AI assistant for projects", createNoteResponse.getContent());
        assertEquals("ideas", noteRepository.findAll().getFirst().getTitle());

        ViewNoteRequest viewNoteRequest = new ViewNoteRequest();
        viewNoteRequest.setUsername("jim456");
        viewNoteRequest.setTitle("ideas");

        assertThrows(UserNotFoundException.class, ()->userService.viewNote(viewNoteRequest));
    }

    @Test
    public void userViewsNonExistentNote_ThrowsExceptionTest() {
        assertEquals(0, userRepository.count());

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("jack123");
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Password123.");
        RegisterResponse jackRegisterResponse = userService.signUp(registerRequest);

        assertEquals(1, userRepository.count());
        assertEquals("jack123", jackRegisterResponse.getUsername());

        CreateNoteRequest createNoteRequest = new CreateNoteRequest();
        createNoteRequest.setUsername("jack123");
        createNoteRequest.setTitle("ideas");
        createNoteRequest.setContent("Build an AI assistant for projects");
        CreateNoteResponse createNoteResponse = userService.createNote(createNoteRequest);

        User jackSafeBox = userRepository.findByUsername("jack123");

        assertEquals(1, vaultRepository.count());
        assertEquals(1, noteRepository.count());
        assertEquals(1, jackSafeBox.getVault().getNotes().size());
        assertEquals("ideas", jackSafeBox.getVault().getNotes().getFirst().getTitle());
        assertEquals("Build an AI assistant for projects", createNoteResponse.getContent());
        assertEquals("ideas", noteRepository.findAll().getFirst().getTitle());

        ViewNoteRequest viewNoteRequest = new ViewNoteRequest();
        viewNoteRequest.setUsername("jack123");
        viewNoteRequest.setTitle("new ideas");

        assertThrows(NoteNotFoundException.class, ()->userService.viewNote(viewNoteRequest));
    }

    @Test
    public void userCreatesNote_UserDeletesNoteTest() {
        assertEquals(0, userRepository.count());

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("jack123");
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Password123.");
        RegisterResponse jackRegisterResponse = userService.signUp(registerRequest);

        assertEquals(1, userRepository.count());
        assertEquals("jack123", jackRegisterResponse.getUsername());

        CreateNoteRequest createNoteRequest = new CreateNoteRequest();
        createNoteRequest.setUsername("jack123");
        createNoteRequest.setTitle("ideas");
        createNoteRequest.setContent("Build an AI assistant for projects");
        CreateNoteResponse createNoteResponse = userService.createNote(createNoteRequest);

        User jackSafeBox = userRepository.findByUsername("jack123");

        assertEquals(1, vaultRepository.count());
        assertEquals(1, noteRepository.count());
        assertEquals(1, jackSafeBox.getVault().getNotes().size());
        assertEquals("ideas", jackSafeBox.getVault().getNotes().getFirst().getTitle());
        assertEquals("Build an AI assistant for projects", createNoteResponse.getContent());
        assertEquals("ideas", noteRepository.findAll().getFirst().getTitle());

        DeleteNoteRequest deleteNoteRequest = new DeleteNoteRequest();
        deleteNoteRequest.setUsername("jack123");
        deleteNoteRequest.setTitle("ideas");
        deleteNoteRequest.setMasterPassword("Password123.");
        DeleteNoteResponse deleteNoteResponse = userService.deleteNote(deleteNoteRequest);

        jackSafeBox = userRepository.findByUsername("jack123");

        assertEquals(0, noteRepository.count());
        assertEquals(0, jackSafeBox.getVault().getNotes().size());
        assertEquals(0, vaultRepository.findAll().getFirst().getNotes().size());
        assertEquals("ideas", deleteNoteResponse.getTitle());
    }

    @Test
    public void userCreatesNote_UserLogsOutDeletesNote_ThrowsExceptionTest() {
        assertEquals(0, userRepository.count());

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("jack123");
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Password123.");
        RegisterResponse jackRegisterResponse = userService.signUp(registerRequest);

        assertEquals(1, userRepository.count());
        assertEquals("jack123", jackRegisterResponse.getUsername());

        CreateNoteRequest createNoteRequest = new CreateNoteRequest();
        createNoteRequest.setUsername("jack123");
        createNoteRequest.setTitle("ideas");
        createNoteRequest.setContent("Build an AI assistant for projects");
        CreateNoteResponse createNoteResponse = userService.createNote(createNoteRequest);

        User jackSafeBox = userRepository.findByUsername("jack123");

        assertEquals(1, vaultRepository.count());
        assertEquals(1, noteRepository.count());
        assertEquals(1, jackSafeBox.getVault().getNotes().size());
        assertEquals("ideas", jackSafeBox.getVault().getNotes().getFirst().getTitle());
        assertEquals("Build an AI assistant for projects", createNoteResponse.getContent());
        assertEquals("ideas", noteRepository.findAll().getFirst().getTitle());

        LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setUsername("jack123");
        LogoutResponse jackLogoutResponse = userService.logout(logoutRequest);

        jackSafeBox = userRepository.findByUsername("jack123");

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
        assertEquals(0, userRepository.count());

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("jack123");
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Password123.");
        RegisterResponse jackRegisterResponse = userService.signUp(registerRequest);

        assertEquals(1, userRepository.count());
        assertEquals("jack123", jackRegisterResponse.getUsername());

        CreateNoteRequest createNoteRequest = new CreateNoteRequest();
        createNoteRequest.setUsername("jack123");
        createNoteRequest.setTitle("ideas");
        createNoteRequest.setContent("Build an AI assistant for projects");
        CreateNoteResponse createNoteResponse = userService.createNote(createNoteRequest);

        User jackSafeBox = userRepository.findByUsername("jack123");

        assertEquals(1, vaultRepository.count());
        assertEquals(1, noteRepository.count());
        assertEquals(1, jackSafeBox.getVault().getNotes().size());
        assertEquals("ideas", jackSafeBox.getVault().getNotes().getFirst().getTitle());
        assertEquals("Build an AI assistant for projects", createNoteResponse.getContent());
        assertEquals("ideas", noteRepository.findAll().getFirst().getTitle());

        DeleteNoteRequest deleteNoteRequest = new DeleteNoteRequest();
        deleteNoteRequest.setUsername("jack123");
        deleteNoteRequest.setTitle("new ideas");
        deleteNoteRequest.setMasterPassword("Password123.");

        assertThrows(NoteNotFoundException.class, ()->userService.deleteNote(deleteNoteRequest));
    }

    @Test
    public void userDeletesNote_PasswordIsInvalid_ThrowsExceptionTest() {
        assertEquals(0, userRepository.count());

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("jack123");
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Password123.");
        RegisterResponse jackRegisterResponse = userService.signUp(registerRequest);

        assertEquals(1, userRepository.count());
        assertEquals("jack123", jackRegisterResponse.getUsername());

        CreateNoteRequest createNoteRequest = new CreateNoteRequest();
        createNoteRequest.setUsername("jack123");
        createNoteRequest.setTitle("ideas");
        createNoteRequest.setContent("Build an AI assistant for projects");
        CreateNoteResponse createNoteResponse = userService.createNote(createNoteRequest);

        User jackSafeBox = userRepository.findByUsername("jack123");

        assertEquals(1, vaultRepository.count());
        assertEquals(1, noteRepository.count());
        assertEquals(1, jackSafeBox.getVault().getNotes().size());
        assertEquals("ideas", jackSafeBox.getVault().getNotes().getFirst().getTitle());
        assertEquals("Build an AI assistant for projects", createNoteResponse.getContent());
        assertEquals("ideas", noteRepository.findAll().getFirst().getTitle());

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
    public void userSavesCreditCardTest() {
        assertEquals(0, userRepository.count());

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("jack123");
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Password123.");
        RegisterResponse jackRegisterResponse = userService.signUp(registerRequest);

        assertEquals(1, userRepository.count());
        assertEquals("jack123", jackRegisterResponse.getUsername());

        SaveCreditCardRequest saveCreditCardRequest = new SaveCreditCardRequest();
        saveCreditCardRequest.setUsername("jack123");
        saveCreditCardRequest.setTitle("gtb savings card");
        saveCreditCardRequest.setCardNumber("5399831619690403");
        saveCreditCardRequest.setExpiryDate("01/2025");
        saveCreditCardRequest.setPin("1234");
        saveCreditCardRequest.setCVV("567");
        saveCreditCardRequest.setAdditionalInformation("for personal use");
        SaveCreditCardResponse jackSaveCreditCardResponse = userService.saveCreditCard(saveCreditCardRequest);

        User jackSafeBox = userRepository.findByUsername("jack123");

        assertEquals(1, jackSafeBox.getVault().getCreditCards().size());
        assertEquals(1, creditCardRepository.count());
        assertEquals("gtb savings card", jackSafeBox.getVault().getCreditCards().getFirst().getTitle());
        assertEquals("gtb savings card", jackSaveCreditCardResponse.getTitle());
    }

    @Test
    public void userEditsCreditCardTest() {
        assertEquals(0, userRepository.count());

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("jack123");
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Password123.");
        RegisterResponse jackRegisterResponse = userService.signUp(registerRequest);

        assertEquals(1, userRepository.count());
        assertEquals("jack123", jackRegisterResponse.getUsername());

        SaveCreditCardRequest saveCreditCardRequest = new SaveCreditCardRequest();
        saveCreditCardRequest.setUsername("jack123");
        saveCreditCardRequest.setTitle("gtb savings card");
        saveCreditCardRequest.setCardNumber("5399831619690403");
        saveCreditCardRequest.setExpiryDate("01/2025");
        saveCreditCardRequest.setPin("1234");
        saveCreditCardRequest.setCVV("567");
        saveCreditCardRequest.setAdditionalInformation("for personal use");
        SaveCreditCardResponse jackSaveCreditCardResponse = userService.saveCreditCard(saveCreditCardRequest);

        User jackSafeBox = userRepository.findByUsername("jack123");

        assertEquals(1, jackSafeBox.getVault().getCreditCards().size());
        assertEquals(1, creditCardRepository.count());
        assertEquals("gtb savings card", jackSafeBox.getVault().getCreditCards().getFirst().getTitle());
        assertEquals("gtb savings card", jackSaveCreditCardResponse.getTitle());

        EditCreditCardRequest editCreditCardRequest = new EditCreditCardRequest();
        editCreditCardRequest.setUsername("jack123");
        editCreditCardRequest.setTitle("gtb savings card");
        editCreditCardRequest.setUpdateTitle("gtb current card");
        editCreditCardRequest.setUpdatedCardNumber("5313581000123430");
        editCreditCardRequest.setUpdatedCardNumber("01/2028");
        editCreditCardRequest.setUpdatedAdditionalInformation("for office use");
        EditCreditCardResponse jackEditCreditCardResponse = userService.editCreditCard(editCreditCardRequest);

        jackSafeBox = userRepository.findByUsername("jack123");

        assertEquals(1, jackSafeBox.getVault().getCreditCards().size());
        assertEquals("gtb current card", jackSafeBox.getVault().getCreditCards().getFirst().getTitle());
        assertEquals(1, creditCardRepository.count());
        assertEquals(jackSafeBox.getVault().getCreditCards().getFirst().getId(), jackEditCreditCardResponse.getId());
    }

    @Test
    public void userViewsCreditCardTest() {
        assertEquals(0, userRepository.count());

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("jack123");
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Password123.");
        RegisterResponse jackRegisterResponse = userService.signUp(registerRequest);

        assertEquals(1, userRepository.count());
        assertEquals("jack123", jackRegisterResponse.getUsername());

        SaveCreditCardRequest saveCreditCardRequest = new SaveCreditCardRequest();
        saveCreditCardRequest.setUsername("jack123");
        saveCreditCardRequest.setTitle("gtb savings card");
        saveCreditCardRequest.setCardNumber("5399831619690403");
        saveCreditCardRequest.setExpiryDate("01/2025");
        saveCreditCardRequest.setPin("1234");
        saveCreditCardRequest.setCVV("567");
        saveCreditCardRequest.setAdditionalInformation("for personal use");
        SaveCreditCardResponse jackSaveCreditCardResponse = userService.saveCreditCard(saveCreditCardRequest);

        User jackSafeBox = userRepository.findByUsername("jack123");
        String id = jackSafeBox.getVault().getCreditCards().getFirst().getId();

        assertEquals(1, jackSafeBox.getVault().getCreditCards().size());
        assertEquals(1, creditCardRepository.count());
        assertEquals("gtb savings card", jackSafeBox.getVault().getCreditCards().getFirst().getTitle());
        assertEquals("gtb savings card", jackSaveCreditCardResponse.getTitle());

        ViewCreditCardRequest viewCreditCardRequest = new ViewCreditCardRequest();
        viewCreditCardRequest.setUsername("jack123");
        viewCreditCardRequest.setTitle("gtb savings card");

        ViewCreditCardResponse jackViewCreditCardResponse = userService.viewCreditCard(viewCreditCardRequest);
        assertEquals(1, creditCardRepository.count());
        assertEquals("gtb savings card", jackViewCreditCardResponse.getTitle());
    }

    @Test
    public void userDeletesCreditCardTest(){
        assertEquals(0, userRepository.count());

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("jack123");
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Password123.");
        RegisterResponse jackRegisterResponse = userService.signUp(registerRequest);

        assertEquals(1, userRepository.count());
        assertEquals("jack123", jackRegisterResponse.getUsername());

        SaveCreditCardRequest saveCreditCardRequest = new SaveCreditCardRequest();
        saveCreditCardRequest.setUsername("jack123");
        saveCreditCardRequest.setTitle("gtb savings card");
        saveCreditCardRequest.setCardNumber("5399831619690403");
        saveCreditCardRequest.setExpiryDate("01/2025");
        saveCreditCardRequest.setPin("1234");
        saveCreditCardRequest.setCVV("567");
        saveCreditCardRequest.setAdditionalInformation("for personal use");
        SaveCreditCardResponse jackSaveCreditCardResponse = userService.saveCreditCard(saveCreditCardRequest);

        User jackSafeBox = userRepository.findByUsername("jack123");

        assertEquals(1, jackSafeBox.getVault().getCreditCards().size());
        assertEquals(1, creditCardRepository.count());
        assertEquals("gtb savings card", jackSafeBox.getVault().getCreditCards().getFirst().getTitle());
        assertEquals("gtb savings card", jackSaveCreditCardResponse.getTitle());

        DeleteCreditCardRequest deleteCreditCardRequest = new DeleteCreditCardRequest();
        deleteCreditCardRequest.setUsername("jack123");
        deleteCreditCardRequest.setTitle("gtb savings card");
        deleteCreditCardRequest.setMasterPassword("Password123.");
        DeleteCreditCardResponse jackDeleteCreditCardResponse = userService.deleteCreditCard(deleteCreditCardRequest);

        jackSafeBox = userRepository.findByUsername("jack123");

        assertEquals(0, jackSafeBox.getVault().getCreditCards().size());
        assertEquals(0, vaultRepository.findAll().getFirst().getCreditCards().size());
        assertEquals(0, creditCardRepository.count());
        assertEquals("gtb savings card", jackDeleteCreditCardResponse.getTitle());

    }

    @Test
    public void savePassportTest() {
        assertEquals(0, userRepository.count());

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("jack123");
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Password123.");
        RegisterResponse jackRegisterResponse = userService.signUp(registerRequest);

        assertEquals(1, userRepository.count());
        assertEquals("jack123", jackRegisterResponse.getUsername());

        SavePassportRequest savePassportRequest = new SavePassportRequest();
        savePassportRequest.setUsername("jack123");
        savePassportRequest.setTitle("my personal passport");
        savePassportRequest.setSurname("smith");
        savePassportRequest.setGivenNames("john scott");
        savePassportRequest.setNationality("british");
        savePassportRequest.setPlaceOfBirth("london");
        savePassportRequest.setDateOfBirth("");
        savePassportRequest.setPassportNumber("533401372");
        savePassportRequest.setIssueDate("");
        savePassportRequest.setExpiryDate("");
        SavePassportResponse jackSavePassportResponse = userService.savePassport(savePassportRequest);

        User jackSafeBox = userRepository.findByUsername("jack123");

        assertEquals(1, passportRepository.count());
        assertEquals(1, vaultRepository.findAll().getFirst().getPassports().size());
        assertEquals(1, jackSafeBox.getVault().getPassports().size());
        assertEquals("my personal passport", jackSafeBox.getVault().getPassports().getFirst().getTitle());
        assertEquals("my personal passport", jackSavePassportResponse.getTitle());
    }

    @Test
    public void userEditsPassportTest() {
        assertEquals(0, userRepository.count());

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("jack123");
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Password123.");
        RegisterResponse jackRegisterResponse = userService.signUp(registerRequest);

        assertEquals(1, userRepository.count());
        assertEquals("jack123", jackRegisterResponse.getUsername());

        SavePassportRequest savePassportRequest = new SavePassportRequest();
        savePassportRequest.setUsername("jack123");
        savePassportRequest.setTitle("my personal passport");
        savePassportRequest.setSurname("smith");
        savePassportRequest.setGivenNames("john scott");
//        savePassportRequest.setNationality("british");
//        savePassportRequest.setPlaceOfBirth("london");
//        savePassportRequest.setDateOfBirth("");
        savePassportRequest.setPassportNumber("533401372");
//        savePassportRequest.setIssueDate("");
//        savePassportRequest.setExpiryDate("");
        SavePassportResponse jackSavePassportResponse = userService.savePassport(savePassportRequest);

        User jackSafeBox = userRepository.findByUsername("jack123");

        assertEquals(1, passportRepository.count());
        assertEquals(1, vaultRepository.findAll().getFirst().getPassports().size());
        assertEquals(1, jackSafeBox.getVault().getPassports().size());
        assertEquals("my personal passport", jackSafeBox.getVault().getPassports().getFirst().getTitle());
        assertEquals("my personal passport", jackSavePassportResponse.getTitle());
        assertNull(jackSafeBox.getVault().getPassports().getFirst().getNationality());
        assertNull(jackSafeBox.getVault().getPassports().getFirst().getPlaceOfBirth());
        assertNull(jackSafeBox.getVault().getPassports().getFirst().getDateOfBirth());
        assertNull(jackSafeBox.getVault().getPassports().getFirst().getIssueDate());
        assertNull(jackSafeBox.getVault().getPassports().getFirst().getExpiryDate());

        EditPassportRequest editPassportRequest = new EditPassportRequest();
        editPassportRequest.setUsername("jack123");
        editPassportRequest.setTitle("my personal passport");
        editPassportRequest.setUpdatedNationality("british");
        editPassportRequest.setUpdatedPlaceOfBirth("london");
        editPassportRequest.setUpdatedDateOfBirth("");
        editPassportRequest.setUpdatedExpiryDate("");
        editPassportRequest.setUpdatedIssueDate("");
        editPassportRequest.setUpdatedExpiryDate("");

        jackSafeBox = userRepository.findByUsername("jack123");
        assertEquals(1, passportRepository.count());
        assertEquals(1, vaultRepository.findAll().getFirst().getPassports().size());
        assertEquals(1, jackSafeBox.getVault().getPassports().size());
        assertEquals("my personal passport", jackSafeBox.getVault().getPassports().getFirst().getTitle());
        assertEquals("my personal passport", jackSavePassportResponse.getTitle());
        assertEquals("", jackSafeBox.getVault().getPassports().getFirst().getIssueDate());
        assertEquals("", jackSafeBox.getVault().getPassports().getFirst().getExpiryDate());
    }

    @Test
    public void userViewsPassportTest() {
        assertEquals(0, userRepository.count());

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("jack123");
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Password123.");
        RegisterResponse jackRegisterResponse = userService.signUp(registerRequest);

        assertEquals(1, userRepository.count());
        assertEquals("jack123", jackRegisterResponse.getUsername());

        SavePassportRequest savePassportRequest = new SavePassportRequest();
        savePassportRequest.setUsername("jack123");
        savePassportRequest.setTitle("my personal passport");
        savePassportRequest.setSurname("smith");
        savePassportRequest.setGivenNames("john scott");
        savePassportRequest.setNationality("british");
        savePassportRequest.setPlaceOfBirth("london");
        savePassportRequest.setDateOfBirth("");
        savePassportRequest.setPassportNumber("533401372");
        savePassportRequest.setIssueDate("");
        savePassportRequest.setExpiryDate("");
        SavePassportResponse jackSavePassportResponse = userService.savePassport(savePassportRequest);

        User jackSafeBox = userRepository.findByUsername("jack123");

        assertEquals(1, passportRepository.count());
        assertEquals(1, vaultRepository.findAll().getFirst().getPassports().size());
        assertEquals(1, jackSafeBox.getVault().getPassports().size());
        assertEquals("my personal passport", jackSafeBox.getVault().getPassports().getFirst().getTitle());
        assertEquals("my personal passport", jackSavePassportResponse.getTitle());

        ViewPassportRequest viewPassportRequest = new ViewPassportRequest();
        viewPassportRequest.setUsername("jack123");
        viewPassportRequest.setTitle("my personal passport");
        ViewPassportResponse viewPassportResponse = userService.viewPassport(viewPassportRequest);

    }

    @Test
    public void deletePassportTest() {
        assertEquals(0, userRepository.count());

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("jack123");
        registerRequest.setMasterPassword("Password123.");
        registerRequest.setConfirmMasterPassword("Password123.");
        RegisterResponse jackRegisterResponse = userService.signUp(registerRequest);

        assertEquals(1, userRepository.count());
        assertEquals("jack123", jackRegisterResponse.getUsername());

        SavePassportRequest savePassportRequest = new SavePassportRequest();
        savePassportRequest.setUsername("jack123");
        savePassportRequest.setTitle("my personal passport");
        savePassportRequest.setSurname("smith");
        savePassportRequest.setGivenNames("john scott");
        savePassportRequest.setNationality("british");
        savePassportRequest.setPlaceOfBirth("london");
        savePassportRequest.setDateOfBirth("");
        savePassportRequest.setPassportNumber("533401372");
        savePassportRequest.setIssueDate("");
        savePassportRequest.setExpiryDate("");
        SavePassportResponse jackSavePassportResponse = userService.savePassport(savePassportRequest);

        User jackSafeBox = userRepository.findByUsername("jack123");

        assertEquals(1, passportRepository.count());
        assertEquals(1, vaultRepository.findAll().getFirst().getPassports().size());
        assertEquals(1, jackSafeBox.getVault().getPassports().size());
        assertEquals("my personal passport", jackSafeBox.getVault().getPassports().getFirst().getTitle());
        assertEquals("my personal passport", jackSavePassportResponse.getTitle());

        DeletePassportRequest deletePassportRequest = new DeletePassportRequest();
        deletePassportRequest.setTitle("my personal passport");
        deletePassportRequest.setUsername("jack123");
        deletePassportRequest.setMasterPassword("Password123.");
        DeletePassportResponse jackDeletePassportResponse = userService.deletePassport(deletePassportRequest);

        jackSafeBox = userRepository.findByUsername("jack123");

        assertEquals(0, passportRepository.count());
        assertEquals(0, vaultRepository.findAll().getFirst().getPassports().size());
        assertEquals(0, jackSafeBox.getVault().getPassports().size());
        assertEquals("my personal passport", jackDeletePassportResponse.getTitle());
    }



}