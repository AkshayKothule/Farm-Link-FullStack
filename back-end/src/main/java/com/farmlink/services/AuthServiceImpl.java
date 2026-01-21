package com.farmlink.services;

import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.farmlink.dto.LoginRequestDto;
import com.farmlink.dto.LoginResponseDto;
import com.farmlink.dto.UserRegistrationDto;
import com.farmlink.entities.Address;
import com.farmlink.entities.User;
import com.farmlink.entities.UserRole;
import com.farmlink.repository.AddressRepository;
import com.farmlink.repository.UserRepository;
import com.farmlink.security.JwtUtils;
import com.farmlink.security.UserPrincipal;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    // 🔐 REQUIRED FOR LOGIN
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    // ================= REGISTER =================
    @Override
    public void register(UserRegistrationDto userDto) {

        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }

        UserRole role;
        if ("FARMER".equalsIgnoreCase(userDto.getRole())) {
            role = UserRole.FARMER;
        } else if ("OWNER".equalsIgnoreCase(userDto.getRole())) {
            role = UserRole.OWNER;
        } else {
            throw new IllegalArgumentException("Invalid role");
        }

        User user = modelMapper.map(userDto, User.class);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRole(role);

        User savedUser = userRepository.save(user);

        if (userDto.getAddressDto() != null) {
            Address address = modelMapper.map(userDto.getAddressDto(), Address.class);
            address.setUser(savedUser);
            addressRepository.save(address);
        }
    }

    // ================= LOGIN =================
    @Override
    public LoginResponseDto login(LoginRequestDto loginDto) {

        // 1️⃣ Authenticate using Spring Security
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getEmail(),
                        loginDto.getPassword()
                )
        );

        // 2️⃣ Get authenticated user
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        // 3️⃣ Generate JWT token
        String token = jwtUtils.generateToken(principal);

        // 4️⃣ Send response
        return new LoginResponseDto(
                token,
                principal.getUserRole()
        );
    }
}
