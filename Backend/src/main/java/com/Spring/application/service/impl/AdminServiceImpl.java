package com.Spring.application.service.impl;

import com.Spring.application.entity.Admin;
import com.Spring.application.enums.Role;
import com.Spring.application.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl {
    @Autowired
    private AdminRepository adminRepository;
    public ResponseEntity<String> addAdmin(String adminName)
    {
        try{
            Admin admin = new Admin();
            admin.setName(adminName);
            admin.setRole(Role.valueOf("ADMIN"));
            adminRepository.save(admin);
            return ResponseEntity.ok("Admin added successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().body("Error adding admin");
    }
    public ResponseEntity<String> updateAdmin(Long adminId, String adminName)
    {
        try {
            Admin admin = adminRepository.findById(adminId).orElse(null);
            if (admin == null) {
                return ResponseEntity.badRequest().body("Admin not found");
            }
            admin.setName(adminName);
            adminRepository.save(admin);
            return ResponseEntity.ok("Admin updated successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().body("Error updating admin");
    }
    public ResponseEntity<String> deleteAdmin(Long adminId)
    {
        try{
            adminRepository.deleteById(adminId);
            return ResponseEntity.ok("Admin deleted successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().body("Error deleting admin");
    }
    public ResponseEntity<Admin> getAdminById(Long adminId){
        try {
            Admin admin = adminRepository.findById(adminId).orElse(null);
            if (admin == null) {
                return ResponseEntity.badRequest().body(null);
            }
            return ResponseEntity.ok(admin);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().body(null);
    }

    public ResponseEntity<List<Admin>> getAllAdmins(){
        List<Admin> admins = adminRepository.findAll();
        return ResponseEntity.ok(admins);
    }
}
