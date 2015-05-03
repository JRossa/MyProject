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

import org.myproject.model.entities.Survey;
import org.myproject.model.entities.SurveyAnswer;
import org.myproject.model.entities.SurveyQuestion;
import org.myproject.model.entities.SurveyAnswerScale;
import org.myproject.model.repositories.SurveyAnswerScaleRepository;
import org.myproject.model.repositories.SurveyQuestionRepository;
import org.myproject.model.repositories.SurveyRepository;
import org.myproject.model.utils.BaseBean;
import org.myproject.support.teacher.TeacherMBean;
import org.myproject.support.teacherhours.TeacherHoursMBean;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;

@Named(value = "surveyMBean")
@Scope(value = WebApplicationContext.SCOPE_SESSION)
public class SurveyMBean extends BaseBean {

	private static final long serialVersionUID = 1984764917382818173L;
	
	public enum SurveyType {TEACHER, TEACHER_UC, STUDENT, STUDENT_UC}

	@Inject
	private SurveyAnswerScaleRepository surveyAnswerScaleRepository;

	@Inject
	private SurveyQuestionRepository surveyQuestionRepository;

	@Inject
	private SurveyRepository surveyRepository;

	@Inject
	private TeacherHoursMBean mbTeacherHoursMBean;

	@Inject
	private TeacherMBean mbTeacherMBean;

    private String answer;
    private Integer answerValue;
    
    private Boolean openQuestion = false;
    
    
    private int currentQuestion = 0;
    private Long answerId = 0L;
    
    private boolean over  = false;
    private boolean last = false;
    private boolean noSurvey = true;
    
    private String title;

    private SurveyType surveyType;
    
    private Survey activeSurvey;

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
    
    @PostConstruct
    public void init () {
    	
//    	this.surveyQuestion = this.surveyQuestionRepository.findAll();
    }
   
 
    public List<SurveyAnswerScale> getSurveyQuestionScale(Long scaleType) {

		return  this.surveyAnswerScaleRepository.findByScaleType(scaleType);

	}

    public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public SurveyType getSurveyType() {
		return surveyType;
	}

	public void setSurveyType(SurveyType surveyType) {
		this.surveyType = surveyType;
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
    		if (this.surveyQuestion == null) {
    			return "EM COSTRUÇÂO  2";
    		}
    		
        	this.openQuestion = (this.surveyQuestion.get(this.currentQuestion).getScaleType().getScaleList().size() == 1);
        	this.answerValue = null;
        	this.answer = null;
        	
            return this.surveyQuestion.get(this.currentQuestion).getText();
        }
        else {
        	if (this.noSurvey) {
        		return "EM COSTRUÇÂO";
        	}
        	
            return  getResourceProperty("labels", "survey_thanks");
        }
    }

    
    public String getGroupQuestion() {
    	
        System.out.println("Get Group Question   : " + this.openQuestion);
        

    	if (!this.over) {
    		if (this.surveyQuestion == null) {
    			return "---------------------";
    		}
    		
            return this.surveyQuestion.get(this.currentQuestion).getQuestionGroup().getDescription();
        }
        else {
            return  "";
        }
    }
    

    public void next() {
        System.out.println("Next");
    }
    

    
    public void startCourse() {
    	
    	this.title = "(UC - " 
                + this.mbTeacherHoursMBean.getSelectedTeacherHours().getCourse().getCode()
                + ")";
    	
    	System.out.println("Title :" + this.title);
    	
    	System.out.println("Teacher :" + 
    	         this.mbTeacherHoursMBean.getSelectedTeacherHours().getTeacher().getFullName());
    	
    	this.activeSurvey = this.surveyRepository.findByActiveType(SurveyType.TEACHER.toString());
    	if (this.activeSurvey != null) {
	    	this.surveyQuestion = this.surveyQuestionRepository.findBySurvey(this.activeSurvey.getId());
	    	
	    	if (this.surveyQuestion != null) {

	    		this.currentQuestion = 0;
		        this.answerId = 0L;
		        this.over = false;
		        this.last = false;
		        this.noSurvey = false;
		        
		        return;
	    	}
    	}
    	
	    this.over = true;
	    this.last = true;
	    this.noSurvey = true;
	
    }

    
    public void startTeacher() {

    	this.title = "(Docente - " 
                + this.mbTeacherMBean.getSelectedTeacher().getFullName()
                + ")";
    	
    	System.out.println("Title :" + this.title);

		this.activeSurvey = this.surveyRepository.findByActiveType(SurveyType.TEACHER_UC.toString());
    	if (this.activeSurvey != null) {

	    	this.surveyQuestion = this.surveyQuestionRepository.findBySurvey(this.activeSurvey.getId());
	
	    	if (this.surveyQuestion != null) {
	    		
		        this.currentQuestion = 0;
		        this.answerId = 0L;
		        this.over = false;
		        this.last = false;
		        this.noSurvey = false;
		        
		        return;
	    	}
    	}
    	
	    this.over = true;
	    this.last = true;
	    this.noSurvey = true;
	    
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
    
    public boolean isNoSurvey() {
		return noSurvey;
	}

	public void setNoSurvey(boolean noSurvey) {
		this.noSurvey = noSurvey;
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
        
        if (this.surveyQuestion == null) {
        	SelectItem selectItem = new SelectItem(0, "");
            this.selectOneItemsScale.add(selectItem);
            return selectOneItemsScale;
        }

        List<SurveyAnswerScale> surveyScale = this.surveyQuestion.get(this.currentQuestion).getScaleType().getScaleList();

        if (surveyScale != null) {
	        for (SurveyAnswerScale scale : surveyScale) {
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