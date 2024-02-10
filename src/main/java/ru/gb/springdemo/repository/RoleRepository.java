package ru.gb.springdemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.gb.springdemo.model.security.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
}