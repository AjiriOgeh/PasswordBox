package com.passwordbox.utilities;

import com.passwordbox.data.models.*;
import com.passwordbox.dataTransferObjects.requests.*;
import com.passwordbox.dataTransferObjects.responses.*;

import java.time.format.DateTimeFormatter;

import static com.passwordbox.utilities.PasscodeGenerator.generatePassword;

public class Mappers {
    public static User registerRequestMap(RegisterRequest registerRequest, Vault vault) {
        User newUser = new User();
        newUser.setUsername(registerRequest.getUsername().toLowerCase());
        newUser.setMasterPassword(registerRequest.getMasterPassword());
        newUser.setVault(vault);
        return newUser;
    }

    public static RegisterResponse registerResponseMap(User user) {
        RegisterResponse registerResponse = new RegisterResponse();
        registerResponse.setId(user.getId());
        registerResponse.setUsername(user.getUsername());
        registerResponse.setDateOfRegistration(user.getDateOfRegistration().format(DateTimeFormatter.ofPattern("MMM dd, yyyy")));
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
        loginInfo.setTitle(saveNewLoginInfoRequest.getTitle().toLowerCase());
        loginInfo.setWebsite(saveNewLoginInfoRequest.getWebsite());
        loginInfo.setLoginId(saveNewLoginInfoRequest.getLoginId());
        if (saveNewLoginInfoRequest.getPassword() == null) loginInfo.setPassword(generatePassword(16));
        else loginInfo.setPassword(saveNewLoginInfoRequest.getPassword());
        return loginInfo;
    }

    public static SaveNewLoginInfoResponse saveNewLoginInfoResponseMap(LoginInfo loginInfo){
        SaveNewLoginInfoResponse saveNewLoginInfoResponse = new SaveNewLoginInfoResponse();
        saveNewLoginInfoResponse.setId(loginInfo.getId());
        saveNewLoginInfoResponse.setTitle(loginInfo.getTitle().toLowerCase().trim());
        saveNewLoginInfoResponse.setWebsite(loginInfo.getWebsite());
        saveNewLoginInfoResponse.setLoginId(loginInfo.getLoginId());
        saveNewLoginInfoResponse.setPassword(loginInfo.getPassword());
        return saveNewLoginInfoResponse;
    }

    public static LoginInfo editLoginInfoRequestMap(EditLoginInfoRequest editLoginInfoRequest, LoginInfo loginInfo) {
        if (editLoginInfoRequest.getEditedTitle() != null) loginInfo.setTitle(editLoginInfoRequest.getEditedTitle().toLowerCase().trim());
        if (editLoginInfoRequest.getEditedLoginId() != null) loginInfo.setLoginId(editLoginInfoRequest.getEditedLoginId());
        if (editLoginInfoRequest.getEditedWebsite() != null) loginInfo.setWebsite(editLoginInfoRequest.getEditedWebsite());
        if (editLoginInfoRequest.getEditedPassword() != null) loginInfo.setPassword(editLoginInfoRequest.getEditedPassword());
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
        viewLoginInfoResponse.setId(loginInfo.getId());
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
        newNote.setTitle(createNoteRequest.getTitle().toLowerCase().trim());
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
        if (editNoteRequest.getEditedTitle() != null) note.setTitle(editNoteRequest.getEditedTitle().toLowerCase().trim());
        if (editNoteRequest.getEditedContent() != null) note.setContent(editNoteRequest.getEditedContent());
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

    public static GeneratePasswordResponse generatePasswordResponseMap(String password) {
        GeneratePasswordResponse generatePasswordResponse = new GeneratePasswordResponse();
        generatePasswordResponse.setPassword(password);
        generatePasswordResponse.setLength(password.length());
        return generatePasswordResponse;
    }

    public static GeneratePinResponse generatePinResponseMap(String pin) {
        GeneratePinResponse generatePinResponse = new GeneratePinResponse();
        generatePinResponse.setPin(pin);
        generatePinResponse.setLength(pin.length());
        return generatePinResponse;
    }

    public static CreditCard saveCreditCardRequestMap(SaveCreditCardRequest saveCreditCardRequest) {
        CreditCard creditCard = new CreditCard();
        creditCard.setTitle(saveCreditCardRequest.getTitle());
        creditCard.setCardNumber(saveCreditCardRequest.getCardNumber());
        creditCard.setCardType(saveCreditCardRequest.getCardType());
        creditCard.setCVV(saveCreditCardRequest.getCVV());
        creditCard.setAdditionalInformation(saveCreditCardRequest.getAdditionalInformation());
        return creditCard;
    }

    public static SaveCreditCardResponse saveCreditCardResponseMap(CreditCard creditCard) {
        SaveCreditCardResponse saveCreditCardResponse = new SaveCreditCardResponse();
        saveCreditCardResponse.setId(creditCard.getId());
        saveCreditCardResponse.setTitle(creditCard.getTitle());
        saveCreditCardResponse.setCreditCardNumber(creditCard.getCardNumber());
        return saveCreditCardResponse;
    }

    public static CreditCard editCreditCardRequestMap(EditCreditCardRequest editCreditCardRequest, CreditCard creditCard) {
        if (editCreditCardRequest.getUpdateTitle() != null) creditCard.setTitle(editCreditCardRequest.getUpdateTitle());
        if (editCreditCardRequest.getUpdatedCardNumber() != null) creditCard.setCardNumber(editCreditCardRequest.getUpdatedCardNumber());
        if (editCreditCardRequest.getUpdatedPin() != null) creditCard.setPin(editCreditCardRequest.getUpdatedPin());
        if (editCreditCardRequest.getUpdatedAdditionalInformation() != null) creditCard.setAdditionalInformation(editCreditCardRequest.getUpdatedAdditionalInformation());
        if (editCreditCardRequest.getUpdatedCVV() != null) creditCard.setCVV(editCreditCardRequest.getUpdatedCVV());
        if (editCreditCardRequest.getUpdatedExpiryDate() != null) creditCard.setExpiryDate(editCreditCardRequest.getUpdatedExpiryDate());
        return creditCard;
    }

    public static EditCreditCardResponse editCreditCardResponseMap(CreditCard creditCard) {
        EditCreditCardResponse editCreditCardResponse = new EditCreditCardResponse();
        editCreditCardResponse.setId(creditCard.getId());
        editCreditCardResponse.setTitle(creditCard.getTitle());
        return editCreditCardResponse;
    }

    public static ViewCreditCardResponse viewCreditCardResponseMap(CreditCard creditCard) {
        ViewCreditCardResponse viewCreditCardResponse = new ViewCreditCardResponse();
        viewCreditCardResponse.setId(creditCard.getId());
        viewCreditCardResponse.setTitle(creditCard.getTitle());
        return viewCreditCardResponse;
    }

    public static DeleteCreditCardResponse deleteCreditCardResponseMap(CreditCard creditCard) {
        DeleteCreditCardResponse deleteCreditCardResponse = new DeleteCreditCardResponse();
        deleteCreditCardResponse.setId(creditCard.getId());
        deleteCreditCardResponse.setTitle(creditCard.getTitle());
        return deleteCreditCardResponse;
    }

    public static Passport savePassportRequestMap(SavePassportRequest savePassportRequest) {
        Passport passport = new Passport();
        passport.setTitle(savePassportRequest.getTitle());
        passport.setPassportNumber(savePassportRequest.getPassportNumber());
        passport.setDateOfBirth(savePassportRequest.getDateOfBirth());
        passport.setNationality(savePassportRequest.getNationality());
        passport.setSurname(savePassportRequest.getSurname());
        passport.setGivenNames(savePassportRequest.getGivenNames());
        passport.setIssueDate(savePassportRequest.getIssueDate());
        passport.setExpiryDate(savePassportRequest.getExpiryDate());
        return passport;
    }

    public static SavePassportResponse savePassportResponseMap(Passport passport) {
        SavePassportResponse savePassportResponse = new SavePassportResponse();
        savePassportResponse.setId(passport.getId());
        savePassportResponse.setTitle(passport.getTitle());
        return savePassportResponse;
    }

    public static DeletePassportResponse deletePassportResponseMap(Passport passport) {
        DeletePassportResponse deletePassportResponse = new DeletePassportResponse();
        deletePassportResponse.setId(passport.getId());
        deletePassportResponse.setTitle(passport.getTitle());
        return deletePassportResponse;
    }
}
