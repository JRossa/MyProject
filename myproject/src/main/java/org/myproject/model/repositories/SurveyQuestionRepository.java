package org.myproject.model.repositories;


import org.myproject.model.entities.SurveyQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyQuestionRepository extends JpaRepository<SurveyQuestion, Long> {

	

}
