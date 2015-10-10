package org.myproject.support.teacherhours;


import java.util.ArrayList;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.myproject.model.entities.Teacher;
import org.myproject.model.entities.TeacherHours;
import org.myproject.model.entities.TeacherHoursExecutionYear;
import org.myproject.model.repositories.TeacherHoursExecutionYearRepository;
import org.myproject.model.utils.BaseBean;
import org.myproject.support.teacher.TeacherMBean;
import org.primefaces.event.SelectEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;

@Scope(value = WebApplicationContext.SCOPE_SESSION)
@Named(value = "teacherHoursExecutionYearMBean")
public class TeacherHoursExecutionYearMBean extends BaseBean {

    private static final long serialVersionUID = 3133883459814058016L;

    private static final Logger logger = Logger.getLogger(Teacher.class);

    @Inject
    private TeacherHoursExecutionYearRepository teacherHoursExecutionYearRepository;

    private List<TeacherHoursExecutionYear> teacherHoursExecutionYear;

    private TeacherHoursExecutionYear selectedTeacherHoursExecutionYear;

    private Long id;

    private String selectedExecutionYear;
    
    
    // listTeacher - control buttons
    private Boolean disableButtons;

    private Boolean renderedDelete;

    private Boolean renderedUpdate;

    private Boolean renderedSurvey;


    
    public TeacherHoursExecutionYearMBean() {
		super();
		
	}


	public void onLoadTeacherHours(String executionYear) {
        System.out.println("onLoadTeacherHours  (TeacherHoursExecutionYear)  :  " + executionYear);

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

//        	this.teacherHoursExecutionYear = this.teacherHoursExecutionYearRepository.findAll();
        this.teacherHoursExecutionYear = 
        		this.teacherHoursExecutionYearRepository.findByExecutionYear(executionYear.replace("_", "/"));
        	
    }


    public void setRenderedListTeacherButtons (String rolename) {
    	System.out.println("setRenderedListTeacherButtons (TeacherHoursExecutionYear)  :  " + rolename);

/*    	
    	try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
*/
    	// After delete JSESSIONID cookie it comes null
    	if (rolename == null)
    		return;
    	
        if (rolename.equals("ROLE_ADMIN")) {
            this.disableButtons = true;
            this.renderedSurvey = true;
            this.renderedUpdate = true;
            this.renderedDelete = true;
        } else if (rolename.equals("ROLE_USER_T")) {
            this.disableButtons = true;
            this.renderedSurvey = true;
            this.renderedUpdate = false;
            this.renderedDelete = false;
        }
 
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    
	public String getSelectedExecutionYear() {
		return selectedExecutionYear;
	}


	public void setSelectedExecutionYear(String selectedExecutionYear) {
		this.selectedExecutionYear = selectedExecutionYear;
	}


	public void setSelectedTeacherHoursExecutionYear(
			TeacherHoursExecutionYear selectedTeacherHoursExecutionYear) {
		this.selectedTeacherHoursExecutionYear = selectedTeacherHoursExecutionYear;
	}


	public List<TeacherHoursExecutionYear> getTeacherHoursExecutionYear() {
		return teacherHoursExecutionYear;
	}


	public void setTeacherHoursExecutionYear(List<TeacherHoursExecutionYear> teacherHoursExecutionYear) {
		this.teacherHoursExecutionYear = teacherHoursExecutionYear;
	}


	public Boolean getDisableButtons() {
		return disableButtons;
	}


	public void setDisableButtons(Boolean disableButtons) {
		this.disableButtons = disableButtons;
	}


	public Boolean getRenderedDelete() {
		return renderedDelete;
	}


	public void setRenderedDelete(Boolean renderedDelete) {
		this.renderedDelete = renderedDelete;
	}


	public Boolean getRenderedUpdate() {
		return renderedUpdate;
	}


	public void setRenderedUpdate(Boolean renderedUpdate) {
		this.renderedUpdate = renderedUpdate;
	}


	public Boolean getRenderedSurvey() {
		return renderedSurvey;
	}


	public void setRenderedSurvey(Boolean renderedSurvey) {
		this.renderedSurvey = renderedSurvey;
	}


	public void setSelectedTeacherHours(TeacherHoursExecutionYear selectedTeacherHoursExecutionYear) {
		this.selectedTeacherHoursExecutionYear = selectedTeacherHoursExecutionYear;
	}


	public void delete() {
        if (this.selectedTeacherHoursExecutionYear != null) {
            this.teacherHoursExecutionYearRepository.delete(this.selectedTeacherHoursExecutionYear.getId());
        }
    }

    public void selectTeacherHoursExecutionYear(SelectEvent ev) {
        try {
            if (ev.getObject() != null) {
                this.selectedTeacherHoursExecutionYear = (TeacherHoursExecutionYear) ev.getObject();
                System.out.println("Passou");
            } else {
                this.selectedTeacherHoursExecutionYear = null;
            }
        } catch (Exception e) {
            this.selectedTeacherHoursExecutionYear = null;

            logger.error(e.getMessage(), e);
        }
    }

    
    public void unselectTeacherHoursExecutionYear() {
        this.selectedTeacherHoursExecutionYear = null;
    }

    public TeacherHoursExecutionYear getSelectedTeacherHoursExecutionYear() {
        return selectedTeacherHoursExecutionYear;
    }

    
    public void save () {
    	
    	this.teacherHoursExecutionYearRepository.saveAndFlush(this.selectedTeacherHoursExecutionYear);
    }
    
    
    public void disableButtons () {
    	
    }

}
