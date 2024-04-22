package com.coinflip.dungeon.Service;

import com.coinflip.dungeon.Domain.ERole;
import com.coinflip.dungeon.Domain.Role;
import com.coinflip.dungeon.Payload.Request.SignupRequest;
import com.coinflip.dungeon.Payload.Response.MessageResponse;
import com.coinflip.dungeon.Repository.RoleRepository;
import com.coinflip.dungeon.Repository.UserRepository;
import com.coinflip.dungeon.Domain.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public List<User> getAllUsers(){
        List<User> users = userRepository.findAll();

        return users;
    }
}
