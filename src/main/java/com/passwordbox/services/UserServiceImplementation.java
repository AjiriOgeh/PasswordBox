package com.passwordbox.services;

import com.passwordbox.data.models.*;
import com.passwordbox.data.repositories.UserRepository;
import com.passwordbox.dataTransferObjects.requests.*;
import com.passwordbox.dataTransferObjects.responses.*;
import com.passwordbox.exceptions.*;
import com.passwordbox.utilities.PasscodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import static com.passwordbox.utilities.FindDetails.*;
import static com.passwordbox.utilities.Mappers.*;

@Service
public class UserServiceImplementation implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VaultService vaultService;

    @Override
    public RegisterResponse signUp(RegisterRequest registerRequest) {
        validateUsername(registerRequest.getUsername());
        validatePassword(registerRequest.getMasterPassword(), registerRequest.getConfirmMasterPassword());
        Vault newUserVault = vaultService.createVault();
        User newUser = registerRequestMap(registerRequest, newUserVault);
        userRepository.save(newUser);
        return registerResponseMap(newUser);
    }

    private void validateUsername(String username) {
        if (username == null) throw new IllegalArgumentException("Username cannot be null. Please enter a valid username.");
        if (username.isEmpty()) throw new IllegalArgumentException("Username cannot be empty. Please enter a valid username.");
        if (username.contains(" ")) throw new IllegalArgumentException("Username cannot space character. Please enter a valid username.");
        if (doesUsernameExist(username.toLowerCase())) throw new UsernameExistsException("Username Exists. Please Try Again");
    }

    private void validatePassword(String masterPassword, String confirmMasterPassword) {
        if (masterPassword == null) throw new IllegalArgumentException("Password cannot be null. Please enter a valid password");
        if (!masterPassword.equals(confirmMasterPassword)) throw new IllegalArgumentException("Passwords do not match. Please Try again");
        if (masterPassword.isEmpty()) throw new IllegalArgumentException("Password field cannot be empty. Please enter a valid password.");
        if (masterPassword.length() < 10) throw new IllegalArgumentException("Password is less than 10 characters. Please Try again.");
    }

    private boolean doesUsernameExist(String username) {
        User user = userRepository.findByUsername(username.toLowerCase());
        return user != null;
    }

    @Override
    public LogoutResponse logout(LogoutRequest logoutRequest) {
        User user = userRepository.findByUsername(logoutRequest.getUsername().toLowerCase());
        if (user == null) throw new UserNotFoundException(String.format("%s does not exist.", logoutRequest.getUsername()));
        user.setLocked(true);
        userRepository.save(user);
        return logoutResponseMap(user);
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername().toLowerCase());
        if (user == null) throw new UserNotFoundException("Invalid Login Details. Please Try Again");
        if (!user.getMasterPassword().equals(loginRequest.getPassword())) throw new InvalidPasswordException("Invalid Login Details. Please Try Again");
        user.setLocked(false);
        userRepository.save(user);
        return loginResponseMap(user);
    }

    @Override
    public SaveNewLoginInfoResponse saveNewLoginInfo(SaveNewLoginInfoRequest saveNewLoginInfoRequest) throws Exception {
        User user = userRepository.findByUsername(saveNewLoginInfoRequest.getUsername().toLowerCase());
        if (user == null) throw new UserNotFoundException(String.format("User %s does not exist.", saveNewLoginInfoRequest.getUsername()));
        if (user.isLocked()) throw new ProfileLockStateException("Please Login to Save Login Info");
        LoginInfo loginInfo = vaultService.saveNewLoginInfo(saveNewLoginInfoRequest, user.getVault());
        userRepository.save(user);
        return saveNewLoginInfoResponseMap(loginInfo);
    }

    @Override
    public EditLoginInfoResponse editLoginInfo(EditLoginInfoRequest editLoginInfoRequest) throws Exception {
        User user = userRepository.findByUsername(editLoginInfoRequest.getUsername().toLowerCase());
        if (user == null) throw new UserNotFoundException(String.format("User %s does not exist.", editLoginInfoRequest.getUsername()));
        if (user.isLocked()) throw new ProfileLockStateException("Please Login to Edit Login Info");
        LoginInfo loginInfo = vaultService.editLoginInfo(editLoginInfoRequest, user.getVault());
        userRepository.save(user);
        return editLoginInfoResponseMap(loginInfo);
    }

    @Override
    public ViewLoginInfoResponse viewLoginInfo(ViewLoginInfoRequest viewLoginInfoRequest) throws Exception {
        User user = userRepository.findByUsername(viewLoginInfoRequest.getUsername().toLowerCase());
        if (user == null) throw new UserNotFoundException(String.format("User %s does not exist.", viewLoginInfoRequest.getUsername()));
        if (user.isLocked()) throw new ProfileLockStateException("Please Login to view Login Info");
        LoginInfo loginInfo = findLoginInfoInVault(viewLoginInfoRequest.getTitle().toLowerCase(), user.getVault());
        return viewLoginInfoResponseMap(loginInfo);
    }

    @Override
    public DeleteLoginInfoResponse deleteLoginInfo(DeleteLoginInfoRequest deleteLoginInfoRequest) {
        User user = userRepository.findByUsername(deleteLoginInfoRequest.getUsername().toLowerCase());
        if (user == null) throw new UserNotFoundException(String.format("User %s does not exist.", deleteLoginInfoRequest.getUsername()));
        if (user.isLocked()) throw new ProfileLockStateException("Please Login to delete Login Info");
        if (!user.getMasterPassword().equals(deleteLoginInfoRequest.getMasterPassword())) throw new InvalidPasswordException("Incorrect password. Please Try again");
        return vaultService.deleteLoginInfo(deleteLoginInfoRequest, user.getVault());
    }

    @Override
    public CreateNoteResponse createNote(CreateNoteRequest createNoteRequest) throws Exception {
        User user = userRepository.findByUsername(createNoteRequest.getUsername().toLowerCase());
        if (user == null) throw new UserNotFoundException(String.format("User %s does not exist.", createNoteRequest.getUsername()));
        if (user.isLocked()) throw new ProfileLockStateException("Please Login to create note");
        Note note = vaultService.createNote(createNoteRequest, user.getVault());
        userRepository.save(user);
        return createNoteResponseMap(note);
    }

    @Override
    public EditNoteResponse editNote(EditNoteRequest editNoteRequest) throws Exception {
        User user = userRepository.findByUsername(editNoteRequest.getUsername().toLowerCase());
        if (user == null) throw new UserNotFoundException(String.format("User %s does not exist.", editNoteRequest.getUsername()));
        if (user.isLocked()) throw new ProfileLockStateException("Please Login to Edit Note");
        Note note = vaultService.editNote(editNoteRequest, user.getVault());
        userRepository.save(user);
        return editNoteResponseMap(note);
    }

    @Override
    public ViewNoteResponse viewNote(ViewNoteRequest viewNoteRequest) throws Exception {
        User user = userRepository.findByUsername(viewNoteRequest.getUsername().toLowerCase());
        if (user == null) throw new UserNotFoundException(String.format("User %s does not exist.", viewNoteRequest.getUsername()));
        if (user.isLocked()) throw new ProfileLockStateException("Please Login to view Note");
        Note note = findNoteInVault(viewNoteRequest.getTitle(), user.getVault());
        return viewNoteResponseMap(note);
    }

    @Override
    public DeleteNoteResponse deleteNote(DeleteNoteRequest deleteNoteRequest) {
        User user = userRepository.findByUsername(deleteNoteRequest.getUsername().toLowerCase());
        if (user == null) throw new UserNotFoundException(String.format("User %s does not exist.", deleteNoteRequest.getUsername()));
        if (user.isLocked()) throw new ProfileLockStateException("Please Login to view Login Information");
        if (!user.getMasterPassword().equals(deleteNoteRequest.getMasterPassword())) throw new InvalidPasswordException("Incorrect password. Please Try again");
        return vaultService.deleteNote(deleteNoteRequest, user.getVault());
    }

    public GeneratePasswordResponse generatePassword(GeneratePasswordRequest generatePasswordRequest) {
        validatePasscodeLength(generatePasswordRequest.getLength());
        String password = PasscodeGenerator.generatePassword(Integer.parseInt(generatePasswordRequest.getLength()));
        return generatePasswordResponseMap(password);
    }
    private static void validatePasscodeLength(String passcodeLength) {
        if (!passcodeLength.matches("\\d+")) throw new InvalidPasscodeLengthException("Please Enter a Valid Number");
        if (Integer.parseInt(passcodeLength) < 1 || Integer.parseInt(passcodeLength) > 30) throw new InvalidPasscodeLengthException("Please Enter a Number between 1 - 30");
    }

    @Override
    public GeneratePinResponse generatePin(GeneratePinRequest generatePinRequest) {
        validatePasscodeLength(generatePinRequest.getLength());
        String pin = PasscodeGenerator.generatePin(Integer.parseInt(generatePinRequest.getLength()));
        return generatePinResponseMap(pin);
    }

    @Override
    public SaveCreditCardResponse saveCreditCard(SaveCreditCardRequest saveCreditCardRequest) throws Exception {
        User user = userRepository.findByUsername(saveCreditCardRequest.getUsername());
        if (user == null) throw new UserNotFoundException(String.format("User %s does not exist.", saveCreditCardRequest.getUsername()));
        if (user.isLocked()) throw new ProfileLockStateException("Please Login to Save Login Info");
        CreditCard creditCard = vaultService.saveCreditCard(saveCreditCardRequest, user.getVault());
        userRepository.save(user);
        return saveCreditCardResponseMap(creditCard);
    }


    @Override
    public EditCreditCardResponse editCreditCard(EditCreditCardRequest editCreditCardRequest) throws Exception {
        User user = userRepository.findByUsername(editCreditCardRequest.getUsername());
        if (user == null) throw new UserNotFoundException(String.format("User %s does not exist.", editCreditCardRequest.getUsername()));
        if (user.isLocked()) throw new ProfileLockStateException("Please login to edit credit card");
        CreditCard creditCard = vaultService.editCreditCard(editCreditCardRequest, user.getVault());
        return editCreditCardResponseMap(creditCard);
    }

    @Override
    public ViewCreditCardResponse viewCreditCard(ViewCreditCardRequest viewCreditCardRequest) throws Exception {
        User user = userRepository.findByUsername(viewCreditCardRequest.getUsername());
        CreditCard creditCard = findCreditCardInVault(viewCreditCardRequest.getTitle(), user.getVault());
        return viewCreditCardResponseMap(creditCard);
    }

    @Override
    public DeleteCreditCardResponse deleteCreditCard(DeleteCreditCardRequest deleteCreditCardRequest) {
        User user = userRepository.findByUsername(deleteCreditCardRequest.getUsername());
        //if (!user.getMasterPassword().equals(deleteCreditCardRequest.getMasterPassword()));
        return vaultService.deleteCreditCard(deleteCreditCardRequest, user.getVault());
    }



}

