package com.fag.musicalmodel.config;

import com.fag.musicalmodel.enums.UserRole;
import lombok.Builder;

@Builder
public record JWTUserData(Long userId, String email, String role) {

    public boolean isAdmin() {
        return UserRole.ADMIN.getAuthority().equals(role);
    }
}
