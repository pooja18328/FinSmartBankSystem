package com.finsmartbank.FinSmartBankSystem.controller;

import com.finsmartbank.FinSmartBankSystem.model.User;
import com.finsmartbank.FinSmartBankSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:8080") // allow local frontend or same server
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // 🟩 Register new user
    @PostMapping("/register")
    public String registerUser(@RequestBody User user) {
        try {
            // Check if user already exists by email
            User existingUser = userRepository.findByEmail(user.getEmail());
            if (existingUser != null) {
                return "User already exists!";
            }

            // Save new user
            userRepository.save(user);
            return "Registration successful!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    // 🟦 Login user
    @PostMapping("/login")
    public String loginUser(@RequestBody User user) {
        User existingUser = userRepository.findByEmailAndPassword(
                user.getEmail(), user.getPassword());

        if (existingUser != null) {
            // ✅ Added role info for JS redirect without removing any lines
            return "Login Successful! Welcome, " + existingUser.getFullName() 
                   + " | Role: " + existingUser.getRole();
        } else {
            return "Invalid Email or Password!";
        }
    }
}
