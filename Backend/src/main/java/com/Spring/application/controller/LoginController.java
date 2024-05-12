package com.Spring.application.controller;

import com.Spring.application.audit.AuditorAwareImpl;
import com.Spring.application.dto.UserDTO;
import com.Spring.application.entity.User;
import com.Spring.application.service.impl.AdminServiceImpl;
import com.Spring.application.service.impl.StudentServiceImpl;
import com.Spring.application.utils.Encrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.NoSuchAlgorithmException;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private StudentServiceImpl studentService;
    @Autowired
    private AdminServiceImpl adminService;

    @GetMapping("/")
    public ResponseEntity<UserDTO> checkCredentials(@RequestParam  String email,@RequestParam String password) throws NoSuchAlgorithmException {
        User admin = adminService.getAdminByEmailAndPassword(email, Encrypt.toHexString(Encrypt.encrypt(password)));
        if (admin != null) {
            AuditorAwareImpl.setAuditorName(admin.getName());
            UserDTO userDTO = new UserDTO(admin.getId(), admin.getName(), admin.getEmail(), admin.getRole().toString());
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        }
        User student = studentService.getStudentByEmailAndPassword(email, Encrypt.toHexString(Encrypt.encrypt(password)));
        if (student != null) {
            AuditorAwareImpl.setAuditorName(student.getName());
            UserDTO userDTO = new UserDTO(student.getId(), student.getName(), student.getEmail(), student.getRole().toString());
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
}
