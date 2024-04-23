package com.Spring.Main.controller;

import com.Spring.Main.entity.Admin;
import com.Spring.Main.service.AdminService;
import com.Spring.Main.service.impl.AdminServiceImpl;
import com.Spring.Main.service.impl.CourseScheduleServiceImpl;
import com.Spring.Main.service.impl.CourseServiceImpl;
import com.Spring.Main.service.impl.StudentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AdminController {
    @Autowired
    private AdminServiceImpl adminService;

    @PostMapping("/addAdmin")
    public ResponseEntity<String> addAdmin(String adminName) {
        return adminService.addAdmin(adminName);
    }

    @PostMapping("/updateAdmin")
    public ResponseEntity<String> updateAdmin(Long adminId, String adminName) {
        return adminService.updateAdmin(adminId, adminName);
    }

    @PostMapping("/deleteAdmin")
    public ResponseEntity<String> deleteAdmin(Long adminId) {
        return adminService.deleteAdmin(adminId);
    }

    @PostMapping("/getAdminById")
    public ResponseEntity<Admin> getAdminById(Long adminId) {
        return adminService.getAdminById(adminId);
    }

    @PostMapping("/getAllAdmins")
    public ResponseEntity<List<Admin>> getAllAdmins() {
        return adminService.getAllAdmins();
    }


}
