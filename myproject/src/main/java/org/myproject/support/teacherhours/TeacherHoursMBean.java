package org.myproject.support.teacherhours;


import java.util.ArrayList;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.myproject.model.entities.LogUser;
import org.myproject.model.entities.Teacher;
import org.myproject.model.entities.TeacherHours;
import org.myproject.model.repositories.TeacherHoursRepository;
import org.myproject.model.repositories.UserRepository;
import org.myproject.model.utils.BaseBean;
import org.myproject.support.survey.SurveyMBean;
import org.myproject.support.teacher.TeacherMBean;
import org.primefaces.event.SelectEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;

@Scope(value = WebApplicationContext.SCOPE_SESSION)
@Named(value = "teacherHoursMBean")
public class TeacherHoursMBean extends BaseBean {

    private static final long serialVersionUID = 3133883459814058016L;

    private static final Logger logger = Logger.getLogger(Teacher.class);

    @Inject
    private UserRepository userRepository;

    @Inject
    private TeacherMBean mbTeacherMBean;

    @Inject
    private TeacherHoursRepository teacherHoursRepository;

    private List<TeacherHours> teacherHours;

    private TeacherHours selectedTeacherHours;

    private Long id;

    private Teacher selectedTeacher;
    
    
    // listTeacher - control buttons
    private Boolean disableButtons;

    private Boolean renderedDelete;

    private Boolean renderedUpdate;

    private Boolean renderedSurvey;

    private Boolean reloadTeacher = true;

    
    public TeacherHoursMBean() {
		super();
		
	}


	public void onLoadTeacherHours() {
        System.out.println("onLoadTeacherHours (TeacherHoursMBean) :  ");

        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext(); 
        String rolename = (String) context.getSessionMap().get("rolename");

        Long userId = Long.parseLong((String) context.getSessionMap().get("userId"));

        setRenderedListTeacherButtons (rolename);

        if (this.mbTeacherMBean.getSelectedTeacher() != null) {
            System.out.println("Select Teacher Id :" + this.mbTeacherMBean.getSelectedTeacher().getId());

            this.selectedTeacher = this.mbTeacherMBean.getSelectedTeacher();
            
            LogUser user = userRepository.findOne(userId);

            if (user.getLogRole().getRolename().equals("ROLE_USER_T")) {
            	this.teacherHours = this.teacherHoursRepository.findByTeacher(user.getTeacher());
            } else {
            	this.teacherHours = 
            		this.teacherHoursRepository.findByTeacherAndExecutionYear(this.mbTeacherMBean.getSelectedTeacher().getId(),
                                                                              this.mbTeacherMBean.getSelectedExecutionYear());
            }
        } 
    }


    public void setRenderedListTeacherButtons (String rolename) {
        
        if (rolename.equals("ROLE_ADMIN")) {
            this.disableButtons = true;
            this.renderedSurvey = true;
            this.renderedUpdate = true;
            this.renderedDelete = true;
    		this.reloadTeacher = true;

        } else if (rolename.equals("ROLE_USER_T")) {
            this.disableButtons = true;
            this.renderedSurvey = true;
            this.renderedUpdate = true;
            this.renderedDelete = false;
    		this.reloadTeacher = true;

        }
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    
	public Teacher getSelectedTeacher() {
		return selectedTeacher;
	}


	public void setSelectedTeacher(Teacher selectedTeacher) {
		this.selectedTeacher = selectedTeacher;
	}


	public List<TeacherHours> getTeacherHours() {
		return teacherHours;
	}


	public void setTeacherHours(List<TeacherHours> teacherHours) {
		this.teacherHours = teacherHours;
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


	public void setSelectedTeacherHours(TeacherHours selectedTeacherHours) {
		this.selectedTeacherHours = selectedTeacherHours;
	}


	public void delete() {
        if (this.selectedTeacherHours != null) {
            this.teacherHoursRepository.delete(this.selectedTeacherHours.getId());
        }
    }

    public void selectTeacherHours(SelectEvent ev) {
        try {
            if (ev.getObject() != null) {
                this.selectedTeacherHours = (TeacherHours) ev.getObject();
                System.out.println("Passou");
            } else {
                this.selectedTeacherHours = null;
            }
        } catch (Exception e) {
            this.selectedTeacherHours = null;

            logger.error(e.getMessage(), e);
        }
    }

    public void unselectTeacherHours() {
        this.selectedTeacherHours = null;
    }

    public TeacherHours getSelectedTeacherHours() {
        return selectedTeacherHours;
    }

    
    public void save () {
    	
    	this.teacherHoursRepository.saveAndFlush(this.selectedTeacherHours);
    }
    
    
    public void disableButtons () {
    	
    }

}
