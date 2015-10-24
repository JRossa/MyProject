package org.myproject.model.repositories;



import java.util.List;

import org.myproject.model.entities.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface TeacherRepository extends JpaRepository<Teacher, Long> {

	public List <Teacher> findByFullNameContaining(String strName);
	
	
	
	@Query(value="SELECT DISTINCT tbl_TEACHER.* "
	            + "FROM tbl_EXECUTION_YEAR INNER JOIN (tbl_PROFESSORSHIP INNER JOIN tbl_TEACHER "
	            + "ON tbl_PROFESSORSHIP.TEACHER_CODE = tbl_TEACHER.ID) "
	            + "ON tbl_EXECUTION_YEAR.ID = tbl_PROFESSORSHIP.EXECUTION_YEAR "
	            + "WHERE (((tbl_EXECUTION_YEAR.EXECUTION_YEAR)=:executionYear))", nativeQuery = true)
	public List <Teacher> findByExecutionYear(@Param("executionYear") String executionYear);

	
	@Query(value="SELECT tbl_TEACHER.* "
            + "FROM tbl_TEACHER "
            + "ORDER BY FULL_NAME", nativeQuery = true)
	public List <Teacher> findAllOrderByFullName();

	
	@Query(value="SELECT tbl_TEACHER.* "
            + "FROM tbl_TEACHER "
			+ "WHERE tbl_TEACHER.FULL_NAME IS NOT NULL "
            + "ORDER BY tbl_TEACHER.FULL_NAME", nativeQuery = true)
	public List <Teacher> findAllListOrderByFullName();

	@Query(value="SELECT tbl_TEACHER.* "
            + "FROM tbl_TEACHER "
			+ "WHERE (tbl_TEACHER.ID)=:teacherId "
            + "ORDER BY tbl_TEACHER.FULL_NAME", nativeQuery = true)
	public List <Teacher> findAllByTeacherId(@Param("teacherId") Long teacherId);
}
