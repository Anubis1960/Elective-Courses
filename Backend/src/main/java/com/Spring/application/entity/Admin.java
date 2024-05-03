package com.Spring.application.entity;

import com.Spring.application.enums.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "Admin")
public class Admin extends User{
    public Admin(String name, String email, String password) {
        this.setName(name);
        this.setRole(Role.ADMIN);
        this.setEmail(email);
        this.setPassword(password);
    }

    public Admin() {
    }
}
