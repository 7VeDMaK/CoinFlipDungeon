package com.coinflip.dungeon.Controller;

import com.coinflip.dungeon.Repository.UserRepository;
import com.coinflip.dungeon.Domain.User;
import com.coinflip.dungeon.Security.JWT.JwtUtils;
import com.coinflip.dungeon.Service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class ApiUserController {

    @Autowired
    UserService userService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping
    public ResponseEntity<List<User>> getAll() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/getSelfUserInfo")
    public ResponseEntity<String> getSelfUserInfo(@RequestHeader("Authorization") String token) {
        if (token.isEmpty()) return ResponseEntity.badRequest().body("User not signed error");
        //Can't be reached
        String username = jwtUtils.getUserNameFromJwtToken(token.split(" ")[1].trim());
        User user = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("User not found"));
        String message = String.format("Username: %s \nE-mail: %s \nCampaigns: %s", username, user.getEmail(), user.getCampaigns());
        return ResponseEntity.ok(message);
    }

    @GetMapping("/getAnotherUserInfo")
    public ResponseEntity<String> getAnotherUserInfo(@RequestHeader("Authorization") String token, @RequestParam String username) {
        if (token.isEmpty()) return ResponseEntity.badRequest().body("User not signed error");
        //Can't be reached
        User user = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("User not found"));
        String message = String.format("Username: %s\nCampaigns: %s", username, user.getCampaigns());
        return ResponseEntity.ok(message);
        //TODO Should create condition about printing only public campaigns, so later
        //TODO Maybe I can unite this 2 endpoints?
    }

}
