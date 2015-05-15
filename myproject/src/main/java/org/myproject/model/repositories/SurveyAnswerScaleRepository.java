package org.myproject.model.repositories;


import java.util.List;

import org.myproject.model.entities.SurveyAnswerScale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SurveyAnswerScaleRepository extends JpaRepository<SurveyAnswerScale, Long> {

	
	@Query(value="SELECT tbl_SURVEY_ANSWER_SCALE.* "
            + "FROM tbl_SURVEY_ANSWER_SCALE "
			+ "WHERE tbl_SURVEY_ANSWER_SCALE.SCALE_TYPE =:scaleType "
            + "ORDER BY tbl_SURVEY_ANSWER_SCALE.VALUE ", nativeQuery = true)
	public List<SurveyAnswerScale> findByScaleType(@Param("scaleType") Long scaleType);

	@Query(value="SELECT tbl_SURVEY_ANSWER_SCALE.ID "
            + "FROM tbl_SURVEY_ANSWER_SCALE "
			+ "WHERE tbl_SURVEY_ANSWER_SCALE.VALUE = 0 ", nativeQuery = true)
	public Long findOpenQuestionId();

}
