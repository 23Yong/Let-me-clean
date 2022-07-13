package com.letmeclean.controller;

import com.letmeclean.common.constants.ResponseConstants;
import com.letmeclean.controller.dto.cleaner.CleanerRequest;
import com.letmeclean.service.AuthService;
import com.letmeclean.service.CleanerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
public class CleanerController {

    private final CleanerService cleanerService;
    private final AuthService authService;

    @PostMapping("/cleaners")
    public ResponseEntity<Void> signUp(@RequestBody CleanerRequest.SignUpRequestDto signUpRequestDto) {
        cleanerService.signUp(signUpRequestDto);
        return ResponseConstants.CREATED;
    }

    @GetMapping("/cleaner-emails/{email}/exists")
    public ResponseEntity<Boolean> checkEmailDuplicated(@PathVariable String email) {
        return ResponseEntity.ok(cleanerService.checkEmailDuplicated(email));
    }
}
