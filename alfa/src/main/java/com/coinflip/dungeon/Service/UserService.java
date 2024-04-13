package com.coinflip.dungeon.Service;

import com.coinflip.dungeon.Repository.UserRepository;
import com.coinflip.dungeon.Domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public List<User> getAllUsers(){
        List<User> users = userRepository.findAll();

        return users;
    }
}
