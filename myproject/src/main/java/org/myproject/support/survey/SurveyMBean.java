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

import org.myproject.model.entities.LogUser;
import org.myproject.model.entities.Survey;
import org.myproject.model.entities.SurveyQuestion;
import org.myproject.model.entities.SurveyAnswerScale;
import org.myproject.model.repositories.SurveyAnswerScaleRepository;
import org.myproject.model.repositories.SurveyRepository;
import org.myproject.model.repositories.UserRepository;
import org.myproject.model.utils.BaseBean;
import org.myproject.support.teacher.TeacherMBean;
import org.myproject.support.teacherhours.TeacherHoursExecutionYearMBean;
import org.myproject.support.teacherhours.TeacherHoursMBean;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;

@Named(value = "surveyMBean")
@Scope(value = WebApplicationContext.SCOPE_SESSION)
public class SurveyMBean extends BaseBean {

	private static final long serialVersionUID = 1984764917382818173L;
	
	public enum SurveyTypeGroup {TEACHER, TEACHER_UC, STUDENT, STUDENT_UC}

	public enum SurveyOption {TEACHER, TEACHERHOURS, TEACHERHOURSEXECUTIONYEAR}
	
	
	@Inject
	private SurveyAnswerScaleRepository surveyAnswerScaleRepository;

	@Inject
	private SurveyRepository surveyRepository;

	@Inject
	private UserRepository userRepository;

	@Inject
	private TeacherMBean mbTeacherMBean;

	@Inject
	private TeacherHoursMBean mbTeacherHoursMBean;

	@Inject
	private TeacherHoursExecutionYearMBean mbTeacherHoursExecutionYearMBean;

    private String answer;
    private Integer answerValue;
    
    private Boolean openQuestion = false;
    
    
    private int currentQuestion = 0;
    private Long answerId = 0L;
    
    private boolean over  = false;
    private boolean last = false;
    private boolean noSurvey = true;
    
    private String title;

    private SurveyTypeGroup surveyTypeGroup;
    
    private SurveyOption surveyOption;
    
    private Survey activeSurvey;

    private List<SurveyQuestion> surveyQuestion;
    
    private String auxGroupQuestion;
    
    private List<SelectItem> selectOneItemsScale;

    private List<SurveyAnswer> surveyAnswer;
    
    private LogUser user;
    
    // listTeacherHours... - control buttons
    private Boolean renderedSurveyDone;

    private Boolean renderedTeacherHours;
    
    private Boolean renderedTeacherHoursExecutionYear;

    private Boolean disableButtons;

    
    public SurveyMBean () {
    	
        this.currentQuestion = 0;
        this.answerId = 0L;
        this.over  = false;
        this.last = false;
        
   	    this.surveyAnswer = new ArrayList<SurveyAnswer>();

    }
 
    
    public void setupUserData () {
    	 
        this.setDisableButtons(false);
        this.setRenderedSurveyDone(false);

        String username = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username");

        this.user = this.userRepository.findByUserName(username);
        
        // WARN - It happens when the 
        if (this.user == null) {
        	return;
        }
        
        if (this.user.getTeacher().getId() != null) {
        	
        	if ((this.user.getLogRole().getRolename().equals("ROLE_ADMIN")) || 
        			(this.user.getLogRole().getRolename().equals("ROLE_USER_U")) ||
        			(this.user.getLogRole().getRolename().equals("ROLE_USER_T"))) {
        		this.setRenderedSurveyDone(true);
        	}
        	else {
        		this.setRenderedSurveyDone(false);
        		
        	}
         }
    }

    @PostConstruct
    public void init () {
    	
    	this.setupUserData();
    	
//    	this.surveyQuestion = this.surveyQuestionRepository.findAll();
    }

    
    public Boolean getRenderedSurveyDone() {
		return renderedSurveyDone;
	}


	public void setRenderedSurveyDone(Boolean renderedSurveyDone) {
		this.renderedSurveyDone = renderedSurveyDone;
	}


	public Boolean getDisableButtons() {
		return disableButtons;
	}


	public void setDisableButtons(Boolean disableButtons) {
		this.disableButtons = disableButtons;
	}


	public Boolean getRenderedTeacherHours() {
		return renderedTeacherHours;
	}


	public void setRenderedTeacherHours(Boolean renderedTeacherHours) {
		this.renderedTeacherHours = renderedTeacherHours;
	}


	public Boolean getRenderedTeacherHoursExecutionYear() {
		return renderedTeacherHoursExecutionYear;
	}


	public void setRenderedTeacherHoursExecutionYear(
			Boolean renderedTeacherHoursExecutionYear) {
		this.renderedTeacherHoursExecutionYear = renderedTeacherHoursExecutionYear;
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

	public Survey getActiveSurvey() {
		return activeSurvey;
	}

	public void setActiveSurvey(Survey activeSurvey) {
		this.activeSurvey = activeSurvey;
	}


	public SurveyTypeGroup getSurveyTypeGroup() {
		return surveyTypeGroup;
	}

	public void setSurveyTypeGroup(SurveyTypeGroup surveyTypeGroup) {
		this.surveyTypeGroup = surveyTypeGroup;
	}

	public SurveyOption getSurveyOption() {
		return surveyOption;
	}

	public void setSurveyOption(SurveyOption surveyOption) {
		this.surveyOption = surveyOption;
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
        	
	            this.answerId++;
	            
	            Integer i = this.currentQuestion;
	            Long question = this.surveyQuestion.get(this.currentQuestion).getId();
	            
	            if (this.surveyTypeGroup.equals(SurveyTypeGroup.TEACHER_UC)) {
	            	this.surveyAnswer.add(new SurveyAnswer(this.activeSurvey.getId(), i, question, answer,
	                        					this.mbTeacherHoursMBean.getSelectedTeacherHours().getCourse().getId()));
	            }
	 
	            if (this.surveyTypeGroup.equals(SurveyTypeGroup.TEACHER)) {
	            	this.surveyAnswer.add(new SurveyAnswer(this.activeSurvey.getId(), i, question, answer, null));
	            }
	            
	            System.out.println("Question #" + this.currentQuestion + " -  Stored ");

        	}

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
        
        this.answer = answer;
    }    

    public String getQuestion() {
    	
        System.out.println("Get Question   : " + this.openQuestion);
       
    	if (!this.over) {
    		if (this.surveyQuestion == null) {
    			return "EM COSTRUÇÂO  - No questions !!!";
    		}
    		
        	this.openQuestion = (this.surveyQuestion.get(this.currentQuestion).getScaleType().getScaleList().size() == 1);
        	this.answerValue = null;
        	this.answer = null;
        	
            return this.surveyQuestion.get(this.currentQuestion).getText();
        }
        else {
        	if (this.noSurvey) {
        		return "EM COSTRUÇÂO  - No surveys !!!";
        	}
        	
            return  getResourceProperty("labels", "survey_thanks");
        }
    }

    
    public String getAuxGroupQuestion() {
		return auxGroupQuestion;
	}


	public void setAuxGroupQuestion(String auxGroupQuestion) {
		this.auxGroupQuestion = auxGroupQuestion;
	}


	public String getGroupQuestion() {
    	String groupQuestion;
 		
        System.out.println("Get Group Question   : " + this.openQuestion);
        

    	if (!this.over) {
    		if (this.surveyQuestion == null) {
    			return "---------------------";
    		}
    		
    	       String[] group = this.surveyQuestion.get(this.currentQuestion).getQuestionGroup().getDescription().split(";");
    		
    	       groupQuestion = group[0];
    	       
    	       if (group.length > 1) {
    	    	   this.setAuxGroupQuestion(group[1]);
    	       } else
    	    	   this.setAuxGroupQuestion(""); 
    	       
            return groupQuestion;
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
    	
    	this.surveyAnswer = new ArrayList<SurveyAnswer>();
    	
    	this.renderedTeacherHours = true;
       	this.renderedTeacherHoursExecutionYear = false;

    	this.setSurveyTypeGroup(SurveyTypeGroup.TEACHER_UC);
    	this.setSurveyOption(SurveyOption.TEACHERHOURS);
    	
    	this.activeSurvey = this.surveyRepository.findByActiveType(SurveyTypeGroup.TEACHER_UC.toString());
    	
    	if (this.activeSurvey != null) {
    		
    		this.surveyQuestion = this.activeSurvey.getSurveyType().getQuestionList();
	    	
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

    	this.surveyAnswer = new ArrayList<SurveyAnswer>();

    	this.renderedTeacherHours = false;
       	this.renderedTeacherHoursExecutionYear = false;

    	this.setSurveyTypeGroup(SurveyTypeGroup.TEACHER);
    	this.setSurveyOption(SurveyOption.TEACHER);
    	
		this.activeSurvey = this.surveyRepository.findByActiveType(SurveyTypeGroup.TEACHER.toString());
		
    	if (this.activeSurvey != null) {

    		this.surveyQuestion = this.activeSurvey.getSurveyType().getQuestionList();

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

 
    public void startTeacherExecutionYear() {

    	this.title = "(Docente - " 
                + this.mbTeacherHoursExecutionYearMBean.getSelectedTeacherHoursExecutionYear().getTeacher().getFullName()
                + ")";
    	
    	System.out.println("Title :" + this.title);

    	this.surveyAnswer = new ArrayList<SurveyAnswer>();
 
    	this.renderedTeacherHours = false;
       	this.renderedTeacherHoursExecutionYear = true;

    	this.setSurveyTypeGroup(SurveyTypeGroup.TEACHER);
    	this.setSurveyOption(SurveyOption.TEACHERHOURSEXECUTIONYEAR);
    	
		this.activeSurvey = this.surveyRepository.findByActiveType(SurveyTypeGroup.TEACHER.toString());
		
    	if (this.activeSurvey != null) {

    		this.surveyQuestion = this.activeSurvey.getSurveyType().getQuestionList();
    		
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