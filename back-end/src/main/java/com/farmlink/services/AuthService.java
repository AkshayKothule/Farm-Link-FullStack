package com.farmlink.services;

import com.farmlink.dto.LoginRequestDto;
import com.farmlink.dto.LoginResponseDto;
import com.farmlink.dto.UserRegistrationDto;

public interface AuthService {
	//Registration Method
	public void register(UserRegistrationDto userDto);
    
    //Login Authentication Method
    public LoginResponseDto login(LoginRequestDto loginDto);
    
    
    public String encodePassword(String rawPassword) ;
}
