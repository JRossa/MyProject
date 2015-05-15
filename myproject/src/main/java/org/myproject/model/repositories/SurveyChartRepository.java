package org.myproject.model.repositories;



import java.util.List;

import org.myproject.model.entities.SurveyChart;
import org.myproject.model.entities.TeacherHours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface SurveyChartRepository extends JpaRepository<SurveyChart, Long> {

	
    @Query(value="SELECT tbl_SURVEY_CHART.* " 
    		   + "FROM tbl_SURVEY_CHART "
    		   + "WHERE tbl_SURVEY_CHART.SURVEY_ID = :surveyId ", nativeQuery = true)
    public List <SurveyChart> findBySurveyId (@Param("surveyId") Long surveyId);

    
    @Query(value="SELECT tbl_SURVEY_CHART.* " 
 		   + "FROM tbl_SURVEY_CHART "
 		   + "WHERE tbl_SURVEY_CHART.SURVEY_ID = :surveyId "
 		   + "AND tbl_SURVEY_CHART.QUESTION_ID = :questionId ", nativeQuery = true)
    public List <SurveyChart> findBySurveyIdAndQuestionId (@Param("surveyId") Long surveyId,
    		                                               @Param("questionId") Long questionId);

    
    @Query(value="INSERT INTO tbl_SURVEY_CHART (SURVEY_ID, QUESTION_ID, ANSWER_ID, ANSWER, FREQ) "
    		   + "SELECT tbl_SURVEY_TEACHER_ANSWER.SURVEY_ID, "
    		          + "tbl_SURVEY_TEACHER_ANSWER.QUESTION_ID, "
    		          + "tbl_SURVEY_TEACHER_ANSWER.ANSWER_ID, "
    		          + "tbl_SURVEY_TEACHER_ANSWER.ANSWER, "
    		          + "COUNT(tbl_SURVEY_TEACHER_ANSWER.ANSWER) AS FREQ "
    		          + "FROM tbl_SURVEY_TEACHER_ANSWER "
    		          + "WHERE tbl_SURVEY_TEACHER_ANSWER.SURVEY_ID = :surveyId "
    		          + "GROUP BY tbl_SURVEY_TEACHER_ANSWER.SURVEY_ID, "
    		          + "tbl_SURVEY_TEACHER_ANSWER.QUESTION_ID,  "
    		          + "tbl_SURVEY_TEACHER_ANSWER.ANSWER_ID, "
    		          + "tbl_SURVEY_TEACHER_ANSWER.ANSWER ", nativeQuery = true)
    public void insertSurveyId (@Param("surveyId") Long surveyId);

}
