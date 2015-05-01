package org.myproject.test.conndb.repositories;

import java.util.ArrayList;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.myproject.model.entities.SurveyQuestion;
import org.myproject.model.entities.SurveyQuestionScale;
import org.myproject.support.survey.SurveyMBean;


public class SurveyQuestionText {
//	public class SurveyQuestionText extends AbstractDatabaseTest {

	private static final Logger logger = Logger.getLogger(SurveyQuestionText.class);
    
    private SurveyMBean mbSurveyMBean = new SurveyMBean();
    
    
    @Test
    public void Teste() {
        String msg = "---------------- Listar Inquérito ----------------------------";
        
        System.out.println(msg);
        
        mbSurveyMBean.init();
        System.out.println(mbSurveyMBean.getScale().size());
        
    	for (SurveyQuestionScale sqs: mbSurveyMBean.getScale()) {
			if (sqs.getScale().equals(1)) {
				logger.info(sqs);
			}
    	}
    	
        System.out.println(msg);
       
        for (SurveyQuestion sq: mbSurveyMBean.getSurveyQuestion()) {
        	System.out.println(sq.getText());
        	System.out.println(sq.getScale());
        }
 
        
    	System.out.println("Índice 1 : " + mbSurveyMBean.getSurveyQuestion().get(1).getText());
    	System.out.println("Índice 1 : " + mbSurveyMBean.getSurveyQuestion().get(1).getScale());

    }

}
