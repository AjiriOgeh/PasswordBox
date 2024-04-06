package com.passwordbox.services;

import com.passwordbox.data.models.LoginInfo;
import com.passwordbox.data.models.Note;
import com.passwordbox.data.models.User;
import com.passwordbox.data.models.Vault;
import com.passwordbox.data.repositories.UserRepository;
import com.passwordbox.dataTransferObjects.requests.*;
import com.passwordbox.dataTransferObjects.responses.*;
import com.passwordbox.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import static com.passwordbox.utilities.Mappers.*;

@Service
public class UserServiceImplementation implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VaultService vaultService;

    @Override
    public RegisterResponse signUp(RegisterRequest registerRequest) {
        validateUsername(registerRequest);
        validatePassword(registerRequest);
        Vault newUserVault = vaultService.createVault();
        User newUser = registerRequestMap(registerRequest, newUserVault);
        userRepository.save(newUser);
        return registerResponseMap(newUser);
    }

    private void validateUsername(RegisterRequest registerRequest) {
        if (registerRequest.getUsername() == null) throw new IllegalArgumentException("Username cannot be null. Please enter a valid username.");
        if (registerRequest.getUsername().isEmpty()) throw new IllegalArgumentException("Username cannot be empty. Please enter a valid username.");
        if (registerRequest.getUsername().contains(" ")) throw new IllegalArgumentException("Username cannot space character. Please enter a valid username.");
        if (doesUsernameExist(registerRequest.getUsername())) throw new UsernameExistsException("Username Exists. Please Try Again");
    }

    private void validatePassword(RegisterRequest registerRequest) {
        if (registerRequest.getMasterPassword() == null) throw new IllegalArgumentException("Password cannot be null. Please enter a valid password");
        if (!registerRequest.getMasterPassword().equals(registerRequest.getConfirmMasterPassword())) throw new IllegalArgumentException("Passwords do not match. Please Try again");
        if (registerRequest.getMasterPassword().isEmpty()) throw new IllegalArgumentException("Password field cannot be empty. Please enter a valid password.");
        if (registerRequest.getMasterPassword().length() < 10) throw new IllegalArgumentException("Password is less than 10 characters. Please Try again.");
        //validate that the password contains other characters
    }

    private boolean doesUsernameExist(String username) {
        User user = userRepository.findByUsername(username);
        return user != null;
    }

    @Override
    public LogoutResponse logout(LogoutRequest logoutRequest) {
        User user = userRepository.findByUsername(logoutRequest.getUsername());
        if (user == null) throw new UserNotFoundException(String.format("%s does not exist.", logoutRequest.getUsername()));
        user.setLocked(true);
        userRepository.save(user);
        return logoutResponseMap(user);
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername());
        if (user == null) throw new UserNotFoundException("Invalid Login Details. Please Try Again");
        if (!user.getMasterPassword().equals(loginRequest.getPassword())) throw new InvalidPasswordException("Invalid Login Details. Please Try Again");
        user.setLocked(false);
        userRepository.save(user);
        return loginResponseMap(user);
    }

    @Override
    public SaveNewLoginInfoResponse saveNewLoginInfo(SaveNewLoginInfoRequest saveNewLoginInfoRequest) {
        User user = userRepository.findByUsername(saveNewLoginInfoRequest.getUsername());
        if (user == null) throw new UserNotFoundException(String.format("User %s does not exist.", saveNewLoginInfoRequest.getUsername()));
        if (user.isLocked()) throw new ProfileLockStateException("Please Login to Save Login Info");
        LoginInfo loginInfo = vaultService.saveNewLogin(saveNewLoginInfoRequest, user.getVault());
        userRepository.save(user);
        return saveNewLoginInfoResponseMap(loginInfo);
    }

    @Override
    public EditLoginInfoResponse editLoginInformation(EditLoginInfoRequest editLoginInfoRequest) {
        User user = userRepository.findByUsername(editLoginInfoRequest.getUsername());
        if (user == null) throw new UserNotFoundException(String.format("User %s does not exist.", editLoginInfoRequest.getUsername()));
        if (user.isLocked()) throw new ProfileLockStateException("Please Login to Edit Login Info");
        LoginInfo loginInfo = vaultService.editLoginInformation(editLoginInfoRequest, user.getVault());
        userRepository.save(user);
        return editLoginInfoResponseMap(loginInfo);
    }

    @Override
    public ViewLoginInfoResponse viewLoginInfo(ViewLoginInfoRequest viewLoginInfoRequest) {
        User user = userRepository.findByUsername(viewLoginInfoRequest.getUsername());
        if (user == null) throw new UserNotFoundException(String.format("User %s does not exist.", viewLoginInfoRequest.getUsername()));
        if (user.isLocked()) throw new ProfileLockStateException("Please Login to view Login Info");
        LoginInfo loginInfo = findLoginInfoInVault(viewLoginInfoRequest.getTitle(), user.getVault());
        return viewLoginInfoResponseMap(loginInfo);
    }

    private LoginInfo findLoginInfoInVault(String title, Vault vault) {
        for(int count = 0; count < vault.getLoginInfos().size(); count++){
            if (vault.getLoginInfos().get(count).getTitle().equals(title))
                return vault.getLoginInfos().get(count);
        }
        throw new LoginInfoNotFoundException("Login Info does not Exist. Please Try Again");
    }

    @Override
    public DeleteLoginInfoResponse deleteLoginInfo(DeleteLoginInfoRequest deleteLoginInfoRequest) {
        User user = userRepository.findByUsername(deleteLoginInfoRequest.getUsername());
        if (user == null) throw new UserNotFoundException(String.format("User %s does not exist.", deleteLoginInfoRequest.getUsername()));
        if (user.isLocked()) throw new ProfileLockStateException("Please Login to delete Login Info");
        if (!user.getMasterPassword().equals(deleteLoginInfoRequest.getMasterPassword())) throw new InvalidPasswordException("Incorrect password. Please Try again");
        return vaultService.deleteLoginInformation(deleteLoginInfoRequest, user.getVault());
    }

    @Override
    public CreateNoteResponse createNote(CreateNoteRequest createNoteRequest) {
        User user = userRepository.findByUsername(createNoteRequest.getUsername());
        if (user == null) throw new UserNotFoundException(String.format("User %s does not exist.", createNoteRequest.getUsername()));
        if (user.isLocked()) throw new ProfileLockStateException("Please Login to create note");
        Note note = vaultService.createNote(createNoteRequest, user.getVault());
        userRepository.save(user);
        return createNoteResponseMap(note);
    }

    @Override
    public EditNoteResponse editNote(EditNoteRequest editNoteRequest) {
        User user = userRepository.findByUsername(editNoteRequest.getUsername());
        if (user == null) throw new UserNotFoundException(String.format("User %s does not exist.", editNoteRequest.getUsername()));
        if (user.isLocked()) throw new ProfileLockStateException("Please Login to Edit Note");
        Note note = vaultService.editNote(editNoteRequest, user.getVault());
        userRepository.save(user);
        return editNoteResponseMap(note);
    }

    @Override
    public ViewNoteResponse viewNote(ViewNoteRequest viewNoteRequest) {
        User user = userRepository.findByUsername(viewNoteRequest.getUsername());
        if (user == null) throw new UserNotFoundException(String.format("User %s does not exist.", viewNoteRequest.getUsername()));
        if (user.isLocked()) throw new ProfileLockStateException("Please Login to view Note");
        Note note = findNoteInVault(viewNoteRequest.getTitle(), user.getVault());
        return viewNoteResponseMap(note);
    }

    @Override
    public DeleteNoteResponse deleteNote(DeleteNoteRequest deleteNoteRequest) {
        User user = userRepository.findByUsername(deleteNoteRequest.getUsername());
        if (user == null) throw new UserNotFoundException(String.format("User %s does not exist.", deleteNoteRequest.getUsername()));
        if (user.isLocked()) throw new ProfileLockStateException("Please Login to view Login Information");
        if (!user.getMasterPassword().equals(deleteNoteRequest.getMasterPassword())) throw new InvalidPasswordException("Incorrect password. Please Try again");
        return vaultService.deleteNote(deleteNoteRequest, user.getVault());
    }

    private Note findNoteInVault(String title, Vault vault) {
        for(int count = 0; count < vault.getNotes().size(); count++){
            if (vault.getNotes().get(count).getTitle().equals(title))
                return vault.getNotes().get(count);
        }
        throw new NoteNotFoundException("Note does not Exist. Please Try Again");
    }


}
