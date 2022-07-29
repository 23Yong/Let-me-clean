package com.letmeclean.global.exception;

import com.letmeclean.global.exception.member.DuplicatedEmailException;
import com.letmeclean.global.exception.member.DuplicatedNicknameException;
import com.letmeclean.global.exception.member.InvalidPasswordException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static com.letmeclean.global.constants.ResponseConstants.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicatedEmailException.class)
    public final ResponseEntity<String> handleDuplicatedEmailException(
            DuplicatedEmailException ex) {
        return DUPLICATED_EMAIL;
    }

    @ExceptionHandler(DuplicatedNicknameException.class)
    public final ResponseEntity<String> handleDuplicatedNicknameException(
            DuplicatedNicknameException ex) {
        return DUPLICATED_NICKNAME;
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public final ResponseEntity<String> handleInvalidPasswordException(
            InvalidPasswordException ex) {
        return INVALID_PASSWORD;
    }
}
