package com.coinflip.dungeon.Controller;

import com.coinflip.dungeon.Domain.User;
import com.coinflip.dungeon.Repository.UserRepository;
import com.coinflip.dungeon.Security.JWT.JwtUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;
    @GetMapping("/me")
    public String getUserInfo(HttpServletRequest request, Model model) {
        String token = jwtUtils.getJwtTokenFromRequest(request);
        if (token.isEmpty()) System.out.println("Bad things");
        String username = jwtUtils.getUserNameFromJwtToken(token);
        User user = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("User not found"));
        model.addAttribute("username", username);
        model.addAttribute("user_email", user.getEmail());
        model.addAttribute("user_campaigns", user.getCampaigns());

        return "me";
    }
}
