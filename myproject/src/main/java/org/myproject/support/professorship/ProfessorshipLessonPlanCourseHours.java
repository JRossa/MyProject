package org.myproject.support.professorship;

import java.util.Date;

import org.myproject.model.entities.Course;
import org.myproject.model.entities.Teacher;
import org.myproject.model.utils.BaseEntity;



public class ProfessorshipLessonPlanCourseHours extends BaseEntity<Long> {

	private static final long serialVersionUID = 672044499845497410L;

	private Teacher teacher;
	
	private Course course;

    private Date startDate;
    
    private Date endDate;

    private Integer hours;

    
    
	public ProfessorshipLessonPlanCourseHours() {
		super();
	}
	

	public ProfessorshipLessonPlanCourseHours(Long id, Teacher teacher, Course course,
			Date startDate, Date endDate, Integer hours) {
		super();

		super.setId(id);

		this.teacher = teacher;
		this.course = course;
		this.startDate = startDate;
		this.endDate = endDate;
		this.hours = hours;
	}

	public ProfessorshipLessonPlanCourseHours(Teacher teacher, Course course,
			                           Date startDate, Date endDate, Integer hours) {
		super();
		
		this.teacher = teacher;
		this.course = course;
		this.startDate = startDate;
		this.endDate = endDate;
		this.hours = hours;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Integer getHours() {
		return hours;
	}

	public void setHours(Integer hours) {
		this.hours = hours;
	}

    // Team - add to Index
    
    
    
}
