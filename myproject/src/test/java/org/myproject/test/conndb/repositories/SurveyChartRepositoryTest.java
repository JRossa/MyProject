package org.myproject.test.conndb.repositories;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.myproject.model.entities.Survey;
import org.myproject.model.entities.SurveyChart;
import org.myproject.model.entities.SurveyQuestion;
import org.myproject.model.entities.SurveyAnswerScale;
import org.myproject.model.entities.SurveyScaleType;
import org.myproject.model.entities.SurveyTypeGroup;
import org.myproject.model.repositories.SurveyAnswerScaleRepository;
import org.myproject.model.repositories.SurveyChartRepository;
import org.myproject.model.repositories.SurveyQuestionRepository;
import org.myproject.model.repositories.SurveyRepository;
import org.myproject.model.repositories.SurveyScaleTypeRepository;
import org.myproject.model.repositories.SurveyTypeGroupRepository;
import org.myproject.model.utils.Cryptor;
import org.myproject.support.survey.SurveyMBean;
import org.myproject.test.conndb.AbstractDatabaseTest;

public class SurveyChartRepositoryTest extends AbstractDatabaseTest {
//	public class SurveyQuestionText extends AbstractDatabaseTest {

	private static final Logger logger = Logger.getLogger(SurveyChartRepositoryTest.class);
    
	
    @Inject
    private SurveyChartRepository surveyChartRepository;

    
   
    public void SurveyChartTest () {
    	List<SurveyChart> chart = this.surveyChartRepository.findBySurveyId(3L);
    	logger.info(chart);

    }

    public void SurveyChartQuestionTest () {
    	List<SurveyChart> chart = this.surveyChartRepository.findBySurveyIdAndQuestionId(3L, 8L);
    	logger.info(chart);
  
    }
 
    public void A1_DeleteTest () {
    	this.surveyChartRepository.deleteSurveyId(3L);
    	this.surveyChartRepository.flush();
    }
    
    
    public void listChartTest () {
    	List<SurveyChart> chart = this.surveyChartRepository.findAll();

		logger.info(chart);
    }
    
    @Test 
    public void A2_SurveyInsertTest () {
    	this.surveyChartRepository.deleteSurveyId(3L);
    	this.surveyChartRepository.flush();
    	
    	this.surveyChartRepository.insertSurveyId(3L);
    	this.surveyChartRepository.flush();
    	
    	List<SurveyChart> chart = this.surveyChartRepository.findAll();

		logger.info(chart);
		
    }
    
 
}
