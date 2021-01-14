package io.trelo.ppmtool.services;

import io.trelo.ppmtool.domain.User;
import io.trelo.ppmtool.exceptions.UsernameAlreadyExistsException;
import io.trelo.ppmtool.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User saveUser (User newUser){

        try{
            newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
            newUser.setUsername(newUser.getUsername());
            newUser.setConfirmpassword("");

            return userRepository.save(newUser);
        }
        catch (Exception e){
            throw new UsernameAlreadyExistsException("Username " + newUser.getUsername() + " Already Exists.");
        }

    }
}
