package org.myproject.test.conndb.repositories;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.myproject.model.entities.SurveyQuestion;
import org.myproject.model.entities.SurveyAnswerScale;
import org.myproject.model.entities.SurveyScaleType;
import org.myproject.model.repositories.SurveyAnswerScaleRepository;
import org.myproject.model.repositories.SurveyQuestionRepository;
import org.myproject.model.repositories.SurveyScaleTypeRepository;
import org.myproject.support.survey.SurveyMBean;
import org.myproject.test.conndb.AbstractDatabaseTest;


public class SurveyQuestionRepositoryTest extends AbstractDatabaseTest {
//	public class SurveyQuestionText extends AbstractDatabaseTest {

	private static final Logger logger = Logger.getLogger(SurveyQuestionRepositoryTest.class);
    
	
	@Inject
	private SurveyAnswerScaleRepository surveyAnswerScaleRepository;

	@Inject
	private SurveyScaleTypeRepository surveyScaleTypeRepository;

	@Inject
	private SurveyQuestionRepository surveyQuestionRepository;

    
    @Test
    public void TestAnswerScale () {
//    	List<SurveyScaleType> scaleType = this.surveyScaleTypeRepository.findAll();
//    	logger.info(scaleType);

    	
//    	List<SurveyAnswerScale> answerScale = this.surveyAnswerScaleRepository.findAll();
//    	logger.info(answerScale);

    	
//    	List<SurveyAnswerScale> answerScaleType = this.surveyAnswerScaleRepository.findByScaleType(1L);
//    	logger.info(answerScaleType);
    	
    	List<SurveyQuestion> question = this.surveyQuestionRepository.findAll();
 
    	
		logger.info(question);
		
    	for (SurveyQuestion sq: question) {
            System.out.println("Get Question   : " + sq.getScaleType().getScaleList().size());
    	}
		
//    	for (SurveyQuestion sq: question) {
//    		List<SurveyAnswerScale> answerScaleType = 
//    				          this.surveyAnswerScaleRepository.findByScaleType(sq.getScaleType().getId());
//    		logger.info(sq);
//    		logger.info(answerScaleType);
//    	}
    	
    	
    	
    }
    

    public void Teste() {
    }
}
