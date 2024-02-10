package ru.gb.springdemo.model.security;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;

@NoArgsConstructor
@Entity
@Component
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    private String role;

    @ManyToMany(mappedBy = "roleList",fetch = FetchType.EAGER)
    private List<Users> users;

    public String getRoleName() {
        return role;
    }

    public Role(String role) {
        this.role = role;
    }

    public long getId() {
        return roleId;
    }

    @Override
    public String getAuthority() {
        return this.role;
    }

    public List<Users> getUsers() {
        return List.copyOf(users);
    }

    public void setUsers(List<Users> users) {
        this.users = users;
    }

    public boolean deprive(Users user) {
        return users.remove(user);
    }

    @Override
    public String toString() {
        return "Role{" +
                "roleId=" + roleId +
                ", role='" + role + '\'' +
                '}';
    }
}