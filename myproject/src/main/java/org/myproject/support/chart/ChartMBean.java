package org.myproject.support.chart;

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
import org.myproject.model.entities.SurveyChart;
import org.myproject.model.entities.SurveyQuestion;
import org.myproject.model.entities.SurveyAnswerScale;
import org.myproject.model.repositories.SurveyAnswerScaleRepository;
import org.myproject.model.repositories.SurveyChartRepository;
import org.myproject.model.repositories.SurveyRepository;
import org.myproject.model.repositories.UserRepository;
import org.myproject.model.utils.BaseBean;
import org.myproject.support.survey.SurveyAnswer;
import org.myproject.support.teacher.TeacherMBean;
import org.myproject.support.teacherhours.TeacherHoursExecutionYearMBean;
import org.myproject.support.teacherhours.TeacherHoursMBean;
import org.primefaces.model.chart.PieChartModel;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;

@Named(value = "chartMBean")
@Scope(value = WebApplicationContext.SCOPE_SESSION)
public class ChartMBean extends BaseBean {

	private static final long serialVersionUID = 1984764917382818173L;
	
	public enum SurveyTypeGroup {TEACHER, TEACHER_UC, STUDENT, STUDENT_UC}

	public enum SurveyOption {TEACHER, TEACHERHOURS, TEACHERHOURSEXECUTIONYEAR}
	

	@Inject
	private SurveyChartRepository surveyChartRepository;

	@Inject
	private SurveyAnswerScaleRepository surveyAnswerScaleRepository;

	@Inject
	private SurveyRepository surveyRepository;

	@Inject
	private UserRepository userRepository;

	private PieChartModel pieModel;
	
    private String answer;
    private Integer answerValue;
    
    private Boolean openQuestion = false;
    
    
    private int currentQuestion = 0;
    
    private boolean over  = false;
    private boolean last = false;
    private boolean noChart = true;
    
    private String title;

    private SurveyTypeGroup surveyTypeGroup;
    
    private SurveyOption surveyOption;
    
    private Survey activeSurvey;

    private List<SurveyQuestion> surveyQuestion;
    
    private String auxGroupQuestion;
    
    private LogUser user;
    
    private List<ChartMap> mapList;
    
    
    // listTeacherHours... - control buttons
    private Boolean renderedSurveyDone;

    private Boolean renderedTeacherHours;
    
    private Boolean renderedTeacherHoursExecutionYear;

    private Boolean disableButtons;

    private Boolean restartButton = true;
    
    
    public ChartMBean () {
    	
    	pieModel = new PieChartModel();
    	
        this.currentQuestion = 0;
        this.over  = false;
        this.last = false;
        
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

	
    public List<ChartMap> getMapList() {
		return mapList;
	}


	public void setMapList(List<ChartMap> mapList) {
		this.mapList = mapList;
	}


	private void createPieModel(SurveyQuestion question) {
        pieModel = new PieChartModel();
        mapList = new ArrayList<ChartMap>();
        
        List<SurveyChart> chart = 
        		this.surveyChartRepository.findBySurveyIdAndQuestionId(this.activeSurvey.getId(), question.getId());
        
        for (SurveyChart c: chart) {
        	pieModel.set(c.getSurveyAnswer().getText() + " - (" + c.getFreq() + ")", c.getFreq());
        	mapList.add(new ChartMap(c.getSurveyAnswer().getText(),  c.getFreq().toString()));
        }
        
        pieModel.setTitle(this.activeSurvey.getExecutionYear().getExecutionYear());
        pieModel.setLegendPosition("e");
        pieModel.setFill(false);
        pieModel.setShowDataLabels(true);
        pieModel.setDiameter(150);
        pieModel.setShowDataLabels(true);
        pieModel.setMouseoverHighlight(true);

    }

    
    public PieChartModel getPieModel() {
		return pieModel;
	}


	public void setPieModel(PieChartModel pieModel) {
		this.pieModel = pieModel;
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
        	
        	if (!this.openQuestion) {
        		createPieModel(this.surveyQuestion.get(this.currentQuestion));
        	}
        	
            return this.surveyQuestion.get(this.currentQuestion).getText();
        }
        else {
        	if (this.noChart) {
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

        System.out.println("Get Question   : " + this.currentQuestion + "  size " + this.surveyQuestion.size());
		
        if(this.currentQuestion < this.surveyQuestion.size()) {
        	
        	this.currentQuestion++;
          

        	if (this.currentQuestion == (this.surveyQuestion.size() - 1)) {
                this.last = true;
            }
            
            if (this.currentQuestion == this.surveyQuestion.size()) {
                this.over = true;
            }
        }
    }
    

    public void startChartCourse() {
    	
    	if (!this.restartButton) {
    		return;
    	}
    	
    	this.restartButton = false;
    	
    	this.title = "(UC - )";
    	
    	
    	System.out.println("Title :" + this.title);
    	
   	
    	this.renderedTeacherHours = true;
       	this.renderedTeacherHoursExecutionYear = false;

    	this.setSurveyTypeGroup(SurveyTypeGroup.TEACHER_UC);
    	this.setSurveyOption(SurveyOption.TEACHERHOURS);
    	
    	this.activeSurvey = this.surveyRepository.findByActiveType(SurveyTypeGroup.TEACHER_UC.toString());

    	if (this.activeSurvey != null) {
    		
    		this.surveyQuestion = this.activeSurvey.getSurveyType().getQuestionList();
	    	
        	System.out.println("Title :" + this.title);

        	if (this.surveyQuestion != null) {

	    		this.currentQuestion = 0;
		        this.over = false;
		        this.last = false;
		        this.noChart = false;
		        
		        return;
	    	}
    	}
    	
	    this.over = true;
	    this.last = true;
	    this.noChart = true;
	
    }

 
    public void startChartTeacher() {

    	this.title = "(Docente - )";
    	
    	System.out.println("Title :" + this.title);


    	this.renderedTeacherHours = false;
       	this.renderedTeacherHoursExecutionYear = true;

    	this.setSurveyTypeGroup(SurveyTypeGroup.TEACHER);
    	this.setSurveyOption(SurveyOption.TEACHERHOURSEXECUTIONYEAR);
    	
		this.activeSurvey = this.surveyRepository.findByActiveType(SurveyTypeGroup.TEACHER.toString());
		
    	if (this.activeSurvey != null) {

    		this.surveyQuestion = this.activeSurvey.getSurveyType().getQuestionList();
    		
	    	if (this.surveyQuestion != null) {
	    		
		        this.currentQuestion = 0;
		        this.over = false;
		        this.last = false;
		        this.noChart = false;
		        
		        return;
	    	}
    	}
    	
	    this.over = true;
	    this.last = true;
	    this.noChart = true;
	    
    }

    
    public String reset() {
        System.out.println("Reset");
        
        this.currentQuestion = 0;
        this.over = false;
        this.last = false;
        this.restartButton = true;
        
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
    
    public boolean isNoChart() {
		return noChart;
	}

	public void setNoChart(boolean noChart) {
		this.noChart = noChart;
	}



    public void disableButtons () {
    	this.reset();
    }
    
    
    public Integer getAnswerValue() {
		return answerValue;
	}


	public Boolean getOpenQuestion() {
		return openQuestion;
	}


	public void setOpenQuestion(Boolean openQuestion) {
		this.openQuestion = openQuestion;
	}


   public String getResourceProperty(String resource, String label) {
        Application application = FacesContext.getCurrentInstance().getApplication();
        ResourceBundle bundle = application.getResourceBundle(FacesContext.getCurrentInstance(), resource);

        return bundle.getString(label);
    }

    
}