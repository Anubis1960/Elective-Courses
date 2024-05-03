package com.Spring.application.controller;

import com.Spring.application.entity.User;
import com.Spring.application.service.impl.AdminServiceImpl;
import com.Spring.application.service.impl.StudentServiceImpl;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.Spring.application.view.Views;

@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private StudentServiceImpl studentService;
    @Autowired
    private AdminServiceImpl adminService;

    @GetMapping("/")
    @JsonView(Views.Public.class)
    public ResponseEntity<User> checkCredentials(String email, String password) {
        User admin = adminService.getAdminByEmailAndPassword(email, password);
        if (admin != null) {
            return new ResponseEntity<>(admin, HttpStatus.OK);
        }
        User student = studentService.getStudentByEmailAndPassword(email, password);
        if (student != null) {
            return new ResponseEntity<>(student, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
}
