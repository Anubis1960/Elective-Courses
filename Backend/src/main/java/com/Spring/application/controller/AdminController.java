package com.Spring.application.controller;

import com.Spring.application.dto.UserDTO;
import com.Spring.application.entity.Admin;
import com.Spring.application.exceptions.ObjectNotFound;
import com.Spring.application.service.impl.AdminServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminServiceImpl adminService;
    @PostMapping("/")
    public ResponseEntity<UserDTO> addAdmin(
            @RequestParam String adminName,
            @RequestParam String email,
            @RequestParam String password) throws NoSuchAlgorithmException{
        Admin admin = adminService.addAdmin(adminName, email,password);
        if (admin == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        UserDTO userDTO = new UserDTO(admin.getId(), admin.getName(), admin.getEmail(), admin.getRole().toString());
        return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateAdmin(
            @PathVariable(value = "id") Long adminId,
            @RequestParam String adminName,
            @RequestParam String email,
            @RequestParam String password) throws ObjectNotFound, NoSuchAlgorithmException {
        Admin admin = adminService.updateAdmin(adminId, adminName, email, password);
        if (admin == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        UserDTO userDTO = new UserDTO(admin.getId(), admin.getName(), admin.getEmail(), admin.getRole().toString());
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserDTO> deleteAdmin(@PathVariable(value = "id") Long adminId) throws ObjectNotFound {
        Admin admin = adminService.deleteAdmin(adminId);
        if (admin == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        UserDTO userDTO = new UserDTO(admin.getId(), admin.getName(), admin.getEmail(), admin.getRole().toString());
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getAdminById(@PathVariable(value = "id") Long adminId) throws ObjectNotFound {
        Admin admin = adminService.getAdminById(adminId);
        if (admin == null) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
        UserDTO userDTO = new UserDTO(admin.getId(), admin.getName(), admin.getEmail(), admin.getRole().toString());
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<UserDTO>> getAllAdmins() {
        List<Admin> admins = adminService.getAllAdmins();
        if (admins.isEmpty()) {
            return new ResponseEntity<>(null,HttpStatus.NO_CONTENT);
        }
        List<UserDTO> userDTOs = UserDTO.convertToDTO(admins);
        return new ResponseEntity<>(userDTOs, HttpStatus.OK);
    }


}
