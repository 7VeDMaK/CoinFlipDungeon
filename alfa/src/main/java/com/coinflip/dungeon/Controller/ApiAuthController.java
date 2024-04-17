package com.coinflip.dungeon.Controller;

import com.coinflip.dungeon.Exceptions.TokenRefreshException;
import com.coinflip.dungeon.Repository.RefreshTokenRepository;
import com.coinflip.dungeon.Repository.RoleRepository;
import com.coinflip.dungeon.Repository.UserRepository;
import com.coinflip.dungeon.Domain.ERole;
import com.coinflip.dungeon.Domain.RefreshToken;
import com.coinflip.dungeon.Domain.Role;
import com.coinflip.dungeon.Domain.User;
import com.coinflip.dungeon.Payload.Request.LoginRequest;
import com.coinflip.dungeon.Payload.Request.SignupRequest;
import com.coinflip.dungeon.Payload.Request.TokenRefreshRequest;
import com.coinflip.dungeon.Payload.Response.JwtResponse;
import com.coinflip.dungeon.Payload.Response.MessageResponse;
import com.coinflip.dungeon.Payload.Response.TokenRefreshResponse;
import com.coinflip.dungeon.Security.JWT.JwtUtils;
import com.coinflip.dungeon.Security.Services.RefreshTokenService;
import com.coinflip.dungeon.Security.Services.UserDetailsImpl;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/api/auth")
public class ApiAuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;
    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @GetMapping("/signup")
    public String getRegisterPage(Model model) {
        model.addAttribute("signUpRequest", new SignupRequest());
        System.out.println(model.getAttribute("signUpRequest"));
        return "register";
    }
// TODO юзать model комментарии
// TODO создать me
// TODO как работают cookie
// TODO если всё ок, то просто возвращаем назад security
    @PostMapping("/signup")
    public String signupUser(@ModelAttribute SignupRequest signUpRequest, Model model) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            model.addAttribute("message", "Username is already taken!");
            return "error_page";
        }
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            model.addAttribute("message", "Email is already in use!");
            return "error_page";
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
        System.out.println(user);
        model.addAttribute("message", "User registered successfully!");
        return "redirect:/api/auth/signin";
    }


    @GetMapping("/signin")
    public String getLoginPage(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "login";
    }


    @PostMapping("/signin")
    public String authenticateUser(@ModelAttribute LoginRequest loginRequest, HttpServletResponse response, Model model) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwt = jwtUtils.generateJwtToken(userDetails);

        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        refreshTokenService.deleteByUserId(userDetails.getId());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
        jwtUtils.addJwtTokenToCookie(response, jwt); // Добавляем токен в куки
        model.addAttribute("userLogin", loginRequest);
        return "me";
    }



//    @PostMapping("/refreshtoken")
//    public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
//        String requestRefreshToken = request.getRefreshToken();
//
//        return refreshTokenService.findByToken(requestRefreshToken)
//                .map(refreshTokenService::verifyExpiration)
//                .map(RefreshToken::getUser)
//                .map(user -> {
//                    String token = jwtUtils.generateTokenFromUsername(user.getUsername());
//                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
//                })
//                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
//                        "Refresh token is not in database!"));
//    }
}
