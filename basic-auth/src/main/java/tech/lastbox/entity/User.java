package tech.lastbox.entity;

import jakarta.persistence.*;
import tech.lastbox.annotations.Password;
import tech.lastbox.annotations.Username;

import java.util.Objects;


@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @Username
    @Column(unique = true)
    private String username;

    @Password
    private String password;

    private String role;

    public User() {}

    public User(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.role = "USER";
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof User user)) return false;
        return Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
