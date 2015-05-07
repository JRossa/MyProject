package org.myproject.model.repositories;



import java.util.List;

import org.myproject.model.entities.Teacher;
import org.myproject.model.entities.TeacherHours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface TeacherHoursRepository extends JpaRepository<TeacherHours, Long> {

    public List <TeacherHours> findByTeacher (Teacher teacher);
    
    @Query(value="SELECT tbl_TEACHER_HOURS.* FROM tbl_TEACHER_HOURS "
               + "JOIN tbl_EXECUTION_YEAR ON tbl_teacher_hours.EXECUTION_YEAR = tbl_execution_year.ID "
               + "WHERE tbl_EXECUTION_YEAR.EXECUTION_YEAR = :executionYear "
               + "AND tbl_TEACHER_HOURS.TEACHER_ID = :teacherId", nativeQuery = true)
    public List <TeacherHours> findByTeacherAndExecutionYear (@Param("teacherId") Long teacherId, 
                                                              @Param("executionYear") String executionYear);


    @Query(value="SELECT DISTINCT tbl_TEACHER_HOURS.* FROM (tbl_TEACHER_HOURS "
               + "INNER JOIN tbl_SURVEY_TEACHER ON (tbl_TEACHER_HOURS.COURSE_ID = tbl_SURVEY_TEACHER.COURSE_ID) "
               + "AND (tbl_TEACHER_HOURS.TEACHER_ID = tbl_SURVEY_TEACHER.TEACHER_ID)) "
               + "INNER JOIN tbl_EXECUTION_YEAR ON tbl_TEACHER_HOURS.EXECUTION_YEAR = tbl_EXECUTION_YEAR.ID "
               + "WHERE tbl_EXECUTION_YEAR.EXECUTION_YEAR = :executionYear "
               + "AND tbl_SURVEY_TEACHER.SURVEY_ID = :surveyId ", nativeQuery = true)
    public List <TeacherHours> findByCourseSurveyDoneInExecutionYear (@Param("surveyId") Long surveyId,
            														  @Param("executionYear") String executionYear);
    
    @Query(value="SELECT DISTINCT tbl_TEACHER_HOURS.* FROM tbl_TEACHER_HOURS "
    		+ "WHERE tbl_TEACHER_HOURS.ID NOT IN "
    		+ "(SELECT DISTINCT tbl_TEACHER_HOURS.ID FROM (tbl_TEACHER_HOURS "
            + "INNER JOIN tbl_SURVEY_TEACHER ON (tbl_TEACHER_HOURS.COURSE_ID = tbl_SURVEY_TEACHER.COURSE_ID) "
            + "AND (tbl_TEACHER_HOURS.TEACHER_ID = tbl_SURVEY_TEACHER.TEACHER_ID)) "
            + "INNER JOIN tbl_EXECUTION_YEAR ON tbl_TEACHER_HOURS.EXECUTION_YEAR = tbl_EXECUTION_YEAR.ID "
            + "WHERE tbl_EXECUTION_YEAR.EXECUTION_YEAR = :executionYear "
            + "AND tbl_SURVEY_TEACHER.SURVEY_ID = :surveyId ) ", nativeQuery = true)
    public List <TeacherHours> findByCourseSurveyNOTDoneInExecutionYear (@Param("surveyId") Long surveyId,
    		                                                             @Param("executionYear") String executionYear);

    
    @Query(value="SELECT DISTINCT tbl_TEACHER_HOURS.* FROM (tbl_TEACHER_HOURS "
            + "INNER JOIN tbl_SURVEY_TEACHER ON (tbl_TEACHER_HOURS.TEACHER_ID = tbl_SURVEY_TEACHER.TEACHER_ID)) "
            + "INNER JOIN tbl_EXECUTION_YEAR ON tbl_TEACHER_HOURS.EXECUTION_YEAR = tbl_EXECUTION_YEAR.ID "
            + "WHERE tbl_EXECUTION_YEAR.EXECUTION_YEAR = :executionYear "
            + "AND tbl_SURVEY_TEACHER.SURVEY_ID = :surveyId ", nativeQuery = true)
    public List <TeacherHours> findByTeacherSurveyDoneInExecutionYear (@Param("surveyId") Long surveyId,
    		                                                           @Param("executionYear") String executionYear);
 
	@Query(value="SELECT DISTINCT tbl_TEACHER_HOURS.* FROM tbl_TEACHER_HOURS "
	 		+ "WHERE tbl_TEACHER_HOURS.ID NOT IN "
	 		+ "(SELECT DISTINCT tbl_TEACHER_HOURS.ID FROM (tbl_TEACHER_HOURS "
	        + "INNER JOIN tbl_SURVEY_TEACHER (tbl_TEACHER_HOURS.TEACHER_ID = tbl_SURVEY_TEACHER.TEACHER_ID)) "
	        + "INNER JOIN tbl_EXECUTION_YEAR ON tbl_TEACHER_HOURS.EXECUTION_YEAR = tbl_EXECUTION_YEAR.ID "
	        + "WHERE tbl_EXECUTION_YEAR.EXECUTION_YEAR = :executionYear "
	        + "AND tbl_SURVEY_TEACHER.SURVEY_ID = :surveyId ) ", nativeQuery = true)
	public List <TeacherHours> findByTeacherSurveyNOTDoneInExecutionYear (@Param("surveyId") Long surveyId,
			                                                              @Param("executionYear") String executionYear);
}
