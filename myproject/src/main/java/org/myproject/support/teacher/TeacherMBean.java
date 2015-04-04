package org.myproject.support.teacher;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.myproject.model.entities.Course;
import org.myproject.model.entities.Professorship;
import org.myproject.model.entities.ProfessorshipCourseHours;
import org.myproject.model.entities.Teacher;
import org.myproject.model.entities.TeacherHours;
import org.myproject.model.repositories.ProfessorshipRepository;
import org.myproject.model.repositories.TeacherHoursRepository;
import org.myproject.model.repositories.TeacherRepository;
import org.myproject.model.utils.BaseBean;
import org.primefaces.event.SelectEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;

@Scope(value = WebApplicationContext.SCOPE_SESSION)
@Named(value = "teacherMBean")
public class TeacherMBean extends BaseBean {

    private static final long serialVersionUID = 3133883459814058016L;

    private static final Logger logger = Logger.getLogger(Teacher.class);

    @Inject
    private TeacherRepository teacherRepository;

    @Inject
    private ProfessorshipRepository professorshipRepository;

    @Inject
    private TeacherHoursRepository teacherHoursRepository;

    private List<Teacher> teachers;

    private Teacher selectedTeacher;

    private Long id;

    private Boolean bSearch = false;

    private List<SelectItem> selectOneItemsTeacher;

    private List<Professorship> professorship;

    private List<ProfessorshipCourseHours> professorshipCourseHours;

    private List<TeacherHours> teacherHours;

    
    // listTeacher - control buttons
    private Boolean renderedProfessorship;

    private Boolean renderedRead;

    private Boolean renderedUpdate;

    private Boolean renderedDelete;

    private Boolean disableButtons;
    
    
    public void onLoadTeste() {
        this.teachers = new ArrayList<Teacher>();

        teachers.add(new Teacher(1L, "Categ 1", "NIM", "Nome"));
        teachers.add(new Teacher(2L, "Categ 2", "NIM", "Nome"));
        teachers.add(new Teacher(3L, "Categ 3", "NIM", "Nome"));
        teachers.add(new Teacher(4L, "Categ 4", "NIM", "Nome"));
    }

    
    public void onLoad() {
    	
    	this.disableButtons = true;
    	 
        if (bSearch) {
            bSearch = false;
        } else {
            
//            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext(); 
//            String rolename = (String) context.getSessionMap().get("rolename");
//            
//            setRederedListTeacherButtons (rolename);

            this.teachers = this.teacherRepository.findAllListOrderByFullName();
        }
    }

    
    public void onLoad(String executionYear) {
        System.out.println("onLoad  : " + executionYear);

        executionYear = executionYear.replace("_", "/");
        System.out.println("onLoad  : " + executionYear);

        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext(); 
        String rolename = (String) context.getSessionMap().get("rolename");
        
        setRederedListTeacherButtons (rolename);

        this.teachers = this.teacherRepository.findByExecutionYear(executionYear.replace("_", "/"));
        
    }

    public void onLoadTeacherHours(String executionYear) {
        System.out.println("onLoadTeacherHours  :  " + executionYear);

        if (this.selectedTeacher != null) {
            System.out.println("Select Teacher Id :" + this.selectedTeacher.getId());

            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext(); 
            String rolename = (String) context.getSessionMap().get("rolename");
            
            setRederedListTeacherButtons (rolename);
            
            this.teacherHours = this.teacherHoursRepository.findByTeacherAndExecutionYear(this.selectedTeacher.getId(),
                    executionYear.replace("_", "/"));
        }
    }

    
    public void onLoadProfessorshipCourseHours() {
        System.out.println("onLoadProfessorshipCourseHours");

        this.professorshipCourseHours = new ArrayList<ProfessorshipCourseHours>();

        if (this.selectedTeacher != null) {
            System.out.println("Select Teacher Id :" + this.selectedTeacher.getId());

            List<Object[]> course = this.professorshipRepository.findCoursesByUserAndExecutionYear(this.selectedTeacher.getId(),
                    "2014/2015");
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

        if (this.selectedTeacher != null) {
            System.out.println("Select Teacher Id :" + this.selectedTeacher.getId());

            this.professorship = this.selectedTeacher.getProfessorship();

        }
    }

    
     public List<ProfessorshipCourseHours> getProfessorshipCourseHours() {
         return professorshipCourseHours;
     }
    
     public void setProfessorshipCourseHours(List<ProfessorshipCourseHours> professorshipCourseHours) {
         this.professorshipCourseHours = professorshipCourseHours;
     }

    
    public void setRederedListTeacherButtons (String rolename) {
        
        if (rolename.equals("ROLE_ADMIN")) {
            this.renderedProfessorship = true;
            this.renderedRead = true;
            this.renderedUpdate = true;
            this.renderedDelete = true;
        } else if (rolename.equals("ROLE_USER_T")) {
            this.renderedProfessorship = true;
            this.renderedRead = true;
            this.renderedUpdate = false;
            this.renderedDelete = false;
        }
    }
 
    
    public Boolean getRenderedProfessorship() {
        return renderedProfessorship;
    }

    public void setRenderedProfessorship(Boolean renderedProfessorship) {
        this.renderedProfessorship = renderedProfessorship;
    }

    public Boolean getRenderedRead() {
        return renderedRead;
    }

    public void setRenderedRead(Boolean renderedRead) {
        this.renderedRead = renderedRead;
    }

    public Boolean getRenderedUpdate() {
        return renderedUpdate;
    }

    public void setRenderedUpdate(Boolean renderedUpdate) {
        this.renderedUpdate = renderedUpdate;
    }

    public Boolean getRenderedDelete() {
        return renderedDelete;
    }

    public void setRenderedDelete(Boolean renderedDelete) {
        this.renderedDelete = renderedDelete;
    }

    
    public Boolean getDisableButtons() {
		return disableButtons;
	}


	public void setDisableButtons(Boolean disableButtons) {
		this.disableButtons = disableButtons;
	}


	public void search(String strName) {
        bSearch = true;
        this.teachers = null;
        this.teachers = this.teacherRepository.findByFullNameContaining(strName);
    }

    @PostConstruct
    public void init() {
        System.out.println("A new backing bean has been created");
        this.teachers = new ArrayList<Teacher>();
    }

    public List<TeacherHours> getTeacherHours() {
        return teacherHours;
    }

    public void setTeacherHours(List<TeacherHours> teacherHours) {
        this.teacherHours = teacherHours;
    }

    public List<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<Teacher> teachers) {
        this.teachers = teachers;
    }

    public List<Professorship> getProfessorship() {
        return professorship;
    }

    public void setProfessorship(List<Professorship> professorship) {
        this.professorship = professorship;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void delete() {
        if (this.selectedTeacher != null) {
            this.teacherRepository.delete(this.selectedTeacher.getId());
        }
    }

    public void selectTeacher(SelectEvent ev) {
        try {
            if (ev.getObject() != null) {
                this.selectedTeacher = (Teacher) ev.getObject();
                System.out.println("Passou");
            } else {
                this.selectedTeacher = null;
            }
        } catch (Exception e) {
            this.selectedTeacher = null;

            logger.error(e.getMessage(), e);
        }
    }

    public void unselectTeacher() {
        this.selectedTeacher = null;
    }

    public Teacher getSelectedTeacher() {
        return selectedTeacher;
    }

    public void setSelectedTeacher(Teacher selectedTeacher) {
        this.selectedTeacher = selectedTeacher;
    }

    
    public List<SelectItem> getSelectOneItemsTeacher() {
        this.selectOneItemsTeacher = new ArrayList<SelectItem>();
        
        List<Teacher> selectTeachers = this.teacherRepository.findAllOrderByFullName();

        for (Teacher teacher : selectTeachers) {
            SelectItem selectItem = new SelectItem(teacher.getId(), teacher.getFullName());
            this.selectOneItemsTeacher.add(selectItem);
        }

        return selectOneItemsTeacher;
    }

}
