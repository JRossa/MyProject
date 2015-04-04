package org.myproject.model.repositories;

 
import org.myproject.model.entities.CategoryGroup;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CategoryGroupRepository extends JpaRepository<CategoryGroup, Long> {


}