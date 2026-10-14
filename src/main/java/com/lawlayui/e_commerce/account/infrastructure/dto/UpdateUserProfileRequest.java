package com.lawlayui.e_commerce.account.infrastructure.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateUserProfileRequest(
    @NotBlank @Size(max = 255, min = 3) String fullName,
    @NotBlank @Size(max = 20, min = 10) String phoneNumber,
    @NotBlank @Size(max = 500, min = 30) String address
) {}
