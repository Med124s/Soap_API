package com.app.articlePage.Model;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Size(max = 20)
    private String name;
    @NotBlank
    @Size(max = 20, min = 8)
    private String email;
    private String dateCreation;
    private String dateModify;
    @NotBlank
    @Size(max = 120)
    private String password;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<URoles> roles = new HashSet<>();

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return name;
    }

    public void setUsername(String username) {
        this.name = username;
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

    public Set<URoles> getRoles() {
        return roles;
    }

    public void setRoles(Set<URoles> roles) {
        this.roles = roles;
    }

    public String getCreatedAt() {
        return dateCreation;
    }

    public void setCreatedAt(String createdAt) {
        this.dateCreation = createdAt;
    }

    public String getUpdatedAt() {
        return dateModify;
    }

    public void setUpdatedAt(String updatedAt) {
        this.dateModify = updatedAt;
    }

    public User(String username, String email, String password, String createdAt, String updatedAt) {
        this.name = username;
        this.email = email;
        this.password = password;
        this.dateModify = updatedAt;
        this.dateCreation = createdAt;
    }
}
