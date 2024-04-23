package com.Spring.Main.service;

import com.Spring.Main.entity.Admin;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AdminService {
    ResponseEntity<String> addAdmin(Long adminId, String adminName, String role);
    ResponseEntity<String> updateAdmin(Long adminId, String adminName, String role);
    ResponseEntity<String> deleteAdmin(Long adminId);
    ResponseEntity<Admin> getAdminById(Long adminId);
    ResponseEntity<List<Admin>> getAllAdmins();
}
