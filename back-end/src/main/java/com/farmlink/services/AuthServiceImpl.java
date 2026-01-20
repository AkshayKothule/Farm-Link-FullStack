package com.farmlink.services;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.farmlink.dto.UserRegistrationDto;
import com.farmlink.entities.Address;
import com.farmlink.entities.User;
import com.farmlink.entities.UserRole;
import com.farmlink.repository.AddressRepository;
import com.farmlink.repository.UserRepository;
import com.farmlink.services.AuthService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Override
    public void register(UserRegistrationDto userDto) {

        // 1️⃣ Email uniqueness
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }

        // 2️⃣ Role validation
        UserRole role;
        if ("FARMER".equalsIgnoreCase(userDto.getRole())) {
            role = UserRole.FARMER;
        } else if ("OWNER".equalsIgnoreCase(userDto.getRole())) {
            role = UserRole.OWNER;
        } else {
            throw new IllegalArgumentException("Invalid role");
        }

        // 3️⃣ Map User
        User user = modelMapper.map(userDto, User.class);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRole(role);

        User savedUser = userRepository.save(user);

        // 4️⃣ Map Address
        if (userDto.getAddressDto() != null) {
            Address address = modelMapper.map(userDto.getAddressDto(), Address.class);
            address.setUser(savedUser);
            addressRepository.save(address);
        }
    }
}
