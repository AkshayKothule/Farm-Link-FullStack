package com.farmlink.services;

import com.farmlink.dto.UserRegistrationDto;

public interface AuthService {

    void register(UserRegistrationDto dto);
}
