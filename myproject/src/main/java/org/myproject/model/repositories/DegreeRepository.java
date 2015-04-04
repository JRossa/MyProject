package org.myproject.model.repositories;

 

import java.util.List;

import org.myproject.model.entities.Degree;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;



public interface DegreeRepository extends JpaRepository<Degree, Long> {

    @Query(value="SELECT tbl_DEGREE.* "
               + "FROM tbl_DEGREE "
               + "WHERE MASTER = true "
               + "ORDER BY CODE", nativeQuery = true)
 public List<Degree> findAllMasterDegrees();

}