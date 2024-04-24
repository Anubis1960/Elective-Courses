package com.Spring.Main.controller;

import com.Spring.Main.entity.Admin;
import com.Spring.Main.service.impl.AdminServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminServiceImpl adminService;

    @PostMapping("/addadmin")
    public ResponseEntity<String> addAdmin(String adminName) {
        return adminService.addAdmin(adminName);
    }

    @PostMapping("/updateadmin")
    public ResponseEntity<String> updateAdmin(Long adminId, String adminName) {
        return adminService.updateAdmin(adminId, adminName);
    }

    @PostMapping("/deleteadmin")
    public ResponseEntity<String> deleteAdmin(Long adminId) {
        return adminService.deleteAdmin(adminId);
    }

    @PostMapping("/getadminbyid")
    public ResponseEntity<Admin> getAdminById(Long adminId) {
        return adminService.getAdminById(adminId);
    }

    @PostMapping("/getalladmins")
    public ResponseEntity<List<Admin>> getAllAdmins() {
        return adminService.getAllAdmins();
    }


}
