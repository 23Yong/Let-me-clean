package com.letmeclean.exception;

import com.letmeclean.exception.member.DuplicatedEmailException;
import com.letmeclean.exception.member.DuplicatedNicknameException;
import com.letmeclean.exception.member.InvalidPasswordException;
import com.letmeclean.exception.member.NotMatchPasswordException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static com.letmeclean.common.constants.ResponseConstants.*;

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

    @ExceptionHandler(NotMatchPasswordException.class)
    public final ResponseEntity<String> handleNotMatchPasswordException(
            NotMatchPasswordException ex) {
        return NOT_MATCH_PASSWORD;
    }
}
