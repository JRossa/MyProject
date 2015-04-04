package org.myproject.model.repositories;

import java.util.List;

import org.myproject.model.entities.CategoryLookupTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CategoryLookupTableRepository extends JpaRepository<CategoryLookupTable, Long> {

    
    @Query(value="SELECT ACADEMIC_CATEGORY_NAME FROM tbl_CATEGORY_LOOKUP_TABLE "
            + "WHERE CATEGORY = :category ", nativeQuery = true)
    public String findAcademicNameByCategory (@Param("category") String category);
    
    
}
