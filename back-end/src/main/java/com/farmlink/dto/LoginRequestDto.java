package com.farmlink.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class LoginRequestDto {
    @Email(message="Email is not Valid")
    private String email;

    @NotBlank(message="Password is required")
    private String password;
}
