package com.Spring.Main.Service.Impl;

import com.Spring.Main.Entity.User;
import com.Spring.Main.Enums.Role;
import com.Spring.Main.Repository.UserRepository;
import com.Spring.Main.Service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.Spring.Main.Service.UserService;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepostory;
    @Autowired
    StudentService studentService;

    @Override
    public ResponseEntity<String> addUser(String name, String role) {
        try{
            User user = new User();
            user.setName(name);
            Role role1 = Role.valueOf(role);
            user.setRole(role1);
            userRepostory.save(user);
            return ResponseEntity.ok("User added successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid role provided: " + role);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().body("Error adding user");
    }

    @Override
    public ResponseEntity<String> updateUser(Long id, String name, String role) {
        try{
            User user = userRepostory.findById(id).orElse(null);
            if (user == null) {
                return ResponseEntity.badRequest().body("User not found");
            }
            user.setName(name);
            user.setRole(Role.valueOf(role));
            userRepostory.save(user);
            return ResponseEntity.ok("User updated successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid role provided: " + role);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().body("Error updating user");
    }

    @Override
    public ResponseEntity<String> deleteUser(Long id) {
        try{
            User user = userRepostory.findById(id).orElse(null);
            if(user == null){
                return ResponseEntity.badRequest().body("User not found");
            }
            userRepostory.delete(user);
            return ResponseEntity.ok("User deleted succesfully");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().body("Error deleting user");
    }

    @Override
    public ResponseEntity<User> getUserById(Long id) {
        try{
            User user = userRepostory.findById(id).orElse(null);
            if(user == null){
                return ResponseEntity.badRequest().body(null);
            }

            return ResponseEntity.ok(user);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().body(null);
    }

    @Override
    public ResponseEntity<List<User>> getAllUsers() {
        try{
            List<User> users = userRepostory.findAll();
            return ResponseEntity.ok(users);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().body(null);
    }
}
