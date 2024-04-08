package com.Spring.Main.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Admin")
public class Admin{
    @Id
    @Column(name = "admin_id")
    private Integer adminId;

    @Column(name = "admin_name")
    private String adminName;

    @OneToOne
    @JoinColumn(name = "admin_id")
    @MapsId
    private Users user;

    public Admin() {

    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Admin(Integer adminId, String adminName, Users user) {
        this.adminId = adminId;
        this.adminName = adminName;
        this.user = user;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }
}
