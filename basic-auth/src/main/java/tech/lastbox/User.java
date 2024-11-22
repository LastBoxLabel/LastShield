package tech.lastbox;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import tech.lastbox.annotations.Password;
import tech.lastbox.annotations.Username;


@Entity
public class User {
    @Id
    @GeneratedValue
    private long id;

    @Username
    private String username;

    @Password
    private String password;
}
