package com.coinflip.dungeon.Controller;

import com.coinflip.dungeon.Repository.RefreshTokenRepository;
import com.coinflip.dungeon.Repository.RoleRepository;
import com.coinflip.dungeon.Repository.UserRepository;
import com.coinflip.dungeon.Domain.ERole;
import com.coinflip.dungeon.Domain.RefreshToken;
import com.coinflip.dungeon.Domain.Role;
import com.coinflip.dungeon.Domain.User;
import com.coinflip.dungeon.Payload.Request.LoginRequest;
import com.coinflip.dungeon.Payload.Request.SignupRequest;
import com.coinflip.dungeon.Security.JWT.JwtUtils;
import com.coinflip.dungeon.Security.Services.RefreshTokenService;
import com.coinflip.dungeon.Security.Services.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/")
public class AuthController {
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

    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        model.addAttribute("signUpRequest", new SignupRequest());
        return "register";
    }

    @PostMapping("/register")
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

        model.addAttribute("message", "User registered successfully!");
        return "redirect:/login";
    }


    @GetMapping("/login")
    public String getLoginPage(@RequestParam(name = "logout", required = false) String logout, Model model) {
        if (logout != null) {
            model.addAttribute("logoutMessage", "You have been logged out successfully.");
        }
        model.addAttribute("loginRequest", new LoginRequest());
        return "login";
    }


    @PostMapping("/login")
    public String authenticateUser(@ModelAttribute LoginRequest loginRequest, HttpServletResponse response, Model model) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwt = jwtUtils.generateJwtToken(userDetails);

        jwtUtils.addJwtTokenToCookie(response, jwt);
        model.addAttribute("userLogin", loginRequest);

        return "redirect:/user/me";
    }

}
