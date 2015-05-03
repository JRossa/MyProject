package org.myproject.test.conndb.repositories;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.myproject.model.entities.Survey;
import org.myproject.model.entities.SurveyQuestion;
import org.myproject.model.entities.SurveyAnswerScale;
import org.myproject.model.entities.SurveyScaleType;
import org.myproject.model.repositories.SurveyAnswerScaleRepository;
import org.myproject.model.repositories.SurveyQuestionRepository;
import org.myproject.model.repositories.SurveyRepository;
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

	@Inject
	private SurveyRepository surveyRepository;

    SurveyMBean mbSurveyMBean = new SurveyMBean();
    
    public void SurveyScaleTypeTest () {
    	List<SurveyScaleType> scaleType = this.surveyScaleTypeRepository.findAll();
    	logger.info(scaleType);

    }  	

    public void SurveyAnswerScaleTest () {
    	List<SurveyAnswerScale> answerScale = this.surveyAnswerScaleRepository.findAll();
    	logger.info(answerScale);

    }
    
    public void SurveyAnswerScaleTypeTest () {
    	List<SurveyAnswerScale> answerScaleType = this.surveyAnswerScaleRepository.findByScaleType(1L);
    	logger.info(answerScaleType);
  
    }
    
    public void SurveyQuestionSizeTest () {
    	List<SurveyQuestion> question = this.surveyQuestionRepository.findAll();
   	
		logger.info(question);
		
    	for (SurveyQuestion sq: question) {
            System.out.println("Get Question   : " + sq.getScaleType().getScaleList().size());
    	}
    }
    
    public void SurveyQuestionTest () {
    	List<SurveyQuestion> question = this.surveyQuestionRepository.findAll();
    	
    	for (SurveyQuestion sq: question) {
    		List<SurveyAnswerScale> answerScaleType = 
    				          this.surveyAnswerScaleRepository.findByScaleType(sq.getScaleType().getId());
    		logger.info(sq);
    		logger.info(answerScaleType);
    	}
    }	

    
    public void SurveyTest () {	
    	System.out.println("Get Question   : " + mbSurveyMBean.getSurveyType().TEACHER);
    	
    	Survey survey = this.surveyRepository.findByActiveType(mbSurveyMBean.getSurveyType().TEACHER.toString());

		logger.info(survey);

    }
    
    @Test
    public void SurveyQuestionSurveyTest () {	
    	Survey survey = this.surveyRepository.findByActiveType(mbSurveyMBean.getSurveyType().TEACHER_UC.toString());

    	List<SurveyQuestion> question = this.surveyQuestionRepository.findBySurvey(survey.getId());
       	
		logger.info(survey.getQuestionList());
		
    }

}
