package com.farmlink.dto;



import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegistrationDto {

    @NotBlank
    private String firstName;
    
    @NotBlank
    private String lastName;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 10, max = 15)
    private String phoneNumber;

    @NotBlank
    @Size(min = 6, max = 60)
    private String password;

    // 👇 role from React (ONLY FARMER / OWNER allowed)
    @NotNull(message = "Role is required")
    private String role;
    
    //Address dto reference
    @NotNull(message = "Address is required")
    private AddressRequestDto addressDto;
}
