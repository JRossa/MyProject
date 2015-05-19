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
import org.myproject.model.entities.TeacherHoursExecutionYear;
import org.myproject.model.repositories.SurveyRepository;
import org.myproject.model.repositories.TeacherHoursExecutionYearRepository;
import org.myproject.model.repositories.TeacherHoursRepository;
import org.myproject.model.utils.BaseBean;
import org.myproject.support.survey.SurveyMBean;
import org.myproject.support.survey.SurveyMBean.SurveyTypeGroup;
import org.myproject.support.teacher.TeacherMBean;
import org.primefaces.event.SelectEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;

@Scope(value = WebApplicationContext.SCOPE_SESSION)
@Named(value = "surveyTeacherHoursExecutionYearMBean")
public class SurveyTeacherHoursExecutionYearMBean extends BaseBean {

	private static final long serialVersionUID = -712004714005574455L;

	private static final Logger logger = Logger.getLogger(SurveyTeacherHoursExecutionYearMBean.class);

    @Inject
    private TeacherHoursExecutionYearRepository teacherHoursExecutionYearRepository;

	@Inject
	private SurveyRepository surveyRepository;

    private List<TeacherHoursExecutionYear> surveyTeacherHoursExecutionYear;

    private TeacherHoursExecutionYear selectedSurveyTeacherHoursExecutionYear;

    private Long id;

    private Survey activeSurvey;
    
    private String selectedExecutionYear;
    
    
    // listSurveyTeacher - control buttons
    private Boolean disableButtons;

    private Boolean renderedEMail;

    private Boolean renderedSurvey;

    private Boolean reloadTeacher = true;

    
    public SurveyTeacherHoursExecutionYearMBean() {
		super();
		
	}


	public void onLoadSurveyTeacherHoursExecutionYear(String executionYear) {
        System.out.println("onLoadSurveyTeacherHoursExecutionYear (SurveyTeacherHoursExecutionYearMBean) :  ");
        
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
        	this.surveyTeacherHoursExecutionYear = 
                   this.teacherHoursExecutionYearRepository.findByTeacherSurveyNOTDoneInExecutionYear(this.activeSurvey.getId(), 
            																         executionYear.replace("_", "/"));
        } else {
        	this.surveyTeacherHoursExecutionYear = new ArrayList<TeacherHoursExecutionYear>();
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

    
	public Survey getActiveSurvey() {
		return activeSurvey;
	}


	public void setActiveSurvey(Survey activeSurvey) {
		this.activeSurvey = activeSurvey;
	}


	public List<TeacherHoursExecutionYear> getSurveyTeacherHoursExecutionYear() {
		return surveyTeacherHoursExecutionYear;
	}


	public void setSurveyTeacherHoursExecutionYear(List<TeacherHoursExecutionYear> surveyTeacherHoursExecutionYear) {
		this.surveyTeacherHoursExecutionYear = surveyTeacherHoursExecutionYear;
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


	public TeacherHoursExecutionYear getSelectedSurveyTeacherHoursExecutionYear() {
		return selectedSurveyTeacherHoursExecutionYear;
	}


	public void setSelectedSurveyTeacherHoursExecutionYear(TeacherHoursExecutionYear selectedSurveyTeacherHoursExecutionYear) {
		this.selectedSurveyTeacherHoursExecutionYear = selectedSurveyTeacherHoursExecutionYear;
	}


	public void delete() {
        if (this.selectedSurveyTeacherHoursExecutionYear != null) {
            this.teacherHoursExecutionYearRepository.delete(this.selectedSurveyTeacherHoursExecutionYear.getId());
        }
    }

    public void selectSurveyTeacherHoursExecutionYear(SelectEvent ev) {
        try {
            if (ev.getObject() != null) {
                this.selectedSurveyTeacherHoursExecutionYear = (TeacherHoursExecutionYear) ev.getObject();
                System.out.println("Passou");
            } else {
                this.selectedSurveyTeacherHoursExecutionYear = null;
            }
        } catch (Exception e) {
            this.selectedSurveyTeacherHoursExecutionYear = null;

            logger.error(e.getMessage(), e);
        }
    }

    public void unselectSurveyTeacherHoursExecutionYear() {
        this.selectedSurveyTeacherHoursExecutionYear = null;
    }

    public TeacherHoursExecutionYear getSelectedTeacherHoursExecutionYear() {
        return selectedSurveyTeacherHoursExecutionYear;
    }


    public void disableButtons () {
    	
    }

}
