package com.Spring.application.controller;

import com.Spring.application.entity.Admin;
import com.Spring.application.exceptions.ObjectNotFound;
import com.Spring.application.service.impl.AdminServiceImpl;
import com.Spring.application.view.Views;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminServiceImpl adminService;
    @PostMapping("/")
    @JsonView(Views.Public.class)
    public ResponseEntity<Admin> addAdmin(@RequestParam String adminName, @RequestParam String email, @RequestParam String password) {
        Admin admin = adminService.addAdmin(adminName, email, password);
        return new ResponseEntity<>(admin, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @JsonView(Views.Public.class)
    public ResponseEntity<Admin> updateAdmin(@PathVariable(value = "id") Long adminId,@RequestParam String adminName) throws ObjectNotFound {
        Admin admin = adminService.updateAdmin(adminId, adminName);
        return new ResponseEntity<>(admin, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @JsonView(Views.Public.class)
    public ResponseEntity<Admin> deleteAdmin(@PathVariable(value = "id") Long adminId) throws ObjectNotFound {
        Admin admin = adminService.deleteAdmin(adminId);
        return new ResponseEntity<>(admin, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @JsonView(Views.Public.class)
    public ResponseEntity<Admin> getAdminById(@PathVariable(value = "id") Long adminId) throws ObjectNotFound {
        Admin admin = adminService.getAdminById(adminId);
        return new ResponseEntity<>(admin, HttpStatus.OK);
    }

    @GetMapping("/")
    @JsonView(Views.Public.class)
    public ResponseEntity<List<Admin>> getAllAdmins() {
        List<Admin> admins = adminService.getAllAdmins();
        if (admins.isEmpty()) {
            return new ResponseEntity<>(null,HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(admins, HttpStatus.OK);
    }


}
