package com.Spring.application.entity;
import com.Spring.application.audit.Auditable;
import com.Spring.application.enums.Role;
import com.Spring.application.view.Views;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;

@MappedSuperclass
public abstract class User extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.Public.class)
    @Column(name = "user_id", nullable = false)
    protected Long id;

    @Column(name = "name", nullable = false)
    @JsonView(Views.Public.class)
    protected String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    @JsonView(Views.Public.class)
    protected Role role;

    @Column(name = "email", nullable = false, unique = true)
    @JsonView(Views.Public.class)
    protected String email;

    @Column(name = "password", nullable = false)
    @JsonView(Views.InternalView.class)
    protected String password;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
