//package com.gayou.auth.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//
//import com.gayou.auth.model.User;
//import com.gayou.auth.repository.UserRepository;
//import com.gayou.auth.exception.UserNotFoundException;
//
//import jakarta.validation.Valid;
//import java.util.List;
//
//@RestController
//@RequestMapping("/auth")
//@Validated
//public class AuthController {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @PostMapping("/user")
//    public ResponseEntity<User> createUser(@Valid @RequestBody User newUser) {
//        User user = userRepository.save(newUser);
//        return new ResponseEntity<>(user, HttpStatus.CREATED);
//    }
//
//    @GetMapping("/users")
//    public ResponseEntity<List<User>> getAllUsers() {
//        List<User> users = userRepository.findAll();
//        return new ResponseEntity<>(users, HttpStatus.OK);
//    }
//
//    @GetMapping("/user/{id}")
//    public ResponseEntity<User> getUserById(@PathVariable Long id) {
//        User user = userRepository.findById(id)
//                .orElseThrow(() -> new UserNotFoundException(id));
//        return new ResponseEntity<>(user, HttpStatus.OK);
//    }
//
//    @PutMapping("/user/{id}")
//    public ResponseEntity<User> updateUser(@Valid @RequestBody User newUser, @PathVariable Long id) {
//        User updatedUser = userRepository.findById(id)
//                .map(user -> {
//                    user.setUsername(newUser.getUsername());
//                    user.setPassword(newUser.getPassword());
//                    return userRepository.save(user);
//                }).orElseThrow(() -> new UserNotFoundException(id));
//        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
//    }
//
//    @DeleteMapping("/user/{id}")
//    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
//        if (!userRepository.existsById(id)) {
//            throw new UserNotFoundException(id);
//        }
//        userRepository.deleteById(id);
//        return new ResponseEntity<>("User with id " + id + " has been deleted successfully.", HttpStatus.OK);
//    }
//}
