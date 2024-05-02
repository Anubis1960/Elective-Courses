package com.Spring.application.service;

import com.Spring.application.entity.Admin;
import com.Spring.application.exceptions.ObjectNotFound;

import java.util.List;

public interface AdminService {
    Admin addAdmin(String adminName, String email, String password);
    Admin updateAdmin(Long adminId, String adminName) throws ObjectNotFound;
    Admin deleteAdmin(Long adminId) throws ObjectNotFound;
    Admin getAdminById(Long adminId) throws ObjectNotFound;
    List<Admin> getAllAdmins();
    Admin getAdminByEmailAndPassword(String email, String password);
}
