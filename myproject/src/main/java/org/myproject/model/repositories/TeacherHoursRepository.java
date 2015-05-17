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


    @Query(value="SELECT DISTINCT tbl_TEACHER_HOURS.* FROM tbl_EXECUTION_YEAR "
    		   + "INNER JOIN ((tbl_TEACHER_HOURS "
    		   + "INNER JOIN tbl_SURVEY_TEACHER "
               + "ON (tbl_TEACHER_HOURS.COURSE_ID = tbl_SURVEY_TEACHER.COURSE_ID) "
               + "AND (tbl_TEACHER_HOURS.TEACHER_ID = tbl_SURVEY_TEACHER.TEACHER_ID)) "
               + "INNER JOIN tbl_survey ON tbl_SURVEY_TEACHER.SURVEY_ID = tbl_survey.ID) "
               + "ON (tbl_TEACHER_HOURS.EXECUTION_YEAR = tbl_EXECUTION_YEAR.ID) "
               + "AND (tbl_EXECUTION_YEAR.ID = tbl_survey.EXECUTION_YEAR) "
               + "WHERE tbl_EXECUTION_YEAR.EXECUTION_YEAR = :executionYear "
               + "AND tbl_SURVEY_TEACHER.SURVEY_ID = :surveyId ", nativeQuery = true)
    public List <TeacherHours> findByCourseSurveyDoneInExecutionYear (@Param("surveyId") Long surveyId,
            														  @Param("executionYear") String executionYear);
//	 INNER JOIN tbl_survey ON tbl_execution_year.ID = tbl_survey.EXECUTION_YEAR
    
    @Query(value="SELECT DISTINCT tbl_TEACHER_HOURS.* "
    		   + "FROM (tbl_EXECUTION_YEAR "
    		   + "INNER JOIN tbl_TEACHER_HOURS ON tbl_EXECUTION_YEAR.ID = tbl_TEACHER_HOURS.EXECUTION_YEAR) "
    		   + "INNER JOIN tbl_SURVEY ON tbl_EXECUTION_YEAR.ID = tbl_SURVEY.EXECUTION_YEAR "
    	  	   + "WHERE tbl_EXECUTION_YEAR.EXECUTION_YEAR = :executionYear AND tbl_TEACHER_HOURS.ID NOT IN "
    		   + "(SELECT DISTINCT tbl_TEACHER_HOURS.ID FROM tbl_EXECUTION_YEAR "
    		   + "INNER JOIN ((tbl_TEACHER_HOURS "
    		   + "INNER JOIN tbl_SURVEY_TEACHER "
               + "ON (tbl_TEACHER_HOURS.COURSE_ID = tbl_SURVEY_TEACHER.COURSE_ID) "
               + "AND (tbl_TEACHER_HOURS.TEACHER_ID = tbl_SURVEY_TEACHER.TEACHER_ID)) "
               + "INNER JOIN tbl_survey ON tbl_SURVEY_TEACHER.SURVEY_ID = tbl_survey.ID) "
               + "ON (tbl_TEACHER_HOURS.EXECUTION_YEAR = tbl_EXECUTION_YEAR.ID) "
               + "AND (tbl_EXECUTION_YEAR.ID = tbl_survey.EXECUTION_YEAR) "
               + "WHERE tbl_EXECUTION_YEAR.EXECUTION_YEAR = :executionYear "
               + "AND tbl_SURVEY_TEACHER.SURVEY_ID = :surveyId  ) ", nativeQuery = true)
    public List <TeacherHours> findByCourseSurveyNOTDoneInExecutionYear (@Param("surveyId") Long surveyId,
    		                                                             @Param("executionYear") String executionYear);

}   
