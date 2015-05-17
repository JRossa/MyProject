package org.myproject.support.survey;


import java.util.ArrayList;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.myproject.model.entities.Survey;
import org.myproject.model.entities.Teacher;
import org.myproject.model.entities.TeacherHours;
import org.myproject.model.repositories.SurveyRepository;
import org.myproject.model.repositories.TeacherHoursRepository;
import org.myproject.model.utils.BaseBean;
import org.myproject.support.survey.SurveyMBean;
import org.myproject.support.survey.SurveyMBean.SurveyTypeGroup;
import org.myproject.support.teacher.TeacherMBean;
import org.primefaces.event.SelectEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;

@Scope(value = WebApplicationContext.SCOPE_SESSION)
@Named(value = "surveyTeacherHoursMBean")
public class SurveyTeacherHoursMBean extends BaseBean {

	private static final long serialVersionUID = -5543562700693434487L;

	private static final Logger logger = Logger.getLogger(SurveyTeacherHoursMBean.class);

    @Inject
    private TeacherHoursRepository teacherHoursRepository;

	@Inject
	private SurveyRepository surveyRepository;

    private List<TeacherHours> surveyTeacherHours;

    private TeacherHours selectedSurveyTeacherHours;

    private Long id;

    private Survey activeSurvey;
    
    private String selectedExecutionYear;
    
    
    // listSurveyTeacher - control buttons
    private Boolean disableButtons;

    private Boolean renderedEMail;

    private Boolean renderedSurvey;

    private Boolean reloadTeacher = true;

    
    public SurveyTeacherHoursMBean() {
		super();
		
	}


	public void onLoadSurveyTeacherHours(String executionYear) {
        System.out.println("onLoadSurveyTeacherHours (SurveyTeacherHoursMBean) :  ");
        
        if (this.selectedExecutionYear != null && this.selectedExecutionYear.length() == 9) {
        	executionYear = this.selectedExecutionYear;
        } else {
	        executionYear = executionYear.replace("_", "/");
        	this.selectedExecutionYear = executionYear;
        }
        	
        System.out.println("onLoad  : " + executionYear);
       
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext(); 
        String rolename = (String) context.getSessionMap().get("rolename");
            
        setRenderedListTeacherButtons (rolename);
            
        this.activeSurvey = this.surveyRepository.findByActiveType(SurveyTypeGroup.TEACHER_UC.toString());
        
        if (this.activeSurvey != null) {
        	this.surveyTeacherHours = 
                   this.teacherHoursRepository.findByCourseSurveyNOTDoneInExecutionYear(this.activeSurvey.getId(), 
            																         executionYear.replace("_", "/"));
        } else {
        	this.surveyTeacherHours = new ArrayList<TeacherHours>();
        }
        	
    }


    public void setRenderedListTeacherButtons (String rolename) {
        
        if (rolename.equals("ROLE_ADMIN")) {
            this.disableButtons = true;
            this.renderedSurvey = true;
            this.renderedEMail = true;
    		this.reloadTeacher = true;

        } else if (rolename.equals("ROLE_USER_T")) {
            this.disableButtons = true;
            this.renderedSurvey = true;
            this.renderedEMail = false;
    		this.reloadTeacher = true;

        }
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    
	public List<TeacherHours> getSurveyTeacherHours() {
		return surveyTeacherHours;
	}


	public void setSurveyTeacherHours(List<TeacherHours> surveyTeacherHours) {
		this.surveyTeacherHours = surveyTeacherHours;
	}


	public Boolean getDisableButtons() {
		return disableButtons;
	}


	public void setDisableButtons(Boolean disableButtons) {
		this.disableButtons = disableButtons;
	}


	public Boolean getRenderedEMail() {
		return renderedEMail;
	}


	public void setRenderedEMail(Boolean renderedEMail) {
		this.renderedEMail = renderedEMail;
	}


	public Boolean getReloadTeacher() {
		return reloadTeacher;
	}


	public void setReloadTeacher(Boolean reloadTeacher) {
		this.reloadTeacher = reloadTeacher;
	}


	public Boolean getRenderedSurvey() {
		return renderedSurvey;
	}


	public void setRenderedSurvey(Boolean renderedSurvey) {
		this.renderedSurvey = renderedSurvey;
	}


	public String getSelectedExecutionYear() {
		return selectedExecutionYear;
	}


	public void setSelectedExecutionYear(String selectedExecutionYear) {
		this.selectedExecutionYear = selectedExecutionYear;
	}


	public TeacherHours getSelectedSurveyTeacherHours() {
		return selectedSurveyTeacherHours;
	}


	public void setSelectedSurveyTeacherHours(TeacherHours selectedSurveyTeacherHours) {
		this.selectedSurveyTeacherHours = selectedSurveyTeacherHours;
	}


	public void delete() {
        if (this.selectedSurveyTeacherHours != null) {
            this.teacherHoursRepository.delete(this.selectedSurveyTeacherHours.getId());
        }
    }

    public void selectSurveyTeacherHours(SelectEvent ev) {
        try {
            if (ev.getObject() != null) {
                this.selectedSurveyTeacherHours = (TeacherHours) ev.getObject();
                System.out.println("Passou");
            } else {
                this.selectedSurveyTeacherHours = null;
            }
        } catch (Exception e) {
            this.selectedSurveyTeacherHours = null;

            logger.error(e.getMessage(), e);
        }
    }

    public void unselectSurveyTeacherHours() {
        this.selectedSurveyTeacherHours = null;
    }

    public TeacherHours getSelectedTeacherHours() {
        return selectedSurveyTeacherHours;
    }


    public void disableButtons () {
    	
    }

}
