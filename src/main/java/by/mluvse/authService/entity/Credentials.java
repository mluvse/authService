package by.mluvse.authService.entity;

import by.mluvse.authService.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "credentials")
@Getter
@Setter
public class Credentials {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String login;
    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "user_id")
    private Long userId;

    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean active=true;
}
