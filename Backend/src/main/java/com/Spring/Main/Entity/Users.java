package com.Spring.Main.Entity;

import jakarta.persistence.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    protected Integer userId;

    @Column(name = "is_admin")
    protected Boolean isAdmin;

    @Column(name = "is_student")
    protected Boolean isStudent;

    public Users() {
    }

    public Users(Integer userId, Boolean isAdmin, Boolean isStudent) {
        this.userId = userId;
        this.isAdmin = isAdmin;
        this.isStudent = isStudent;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public Boolean getStudent() {
        return isStudent;
    }

    public void setStudent(Boolean student) {
        isStudent = student;
    }
}
