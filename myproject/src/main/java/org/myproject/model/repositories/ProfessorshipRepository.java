package org.myproject.model.repositories;

import java.util.List;

import org.myproject.model.entities.Course;
import org.myproject.model.entities.Professorship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ProfessorshipRepository extends JpaRepository<Professorship, Long> {

	public List <Professorship> findByCourseCode(Integer courseCode);
	
	public List <Professorship> findByTeacherId(Long teacherId);
	
	@Query(value="SELECT ID, CODE, NAME FROM tbl_COURSE "
	           + "WHERE ID IN (SELECT DISTINCT tbl_PROFESSORSHIP.COURSE_CODE "
	           + "FROM tbl_EXECUTION_YEAR INNER JOIN tbl_PROFESSORSHIP "
	           + "ON tbl_EXECUTION_YEAR.ID = tbl_PROFESSORSHIP.EXECUTION_YEAR "
	           + "WHERE tbl_PROFESSORSHIP.TEACHER_CODE = :teacherId AND "
	           + "tbl_EXECUTION_YEAR.EXECUTION_YEAR = :executionYear) ", nativeQuery = true)
	public List <Object[]> findCoursesByUserAndExecutionYear(@Param("teacherId") Long teacherId,
	                                                         @Param("executionYear") String executionYear);

	@Query(value="SELECT DISTINCT tbl_PROFESSORSHIP.* "
	           + "FROM tbl_EXECUTION_YEAR INNER JOIN tbl_PROFESSORSHIP "
	           + "ON tbl_EXECUTION_YEAR.ID = tbl_PROFESSORSHIP.EXECUTION_YEAR "
	           + "WHERE tbl_PROFESSORSHIP.TEACHER_CODE = :teacherId AND "
	           + "tbl_EXECUTION_YEAR.EXECUTION_YEAR = :executionYear ", nativeQuery = true)
	public List <Professorship> findByTeacherIdAndExecutionYear(@Param("teacherId") Long teacherId,
	                                                            @Param("executionYear") String executionYear);

	
	@Query(value="SELECT DISTINCT tbl_PROFESSORSHIP.* "
	           + "FROM tbl_EXECUTION_YEAR INNER JOIN tbl_PROFESSORSHIP "
	           + "ON tbl_EXECUTION_YEAR.ID = tbl_PROFESSORSHIP.EXECUTION_YEAR "
	           + "WHERE tbl_PROFESSORSHIP.TEACHER_CODE = :teacherId "
	           + "AND tbl_PROFESSORSHIP.COURSE_CODE = :courseId "
	           + "AND tbl_PROFESSORSHIP.DEGREE_CODE = :degreeId "
	           + "AND tbl_EXECUTION_YEAR.EXECUTION_YEAR = :executionYear ", nativeQuery = true)
	public List <Professorship> findByTeacherIdCourseIdDegreeIdAndExecutionYear(@Param("teacherId") Long teacherId,
																				@Param("courseId") Long courseId,
																				@Param("degreeId") Long degreeId,
	                                                                            @Param("executionYear") String executionYear);

}

