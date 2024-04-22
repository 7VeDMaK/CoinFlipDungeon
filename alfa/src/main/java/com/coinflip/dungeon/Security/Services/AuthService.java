package com.coinflip.dungeon.Security.Services;

import com.coinflip.dungeon.Domain.ERole;
import com.coinflip.dungeon.Domain.Role;
import com.coinflip.dungeon.Domain.User;
import com.coinflip.dungeon.Payload.Request.LoginRequest;
import com.coinflip.dungeon.Payload.Request.SignupRequest;
import com.coinflip.dungeon.Payload.Response.MessageResponse;
import com.coinflip.dungeon.Repository.RoleRepository;
import com.coinflip.dungeon.Repository.UserRepository;
import com.coinflip.dungeon.Security.JWT.JwtUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AuthService {

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    RefreshTokenService refreshTokenService;
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    public ResponseEntity<?> registerUser(SignupRequest signUpRequest) {

        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin" -> {
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                    }
                    case "mod" -> {
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);
                    }
                    case "sup" -> {
                        Role modRole = roleRepository.findByName(ERole.ROLE_SUPPORT)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);
                    }
                    default -> {
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                    }
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }


    public ResponseEntity<?> authenticateUser(LoginRequest loginRequest, HttpServletResponse response) {

        if (loginRequest == null)
            return ResponseEntity.badRequest().body(new MessageResponse("Error: login request is empty!"));
        else if (loginRequest.getUsername() == null)
            return ResponseEntity.badRequest().body(new MessageResponse("Error: username is empty!"));
        else if (loginRequest.getPassword() == null)
            return ResponseEntity.badRequest().body(new MessageResponse("Error: password is empty!"));

        Authentication authentication;

        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            String jwt = jwtUtils.generateJwtToken(userDetails);

            List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

//        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
            //TODO доделать refresh token логику

            jwtUtils.addJwtTokenToCookie(response, jwt);

            return ResponseEntity.ok(new MessageResponse("All ok!"));
        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Password not correct. Try again")); // Отображение страницы с сообщением об ошибке
        }
    }
}
