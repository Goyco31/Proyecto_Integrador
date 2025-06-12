package com.integrador.spring.app.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Validate2FARequest {
    private String twoFactorCode;
    private String tempToken;
}