package org.myproject.model.repositories;


import java.util.List;

import org.myproject.model.entities.Survey;
import org.myproject.model.entities.SurveyAnswerScale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface SurveyRepository extends JpaRepository<Survey, Long> {

	
	@Query(value="SELECT tbl_SURVEY.* "
            + "FROM tbl_SURVEY "
			+ "INNER JOIN tbl_SURVEY_TYPE ON tbl_SURVEY.SURVEY_TYPE = tbl_SURVEY_TYPE.ID "
			+ "WHERE tbl_SURVEY_TYPE.DESCRIPTION = :surveyType "
            + "AND tbl_SURVEY.ACTIVE = true ", nativeQuery = true)
	public Survey findByActiveType(@Param("surveyType") String surveyType);

}
