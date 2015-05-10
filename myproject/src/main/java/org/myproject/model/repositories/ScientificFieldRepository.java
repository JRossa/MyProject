package org.myproject.model.repositories;

import org.myproject.model.entities.ScientificField;
import org.springframework.data.jpa.repository.JpaRepository;



public interface ScientificFieldRepository extends JpaRepository<ScientificField, Long> {

}
