package com.Spring.application.dto;

import com.Spring.application.entity.Admin;

import java.util.ArrayList;
import java.util.List;

public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private String role;

    public UserDTO(Long id, String name, String email, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public UserDTO() {
    }

    public static List<UserDTO> convertToDTO(List<Admin> admins) {
        List<UserDTO> userDTOS = new ArrayList<>();
        admins.forEach(admin -> userDTOS.add(new UserDTO(admin.getId(), admin.getName(), admin.getEmail(), admin.getRole().toString())));
        return userDTOS;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
