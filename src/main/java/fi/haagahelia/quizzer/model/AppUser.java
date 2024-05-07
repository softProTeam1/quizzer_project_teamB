package fi.haagahelia.quizzer.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "UserTable")
@NoArgsConstructor
@Getter
@Setter
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;
    // Username with unique constraint
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String passwordHash;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "role", nullable = false)
    private String role;

    public AppUser(String username, String passwordHash, String email, String role) {
        super();
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
        this.role = role;
    }


}
