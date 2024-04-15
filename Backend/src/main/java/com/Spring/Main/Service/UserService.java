package com.Spring.Main.Service;

import com.Spring.Main.Entity.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
    ResponseEntity<String> addUser(String name, String role);
    ResponseEntity<String> updateUser(Long id, String name, String role);
    ResponseEntity<String> deleteUser(Long id);
    ResponseEntity<User> getUserById(Long id);
    ResponseEntity<List<User>> getAllUsers();
}
