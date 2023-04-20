package com.itenebris.jpa.demo.respository;

import com.itenebris.jpa.demo.model.Company;
import com.itenebris.jpa.demo.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByRoleType(String roleType);
}
