package com.coinflip.dungeon.Controller;

import com.coinflip.dungeon.Payload.Response.MessageResponse;
import com.coinflip.dungeon.Repository.RefreshTokenRepository;
import com.coinflip.dungeon.Repository.RoleRepository;
import com.coinflip.dungeon.Repository.UserRepository;
import com.coinflip.dungeon.Payload.Request.LoginRequest;
import com.coinflip.dungeon.Payload.Request.SignupRequest;
import com.coinflip.dungeon.Security.JWT.JwtUtils;
import com.coinflip.dungeon.Security.Services.AuthService;
import com.coinflip.dungeon.Security.Services.RefreshTokenService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    AuthService authService;

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
    public String registerUser(@ModelAttribute SignupRequest signUpRequest, Model model) {
        ResponseEntity<?> responseEntity = authService.registerUser(signUpRequest);

        HttpStatus statusCode = (HttpStatus) responseEntity.getStatusCode();
        String responseBody = ((MessageResponse) Objects.requireNonNull(responseEntity.getBody())).getMessage(); // Получить тело ответа

        if (statusCode == HttpStatus.OK) {
            model.addAttribute("message", "User registered successfully!");
            return "redirect:/login";
        } else {
            model.addAttribute("errorMessage", "Error occurred during registration: " + responseBody +" "+ statusCode);
            return "error_page";
        }
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

        ResponseEntity<?> responseEntity = authService.authenticateUser(loginRequest,response);

        HttpStatus statusCode = (HttpStatus) responseEntity.getStatusCode();
        String responseBody = ((MessageResponse) Objects.requireNonNull(responseEntity.getBody())).getMessage(); // Получить тело ответа

        if (statusCode == HttpStatus.OK) {
            model.addAttribute("message", "User authenticated successfully!");
            return "redirect:/user/me";
        } else {
            model.addAttribute("errorMessage", "Error occurred during authentication: " + responseBody +" "+ statusCode);
            return "error_page";
        }

    }

}
