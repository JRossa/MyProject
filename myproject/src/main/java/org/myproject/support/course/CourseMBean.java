package org.myproject.support.course;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.myproject.model.entities.Course;
import org.myproject.model.entities.Teacher;
import org.myproject.model.repositories.CourseRepository;
import org.myproject.model.utils.BaseBean;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;

@Scope(value = WebApplicationContext.SCOPE_SESSION)
@Named(value = "courseMBean")
public class CourseMBean extends BaseBean {

    private static final long serialVersionUID = 7979033414618829290L;

    private static final Logger logger = Logger.getLogger(CourseMBean.class);

    @Inject
    private CourseRepository courseRepository;
   
    private List<SelectItem> selectOneItemsCourse;
    
    private List<Course> courses;

    
    public void onLoad() {
        this.courses = this.courseRepository.findAll();
    }
    
    
    public List<SelectItem> getSelectOneItemsCourse() {
        this.selectOneItemsCourse = new ArrayList<SelectItem>();

        List <Course> selectedCourses = this.courseRepository.findAll();
        
        for (Course course : selectedCourses) {
//            System.out.println("Course :" + course.getId() + "  " + course.getName());
            
            SelectItem selectItem = new SelectItem(course.getId(), course.getName());
            this.selectOneItemsCourse.add(selectItem);
        }

        return this.selectOneItemsCourse;
    }

    
    public List<SelectItem> getSelectOneItemsPresentCourse() {
        this.selectOneItemsCourse = new ArrayList<SelectItem>();

        List <Course> selectedCourses = this.courseRepository.findAllPresentCourses();
        
        for (Course course : selectedCourses) {
//            System.out.println("Course :" + course.getId() + "  " + course.getName());
            
            SelectItem selectItem = new SelectItem(course.getId(), course.getCode() + " - " + course.getName());
            this.selectOneItemsCourse.add(selectItem);
        }

        return this.selectOneItemsCourse;
    }

}
