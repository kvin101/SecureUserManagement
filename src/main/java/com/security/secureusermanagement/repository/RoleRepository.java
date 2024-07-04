package com.security.secureusermanagement.repository;

import com.security.secureusermanagement.dao.role.Role;
import com.security.secureusermanagement.dao.role.RoleName;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

  Optional<Role> findByName(RoleName name);
}