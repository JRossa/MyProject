package org.myproject.support.professorship;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.myproject.model.entities.Professorship;
import org.myproject.model.entities.Teacher;
import org.myproject.model.repositories.ProfessorshipRepository;
import org.myproject.model.repositories.TeacherRepository;
import org.myproject.model.utils.BaseBean;
import org.myproject.model.utils.ProfessorshipCourseHours;
import org.myproject.support.teacher.TeacherMBean;
import org.primefaces.event.SelectEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;

@Scope(value = WebApplicationContext.SCOPE_SESSION)
@Named(value = "professorshipMBean")
public class ProfessorshipMBean extends BaseBean {

    private static final long   serialVersionUID = 3133883459814058016L;

    private static final Logger logger           = Logger.getLogger(ProfessorshipMBean.class);

    @Inject
    private ProfessorshipRepository professorshipRepository;

    @Inject
    private TeacherMBean mbTeacherMBean;
    
    private List<Professorship> professorships;
    
    private Professorship selectProfessorship;

    private List<ProfessorshipCourseHours> professorshipCourseHours;

    private Long Id;
    
    private String selectedExecutionYear;
    
    
    public ProfessorshipMBean() {
		super();
	}

   
    
    public Long getId() {
		return Id;
	}


	public void setId(Long id) {
		Id = id;
	}


    public String getSelectedExecutionYear() {
		return selectedExecutionYear;
	}


	public void setSelectedExecutionYear(String selectedExecutionYear) {
		this.selectedExecutionYear = selectedExecutionYear;
	}


	public void selectProfessorship(SelectEvent ev) {
        try {
            if (ev.getObject() != null) {
                this.selectProfessorship = (Professorship) ev.getObject();
                System.out.println("Passou");
            } else {
                this.selectProfessorship = null;
            }
        } catch (Exception e) {
            this.selectProfessorship = null;

            logger.error(e.getMessage(), e);
        }
    }

    public void unselectProfessorship() {
        this.selectProfessorship = null;
    }


	public Professorship getSelectProfessorship() {
		return selectProfessorship;
	}



	public void setSelectProfessorship(Professorship selectProfessorship) {
		this.selectProfessorship = selectProfessorship;
	}



	public List<Professorship> getProfessorships() {
        return professorships;
    }

    
    public void setProfessorships(List<Professorship> professorships) {
        this.professorships = professorships;
    }

    
    public List<ProfessorshipCourseHours> getProfessorshipCourseHours() {
        return professorshipCourseHours;
    }
   
    
    public void setProfessorshipCourseHours(List<ProfessorshipCourseHours> professorshipCourseHours) {
        this.professorshipCourseHours = professorshipCourseHours;
    }

	public void init() {
        System.out.println("A new backing bean has been created");
        this.professorships = new ArrayList<Professorship>();
        this.selectedExecutionYear = this.mbTeacherMBean.getSelectedExecutionYear();
    }


    public void onLoadProfessorshipCourseHours() {
        System.out.println("onLoadProfessorshipCourseHours");

        this.professorshipCourseHours = new ArrayList<ProfessorshipCourseHours>();
        this.selectedExecutionYear = this.mbTeacherMBean.getSelectedExecutionYear();

        if (this.mbTeacherMBean.getSelectedTeacher() != null) {
            System.out.println("Select Teacher Id :" + this.mbTeacherMBean.getSelectedTeacher().getId());

            this.selectedExecutionYear = this.mbTeacherMBean.getSelectedExecutionYear();
            
            List<Object[]> course = 
            		this.professorshipRepository.findCoursesByUserAndExecutionYear(
            				                                 this.mbTeacherMBean.getSelectedTeacher().getId(),
            				                                 this.selectedExecutionYear);

            for (Object[] c : course) {
                System.out.println("Code    :  " + c[1].toString());
                System.out.println("Name    :  " + c[2].toString());

                this.professorshipCourseHours.add(new ProfessorshipCourseHours(c[1].toString(), c[2].toString(), 0.0));
            }

            // Verificação
            for (ProfessorshipCourseHours ch : professorshipCourseHours) {
                System.out.println("Final Code    :  " + ch.getCode());
            }
        }
    }

    
    public void onLoadProfessorship() {
        System.out.println("onLoadProfessorship");

        if (this.mbTeacherMBean.getSelectedTeacher() != null) {
            System.out.println("Select Teacher Id :" + this.mbTeacherMBean.getSelectedTeacher().getId());

            this.selectedExecutionYear = this.mbTeacherMBean.getSelectedExecutionYear();
            if (this.selectedExecutionYear != null && this.selectedExecutionYear.length() == 9) {
            	this.professorships =  
            		this.professorshipRepository.findByTeacherIdAndExecutionYear(
            				   this.mbTeacherMBean.getSelectedTeacher().getId(), this.selectedExecutionYear);
            } else {
                this.professorships = this.mbTeacherMBean.getSelectedTeacher().getProfessorship();
            }
        }
    }

    


    
    


}
