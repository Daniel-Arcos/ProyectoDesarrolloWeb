package com.web.aldalu.aldalu.models.dtos.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChangePasswordRequestDTO {
    private String currentPassword;
    private String newPassword;
    private String confirmationPassword;    
}
