package org.myproject.support.survey;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;

import org.myproject.model.entities.Survey.SurveyType;
import org.myproject.model.entities.SurveyAnswer;
import org.myproject.model.entities.SurveyQuestion;
import org.myproject.model.entities.SurveyQuestionScale;
import org.myproject.model.entities.Teacher;
import org.myproject.report.AbstractBaseReportBean.ExportOption;
import org.myproject.support.teacher.TeacherMBean;
import org.myproject.support.teacherhours.TeacherHoursMBean;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;

@Named(value = "surveyMBean")
@Scope(value = WebApplicationContext.SCOPE_SESSION)
public class SurveyMBean {


	@Inject
	private TeacherHoursMBean mbTeacherHoursMBean;
	
    private SurveyType surveyType;
     
    private String answer;
    private Integer answerValue;
    
    private Boolean openQuestion = false;
    
    
    private int currentQuestion = 0;
    private Long answerId = 0L;
    private boolean over  = false;
    private boolean last = false;

    
    private List<SurveyQuestionScale> scale;
    
    private List<SurveyQuestion> surveyQuestion;
    
    private List<SelectItem> selectOneItemsScale;

    private List<SurveyAnswer> surveyAnswer;
    
    
    public SurveyMBean () {
    	
        this.currentQuestion = 0;
        this.answerId = 0L;
        this.over  = false;
        this.last = false;
        
   	    this.surveyAnswer = new ArrayList<SurveyAnswer>();

    }
    
    
    public void initQuestionScale () {
    	this.scale = new ArrayList<SurveyQuestionScale>();
    	
    	this.scale.add(new SurveyQuestionScale(1L, 1, 1, "Muito insatisfeito"));
    	this.scale.add(new SurveyQuestionScale(2L, 1, 2, "Insatisfeito"));
    	this.scale.add(new SurveyQuestionScale(3L, 1, 3, "Satisfeito"));
    	this.scale.add(new SurveyQuestionScale(4L, 1, 4, "Muito Satisfeito"));
    	
    	this.scale.add(new SurveyQuestionScale(5L, 2, 1, "Discordo Totalmente"));
    	this.scale.add(new SurveyQuestionScale(6L, 2, 2, "Discordo"));
    	this.scale.add(new SurveyQuestionScale(7L, 2, 3, "Indeciso"));
    	this.scale.add(new SurveyQuestionScale(8L, 2, 4, "Concordo"));
    	this.scale.add(new SurveyQuestionScale(9L, 2, 5, "Concordo Totalmente"));
    	this.scale.add(new SurveyQuestionScale(9L, 2, 6, "Concordo Muito Totalmente"));
   	
    	this.scale.add(new SurveyQuestionScale(10L, 3, 1, "Mau"));
    	this.scale.add(new SurveyQuestionScale(11L, 3, 2, "Insuficiente"));
    	this.scale.add(new SurveyQuestionScale(12L, 3, 3, "Razoável"));
    	this.scale.add(new SurveyQuestionScale(13L, 3, 4, "Bom"));
    	this.scale.add(new SurveyQuestionScale(14L, 3, 5, "Excelente"));
    	
    	this.scale.add(new SurveyQuestionScale(15L, 4, 1, "Nunca"));
    	this.scale.add(new SurveyQuestionScale(16L, 4, 2, "Pouco"));
    	this.scale.add(new SurveyQuestionScale(17L, 4, 3, "Algumas Vezes"));
    	this.scale.add(new SurveyQuestionScale(18L, 4, 4, "Sempre"));
    	
    	this.scale.add(new SurveyQuestionScale(19L, 5, 1, "1h"));
    	this.scale.add(new SurveyQuestionScale(20L, 5, 2, "2h"));
    	this.scale.add(new SurveyQuestionScale(21L, 5, 3, "3h"));
    	this.scale.add(new SurveyQuestionScale(22L, 5, 4, "5h"));
    	
    }
    
    
    public List<SurveyQuestionScale> getSurveyQuestionScale(Integer scaleId) {
    	List<SurveyQuestionScale> surveyScale = new ArrayList<SurveyQuestionScale>();
	
		for (SurveyQuestionScale sqs: this.scale) {
			if (sqs.getScale().equals(scaleId)) {
				surveyScale.add(sqs);
			}
		}
		
		return surveyScale;
	}
    
    
   @PostConstruct
    public void init () {
        // TODO - inicializar os valores para o selectOneRadio
    	
    	// Add Likert Items
    	this.initQuestionScale();
    	
    	
   	    
    	this.surveyQuestion = new ArrayList<SurveyQuestion>();
    	
    	this.surveyQuestion.add(new SurveyQuestion(1L, null, null, this.getSurveyQuestionScale(1), 
    			                                                 "Ambiente de Trabalho na AM"));
    	this.surveyQuestion.add(new SurveyQuestion(2L, null, null, null, 
                                                                 "Imagem Insitucional da AM"));
    	this.surveyQuestion.add(new SurveyQuestion(3L, null, null, this.getSurveyQuestionScale(3), 
                                                                 "Desempenho Global da AM"));
    	this.surveyQuestion.add(new SurveyQuestion(4L, null, null, this.getSurveyQuestionScale(3), 
                "O nº médio de alunos por turma é adequado para atingir os objectivos de aprendizagem ?"));
    }
    
    
    public SurveyType getSurveyType() {
		return surveyType;
	}

	public void setSurveyType(SurveyType surveyType) {
		this.surveyType = surveyType;
	}

	public List<SurveyQuestionScale> getScale() {
		return scale;
	}

	public void setScale(List<SurveyQuestionScale> scale) {
		this.scale = scale;
	}


	public List<SurveyQuestion> getSurveyQuestion() {
		return surveyQuestion;
	}

	public void setSurveyQuestion(List<SurveyQuestion> surveyQuestion) {
		this.surveyQuestion = surveyQuestion;
	}

	public String getAnswer() {
        System.out.println("Get Answer");
        
        return this.answer;
    }

	
	private void processAnswer (String answer) {
		
        if(this.currentQuestion < this.surveyQuestion.size()) {
        	
        	if (answer != null) {
        		System.out.println(this.openQuestion + "   To the question  \"" + 
            		this.surveyQuestion.get(this.currentQuestion).getText() + "\" your answer is " + answer);
        	}
        	
            this.answerId++;
            
            Integer i = this.currentQuestion;
        	this.surveyAnswer.add(new SurveyAnswer(this.answerId, 1L,  i.longValue(), 
        			                                      answer,
        			                                      this.mbTeacherHoursMBean.getSelectedTeacherHours().getCourse().getId()));
            this.currentQuestion++;
           

        	if (this.currentQuestion == (this.surveyQuestion.size() - 1)) {
                this.last = true;
            }
            
            if (this.currentQuestion == this.surveyQuestion.size()) {
                this.over = true;
            }
        }
		
	}
	
	
    public void setAnswer(String answer) {       
        System.out.println("Set Answer\n\n");
        
        if (answer != null) {
        	this.processAnswer(answer);
        }
        
        // TODO - Keep answer value
        this.answer = answer;
    }    

    public String getQuestion() {
    	
        System.out.println("Get Question   : " + this.openQuestion);
        
    	if (!this.over) {
        	this.openQuestion = (this.surveyQuestion.get(this.currentQuestion).getScale() == null);
        	this.answerValue = null;
        	this.answer = null;
        	
            return this.surveyQuestion.get(this.currentQuestion).getText();
        }
        else {
            return  getResourceProperty("labels", "survey_thanks");
        }
    }

    
    public String getGroupQuestion() {
    	
        System.out.println("Get Group Question   : " + this.openQuestion);
        

    	if (!this.over) {
//            return this.surveyQuestion.get(this.currentQuestion).getGroup().getName();
    		return "Teste";
        }
        else {
            return  "";
        }
    }
    

    public void next() {
        System.out.println("Next");
    }
    

    public void startCourse() {
    	
    	System.out.println("Teacher :" + 
    	         this.mbTeacherHoursMBean.getSelectedTeacherHours().getTeacher().getFullName());
       	System.out.println("Course :" + 
   	             this.mbTeacherHoursMBean.getSelectedTeacherHours().getCourse().getCode());
    	
        this.currentQuestion = 0;
        this.answerId = 0L;
        this.over = false;
        this.last = false;
	
    }

    
    public void startTeacher() {
    	
        this.currentQuestion = 0;
        this.answerId = 0L;
        this.over = false;
        this.last = false;
	
    }

    
    public String reset() {
        System.out.println("Reset");
        
        this.currentQuestion = 0;
        this.answerId = 0L;
        this.over = false;
        this.last = false;
        
        return "";
        //return "/pages/survey/questionTeacher.xhtml";
    }

    public boolean isOver() {
        return over;
    }

    public void setOver(boolean over) {
        this.over = over;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }
    
    
    public List<SurveyAnswer> getSurveyAnswer() {
		return surveyAnswer;
	}


	public void setSurveyAnswer(List<SurveyAnswer> surveyAnswer) {
		this.surveyAnswer = surveyAnswer;
	}


	public void listAnswers () {

    	System.out.println("----------------- Survey N : " + this.getSurveyAnswer().size());

    	for (SurveyAnswer sa: this.getSurveyAnswer()) {
    		System.out.println("Id              : " + sa.getId());
    		System.out.println("Survey          : " + sa.getSurvey());
    		System.out.println("Survey Question : " + sa.getQuestion());
    		System.out.println("Survey Answer   : " + sa.getValue());
    		System.out.println("Survey Course   : " + sa.getCourse());
    	}

	}
	
	
	public void save () {
    	this.reset();
    	
    	this.listAnswers();
    	
    }
    
    
    public void sendEMail () {
    	
    }
    
    
    public void disableButtons () {
    }
    
    
    
    public Integer getAnswerValue() {
		return answerValue;
	}


	public void setAnswerValue(Integer answerValue) {

		
		System.out.println("set Answer Value  :  " + answerValue);
		
		if (answerValue != null) {
			this.processAnswer(answerValue.toString());
		}
		
        // TODO - Keep answer value

		this.answerValue = answerValue;
	}

	
	public Boolean getOpenQuestion() {
		return openQuestion;
	}


	public void setOpenQuestion(Boolean openQuestion) {
		this.openQuestion = openQuestion;
	}


	public List<SelectItem> getSelectOneItemsScale() {
        this.selectOneItemsScale = new ArrayList<SelectItem>();
        
        List<SurveyQuestionScale> surveyScale = this.surveyQuestion.get(this.currentQuestion).getScale();

        if (surveyScale != null) {
	        for (SurveyQuestionScale scale : surveyScale) {
	            SelectItem selectItem = new SelectItem(scale.getValue(), scale.getText());
	            this.selectOneItemsScale.add(selectItem);
	        }
        } else {
        	SelectItem selectItem = new SelectItem(0, "");
            this.selectOneItemsScale.add(selectItem);
        }
        
        return selectOneItemsScale;
    }

	
    public String getResourceProperty(String resource, String label) {
        Application application = FacesContext.getCurrentInstance().getApplication();
        ResourceBundle bundle = application.getResourceBundle(FacesContext.getCurrentInstance(), resource);

        return bundle.getString(label);
    }

}