package com.fag.musicalmodel.dto;

public record CanalDto(
        Long idSimulador,
        int agudo,
        int medio,
        int grave,
        int volume
) {
}
