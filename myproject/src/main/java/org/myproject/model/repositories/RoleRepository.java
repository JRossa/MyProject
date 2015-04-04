package org.myproject.model.repositories;

import org.myproject.model.entities.LogRole;
import org.springframework.data.jpa.repository.JpaRepository;



public interface RoleRepository extends JpaRepository<LogRole, Long> {

}
