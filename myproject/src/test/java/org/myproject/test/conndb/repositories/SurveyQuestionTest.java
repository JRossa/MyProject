package org.myproject.test.conndb.repositories;

import java.util.ArrayList;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.myproject.model.entities.SurveyQuestion;
import org.myproject.model.entities.SurveyAnswerScale;
import org.myproject.support.survey.SurveyMBean;
import org.myproject.test.conndb.AbstractDatabaseTest;


public class SurveyQuestionTest  {
//	public class SurveyQuestionText extends AbstractDatabaseTest {

	private static final Logger logger = Logger.getLogger(SurveyQuestionTest.class);
    
    private SurveyMBean mbSurveyMBean = new SurveyMBean();
    
    
    @Test
    public void Teste() {
        String msg = "---------------- Listar Inquérito ----------------------------";
        
        System.out.println(msg);
        
        mbSurveyMBean.init();
    	
        System.out.println(msg);
       
        for (SurveyQuestion sq: mbSurveyMBean.getSurveyQuestion()) {
        	System.out.println(sq.getText());
        	System.out.println(sq.getScaleType().getId());
        }
 
        
    	System.out.println("Índice 1 : " + mbSurveyMBean.getSurveyQuestion().get(1).getText());
    	System.out.println("Índice 1 : " + mbSurveyMBean.getSurveyQuestion().get(1).getId());

    }

}
