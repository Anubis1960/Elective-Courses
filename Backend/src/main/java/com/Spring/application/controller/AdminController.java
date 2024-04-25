package com.Spring.application.controller;

import com.Spring.application.entity.Admin;
import com.Spring.application.service.impl.AdminServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminServiceImpl adminService;

    @PostMapping("/")
    public ResponseEntity<String> addAdmin(String adminName) {
        return adminService.addAdmin(adminName);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateAdmin(Long adminId, String adminName) {
        return adminService.updateAdmin(adminId, adminName);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAdmin(Long adminId) {
        return adminService.deleteAdmin(adminId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Admin> getAdminById(Long adminId) {
        return adminService.getAdminById(adminId);
    }

    @GetMapping("/")
    public ResponseEntity<List<Admin>> getAllAdmins() {
        return adminService.getAllAdmins();
    }


}
