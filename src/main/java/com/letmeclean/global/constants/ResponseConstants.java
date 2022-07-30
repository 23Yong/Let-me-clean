package com.letmeclean.global.constants;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseConstants {

    public static final ResponseEntity<Void> OK =
            ResponseEntity.ok().build();

    public static final ResponseEntity<Void> CREATED =
            ResponseEntity.status(HttpStatus.CREATED).build();

    public static final ResponseEntity<String> DUPLICATED_EMAIL =
            new ResponseEntity<>("중복된 이메일입니다.", HttpStatus.CONFLICT);

    public static final ResponseEntity<String> DUPLICATED_NICKNAME =
            new ResponseEntity<>("중복된 닉네임입니다.", HttpStatus.CONFLICT);

    public static final ResponseEntity<String> INVALID_PASSWORD =
            new ResponseEntity<>("유효한 비밀번호 형식이 아닙니다.", HttpStatus.BAD_REQUEST);

    public static final ResponseEntity<String> DUPLICATED_TICKET =
            new ResponseEntity<>("중복된 티켓이름입니다.", HttpStatus.CONFLICT);
}
