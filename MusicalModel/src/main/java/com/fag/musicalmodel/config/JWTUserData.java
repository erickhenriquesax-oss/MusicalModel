package com.fag.musicalmodel.config;

import lombok.Builder;

@Builder
public record JWTUserData(Long userId, String email, String role) {
}
