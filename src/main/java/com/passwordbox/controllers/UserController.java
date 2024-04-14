package com.passwordbox.controllers;

import com.passwordbox.dataTransferObjects.requests.*;
import com.passwordbox.dataTransferObjects.responses.ApiResponse;
import com.passwordbox.dataTransferObjects.responses.ViewCreditCardResponse;
import com.passwordbox.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/Signup")
    public ResponseEntity<?> signUp(@RequestBody RegisterRequest registerRequest) {
        try {
            return new ResponseEntity<>(new ApiResponse(true, userService.signUp(registerRequest)), HttpStatus.OK);
        }
        catch (Exception error) {
            return new ResponseEntity<>(new ApiResponse(false, error.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/Logout")
    public ResponseEntity<?> logout(@RequestBody LogoutRequest logoutRequest) {
        try {
            return new ResponseEntity<>(new ApiResponse(true, userService.logout(logoutRequest)), HttpStatus.OK);
        }
        catch (Exception error) {
            return new ResponseEntity<>(new ApiResponse(false, error.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/Login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            return new ResponseEntity<>(new ApiResponse(true, userService.login(loginRequest)), HttpStatus.OK);
        }
        catch (Exception error) {
            return new ResponseEntity<>(new ApiResponse(false, error.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/SaveLoginInfo")
    public ResponseEntity<?> saveNewLoginInfo(@RequestBody SaveNewLoginInfoRequest saveNewLoginInfoRequest) {
        try {
            return new ResponseEntity<>(new ApiResponse(true, userService.saveNewLoginInfo(saveNewLoginInfoRequest)), HttpStatus.CREATED);
        }
        catch (Exception error) {
            return new ResponseEntity<>(new ApiResponse(false, error.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/EditLoginInfo")
    public ResponseEntity<?> editLoginInfo(@RequestBody EditLoginInfoRequest editLoginInfoRequest) {
        try {
            return new ResponseEntity<>(new ApiResponse(true, userService.editLoginInfo(editLoginInfoRequest)), HttpStatus.OK);
        } catch (Exception error) {
            return new ResponseEntity<>(new ApiResponse(false, error.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/ViewLoginInfo")
    public ResponseEntity<?> viewLoginInfo(@RequestBody ViewLoginInfoRequest viewLoginInfoRequest) {
        try {
            return new ResponseEntity<>(new ApiResponse(true, userService.viewLoginInfo(viewLoginInfoRequest)), HttpStatus.OK);
        }
        catch (Exception error) {
            return new ResponseEntity<>(new ApiResponse(false, error.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/DeleteLoginInfo")
    public ResponseEntity<?> deleteLoginInfo(@RequestBody DeleteLoginInfoRequest deleteLoginInfoRequest) {
        try {
            return new ResponseEntity<>(new ApiResponse(true, userService.deleteLoginInfo(deleteLoginInfoRequest)), HttpStatus.OK);
        }
        catch (Exception error) {
            return new ResponseEntity<>(new ApiResponse(false, error.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/CreateNote")
    public ResponseEntity<?> createNote(@RequestBody CreateNoteRequest createNoteRequest) {
        try {
            return new ResponseEntity<>(new ApiResponse(true, userService.createNote(createNoteRequest)), HttpStatus.CREATED);
        }
        catch (Exception error) {
            return new ResponseEntity<>(new ApiResponse(false, error.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/EditNote")
    public ResponseEntity<?> editNote(@RequestBody EditNoteRequest editNoteRequest) {
        try {
            return new ResponseEntity<>(new ApiResponse(true, userService.editNote(editNoteRequest)), HttpStatus.OK);
        }
        catch (Exception error) {
            return new ResponseEntity<>(new ApiResponse(false, error.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/ViewNote")
    public ResponseEntity<?> viewNote(@RequestBody ViewNoteRequest viewNoteRequest) {
        try {
            return new ResponseEntity<>(new ApiResponse(true, userService.viewNote(viewNoteRequest)), HttpStatus.OK);
        }
        catch (Exception error) {
            return new ResponseEntity<>(new ApiResponse(false, error.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/DeleteNote")
    public ResponseEntity<?> deleteNote(@RequestBody DeleteNoteRequest deleteNoteRequest) {
        try {
            return new ResponseEntity<>(new ApiResponse(true, userService.deleteNote(deleteNoteRequest)), HttpStatus.OK);
        }
        catch (Exception error) {
            return new ResponseEntity<>(new ApiResponse(false, error.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/GeneratePassword")
    public ResponseEntity<?> generatePassword(@RequestBody GeneratePasswordRequest generatePasswordRequest) {
        try {
            return new ResponseEntity<>(new ApiResponse(true, userService.generatePassword(generatePasswordRequest)), HttpStatus.CREATED);
        }
        catch (Exception error) {
            return new ResponseEntity<>(new ApiResponse(false, error.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/GeneratePin")
    public ResponseEntity<?> generatePin(@RequestBody GeneratePinRequest generatePinRequest) {
        try {
            return new ResponseEntity<>(new ApiResponse(true, userService.generatePin(generatePinRequest)), HttpStatus.CREATED);
        }
        catch (Exception error) {
            return new ResponseEntity<>(new ApiResponse(false, error.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/SaveCreditCard")
    public ResponseEntity<?> saveCreditCard(@RequestBody SaveCreditCardRequest saveCreditCardRequest) {
        try {
            return new ResponseEntity<>(new ApiResponse(true, userService.saveCreditCard(saveCreditCardRequest)), HttpStatus.CREATED);
        }
        catch (Exception error) {
            return new ResponseEntity<>(new ApiResponse(false, error.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/EditCreditCard")
    public ResponseEntity<?> editCreditCard(@RequestBody EditCreditCardRequest editCreditCardRequest) {
        try {
            return new ResponseEntity<>(new ApiResponse(true, userService.editCreditCard(editCreditCardRequest)), HttpStatus.OK);
        }
        catch (Exception error) {
            return new ResponseEntity<>(new ApiResponse(false, error.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/ViewCreditCard")
    public ResponseEntity<?> viewCreditCard(@RequestBody ViewCreditCardRequest viewCreditCardRequest) {
        try {
            return new ResponseEntity<>(new ApiResponse(true, userService.viewCreditCard(viewCreditCardRequest)), HttpStatus.OK);
        }
        catch (Exception error) {
            return new ResponseEntity<>(new ApiResponse(false, error.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/DeleteCreditCard")
    public ResponseEntity<?> deleteCreditCard(@RequestBody DeleteCreditCardRequest deleteCreditCardRequest) {
        try {
            return new ResponseEntity<>(new ApiResponse(true, userService.deleteCreditCard(deleteCreditCardRequest)), HttpStatus.OK);
        }
        catch (Exception error) {
            return new ResponseEntity<>(new ApiResponse(false, error.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
