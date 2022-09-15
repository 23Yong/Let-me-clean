package com.letmeclean.dto.member.request;

public record PasswordModifyRequest(
        String email,
        String prevPassword,
        String newPassword
) {
}
