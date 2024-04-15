package com.Spring.Main.Entity;
import com.Spring.Main.Audit.Auditable;
import com.Spring.Main.Enums.Role;
import jakarta.persistence.*;

@Entity
@Table(name = "Users")
public class User extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id", nullable = false)
    protected Long id;

    @Column(name = "name", nullable = false)
    protected String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    protected Role role;

    public User() {
    }

    public User(Long userId, String name, Role role) {
        this.id = userId;
        this.role = role;
        this.name = name;
    }

    public User(String name, Role role){
        this.role = role;
        this.name = name;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Long getUserId() {
        return id;
    }

    public void setUserId(Long userId) {
        this.id = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
