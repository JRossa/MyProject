package org.myproject.model.repositories;


import java.util.List;

import org.myproject.model.entities.Survey;
import org.myproject.model.entities.SurveyQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SurveyQuestionRepository extends JpaRepository<SurveyQuestion, Long> {

	
	@Query(value="SELECT tbl_SURVEY_QUESTION.* "
            + "FROM tbl_SURVEY_QUESTION "
			+ "WHERE tbl_SURVEY_QUESTION.SURVEY_ID = :survey "
            + "ORDER BY tbl_SURVEY_QUESTION.GROUP_ID ASC, tbl_SURVEY_QUESTION.ID ", nativeQuery = true)
	public List<SurveyQuestion> findBySurvey(@Param("survey") Long survey);

}
