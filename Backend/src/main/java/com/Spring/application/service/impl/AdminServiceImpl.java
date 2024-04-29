package com.Spring.application.service.impl;

import com.Spring.application.entity.Admin;
import com.Spring.application.enums.Role;
import com.Spring.application.exceptions.ObjectNotFound;
import com.Spring.application.repository.AdminRepository;
import com.Spring.application.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminRepository adminRepository;
    public Admin addAdmin(String adminName)
    {
        Admin admin = new Admin();
        admin.setName(adminName);
        admin.setRole(Role.valueOf("ADMIN"));
        adminRepository.save(admin);
        return admin;
    }
    public Admin updateAdmin(Long adminId, String adminName) throws ObjectNotFound {
        Admin admin = adminRepository.findById(adminId).orElse(null);
        if (admin == null) {
            throw new ObjectNotFound("Admin not found");
        }
        admin.setName(adminName);
        adminRepository.save(admin);
        return admin;
    }

    public Admin deleteAdmin(Long adminId) throws ObjectNotFound {
        Admin admin = adminRepository.findById(adminId).orElse(null);
        if (admin == null) {
            throw new ObjectNotFound("Admin not found");
        }
        adminRepository.delete(admin);
        return admin;
    }
    public Admin getAdminById(Long adminId) throws ObjectNotFound {
        Admin admin = adminRepository.findById(adminId).orElse(null);
        if (admin == null) {
            throw new ObjectNotFound("Admin not found");
        }
        return admin;
    }

    public List<Admin> getAllAdmins(){
        return adminRepository.findAll();
    }
}