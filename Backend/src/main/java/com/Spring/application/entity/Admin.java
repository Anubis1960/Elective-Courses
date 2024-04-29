package com.Spring.application.entity;

import com.Spring.application.enums.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "Admin")
public class Admin extends User{
    public Admin(Long userId, String name, Role role) {
        this.id = userId;
        this.name = name;
        this.role = role;
    }

    public Admin(Long userId, String name) {
        this.id = userId;
        this.name = name;
        this.role = Role.ADMIN;
    }

    public Admin(String name) {
        this.name = name;
        this.role = Role.ADMIN;
    }

    public Admin(String name, Role role) {
        this.name = name;
        this.role = role;
    }

    public Admin() {
    }
}
