package com.Spring.application.service.impl;

import com.Spring.application.entity.Admin;
import com.Spring.application.exceptions.ObjectNotFound;
import com.Spring.application.repository.AdminRepository;
import com.Spring.application.service.AdminService;
import com.Spring.application.utils.Encrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminRepository adminRepository;

    @Override
    public Admin addAdmin(String adminName, String email, String password) throws NoSuchAlgorithmException {
        Admin admin = new Admin(adminName, email, Encrypt.toHexString(Encrypt.encrypt(password)));
        adminRepository.save(admin);
        return admin;
    }

    @Override
    public Admin updateAdmin(Long adminId, String adminName, String email,  String password) throws ObjectNotFound, NoSuchAlgorithmException {
        Admin admin = adminRepository.findById(adminId).orElse(null);
        if (admin == null) {
            return null;
        }
        admin.setName(adminName);
        admin.setEmail(email);
        admin.setPassword(Encrypt.toHexString(Encrypt.encrypt(password)));
        adminRepository.save(admin);
        return admin;
    }

    @Override
    public Admin deleteAdmin(Long adminId) throws ObjectNotFound {
        Admin admin = adminRepository.findById(adminId).orElse(null);
        if (admin == null) {
            return null;
        }
        adminRepository.delete(admin);
        return admin;
    }

    @Override
    public Admin getAdminById(Long adminId) throws ObjectNotFound {
        Admin admin = adminRepository.findById(adminId).orElse(null);
        if (admin == null) {
            return null;
        }
        return admin;
    }

    @Override
    public List<Admin> getAllAdmins(){
        return adminRepository.findAll();
    }

    @Override
    public Admin getAdminByEmailAndPassword(String email, String password) {
        return adminRepository.findByEmailAndPassword(email, password);
    }
}
