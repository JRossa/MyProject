package org.myproject.model.repositories;



import java.util.List;

import org.myproject.model.entities.Teacher;
import org.myproject.model.entities.TeacherHoursExecutionYear;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface TeacherHoursExecutionYearRepository extends JpaRepository<TeacherHoursExecutionYear, Long> {

    public List <TeacherHoursExecutionYear> findByTeacher (Teacher teacher);
    
    @Query(value="SELECT tbl_TEACHER_HOURS_EXECUTION_YEAR.* FROM tbl_TEACHER_HOURS_EXECUTION_YEAR "
               + "JOIN tbl_EXECUTION_YEAR ON tbl_TEACHER_HOURS_EXECUTION_YEAR.EXECUTION_YEAR = tbl_EXECUTION_YEAR.ID "
               + "WHERE tbl_EXECUTION_YEAR.EXECUTION_YEAR = :executionYear "
               + "AND tbl_TEACHER_HOURS_EXECUTION_YEAR.TEACHER_ID = :teacherId", nativeQuery = true)
    public List <TeacherHoursExecutionYear> findByTeacherAndExecutionYear (@Param("teacherId") Long teacherId, 
                                                                           @Param("executionYear") String executionYear);


    @Query(value="SELECT tbl_TEACHER_HOURS_EXECUTION_YEAR.* FROM tbl_TEACHER_HOURS_EXECUTION_YEAR "
            + "JOIN tbl_EXECUTION_YEAR ON tbl_TEACHER_HOURS_EXECUTION_YEAR.EXECUTION_YEAR = tbl_EXECUTION_YEAR.ID "
            + "WHERE tbl_EXECUTION_YEAR.EXECUTION_YEAR = :executionYear ", nativeQuery = true)
    public List <TeacherHoursExecutionYear> findByExecutionYear (@Param("executionYear") String executionYear);

    
    @Query(value="SELECT DISTINCT tbl_TEACHER_HOURS_EXECUTION_YEAR.* FROM (tbl_TEACHER_HOURS_EXECUTION_YEAR "
            + "INNER JOIN tbl_SURVEY_TEACHER ON (tbl_TEACHER_HOURS_EXECUTION_YEAR.TEACHER_ID = tbl_SURVEY_TEACHER.TEACHER_ID)) "
            + "INNER JOIN tbl_EXECUTION_YEAR ON tbl_TEACHER_HOURS_EXECUTION_YEAR.EXECUTION_YEAR = tbl_EXECUTION_YEAR.ID "
            + "WHERE tbl_EXECUTION_YEAR.EXECUTION_YEAR = :executionYear "
            + "AND tbl_SURVEY_TEACHER.SURVEY_ID = :surveyId ", nativeQuery = true)
    public List <TeacherHoursExecutionYear> findByTeacherSurveyDoneInExecutionYear (@Param("surveyId") Long surveyId,
    		                                                                        @Param("executionYear") String executionYear);
 
	@Query(value="SELECT DISTINCT tbl_TEACHER_HOURS_EXECUTION_YEAR.* FROM tbl_TEACHER_HOURS_EXECUTION_YEAR "
	 		+ "WHERE tbl_TEACHER_HOURS_EXECUTION_YEAR.ID NOT IN "
	 		+ "(SELECT DISTINCT tbl_TEACHER_HOURS_EXECUTION_YEAR.ID FROM (tbl_TEACHER_HOURS_EXECUTION_YEAR "
            + "INNER JOIN tbl_SURVEY_TEACHER ON (tbl_TEACHER_HOURS_EXECUTION_YEAR.TEACHER_ID = tbl_SURVEY_TEACHER.TEACHER_ID)) "
            + "INNER JOIN tbl_EXECUTION_YEAR ON tbl_TEACHER_HOURS_EXECUTION_YEAR.EXECUTION_YEAR = tbl_EXECUTION_YEAR.ID "
            + "WHERE tbl_EXECUTION_YEAR.EXECUTION_YEAR = :executionYear "
            + "AND tbl_SURVEY_TEACHER.SURVEY_ID = :surveyId) ", nativeQuery = true)
	public List <TeacherHoursExecutionYear> findByTeacherSurveyNOTDoneInExecutionYear (@Param("surveyId") Long surveyId,
			                                                                           @Param("executionYear") String executionYear);

}
