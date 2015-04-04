package org.myproject.support.teacher;

import java.util.ResourceBundle;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.myproject.model.entities.Teacher;
import org.myproject.model.repositories.TeacherRepository;
import org.myproject.model.utils.BaseBean;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;

@Scope(value = WebApplicationContext.SCOPE_REQUEST)
@Named(value = "teacherAddEditMBean")
public class TeacherAddEditMBean extends BaseBean {

	private static final long serialVersionUID = -1837094273010208359L;
	
	@Inject
	private TeacherRepository teacherRepository;
	
	@Inject
	private TeacherMBean mbTeacherBean;
	
	private Teacher teacher;

	private String title;
	
	private String strSearch;
	
	
        public TeacherAddEditMBean () {
            this.teacher = new Teacher();
        }
    
	
	public String getStrSearch() {
		return strSearch;
	}

	public void setStrSearch(String strSearch) {
		this.strSearch = strSearch;
	}

	public Teacher getTeacher() {
		return this.teacher;
	}

	public void setTeachers(Teacher teacher) {
		this.teacher = teacher;
	}
	
	public 	void add() {
		this.title = getResourceProperty("labels", "teacher_add");
	}
	
	public 	void update() {
		this.teacher = mbTeacherBean.getSelectedTeacher();
		this.title = getResourceProperty("labels", "teacher_update");
	}

	public void cancel () {
		this.mbTeacherBean.unselectTeacher();
	}
	
	public void search () {
		this.mbTeacherBean.search(this.strSearch);
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public void save() {
	    if (this.teacher != null) {
		if (this.teacher.getId() == null) {
		// Add
		this.teacherRepository.save(this.teacher);
		} else {
		// Update
		this.teacherRepository.save(this.teacher);			
		}
	    }
	}

    public String getResourceProperty(String resource, String label) {
        Application application = FacesContext.getCurrentInstance().getApplication();
        ResourceBundle bundle = application.getResourceBundle(FacesContext.getCurrentInstance(), resource);

        return bundle.getString(label);
    }

}
