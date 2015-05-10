package org.myproject.model.repositories;


import java.util.List;

import org.myproject.model.entities.Survey;
import org.myproject.model.entities.SurveyQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SurveyQuestionRepository extends JpaRepository<SurveyQuestion, Long> {

	
}
