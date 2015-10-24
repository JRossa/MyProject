package org.myproject.model.entities;



import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.myproject.model.utils.BaseEntity;

/*
@Entity
@Table(name = "tbl_TEACHER_LESSON_PLAN_TOT_HOURS")
@AttributeOverride(name = "id", column = @Column(name = "ID"))
public class TeacherLessonPlanHours extends BaseEntity<Long> {

    private static final long serialVersionUID = -3523046839968854829L;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TEACHER_ID")
    private Teacher teacher;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "COURSE_ID")
    private Course course;

    //  Only store date    
//  @Temporal(TemporalType.DATE)
    @Column(name = "START_DATE")
    private Date startDate;

//  @Temporal(TemporalType.DATE)
    @Column(name = "END_DATE")
    private Date endDate;

    @Column(name = "HOURS")
    private Integer  hours;

    
    
	public TeacherLessonPlanHours() {
		super();
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

}
*/

@Entity
@Table(name = "tbl_TEACHER_LESSON_PLAN_HOURS")
@AttributeOverride(name = "id", column = @Column(name = "TEACHER_ID"))
public class TeacherLessonPlanHours extends BaseEntity<Long> {

    private static final long serialVersionUID = -3523046839968854829L;

    @Column(name = "COURSE_ID")
    private Long course;

    //  Only store date    
//  @Temporal(TemporalType.DATE)
    @Column(name = "START_DATE")
    private Date startDate;

//  @Temporal(TemporalType.DATE)
    @Column(name = "END_DATE")
    private Date endDate;

    @Column(name = "HOURS")
    private Integer  hours;

    
    
	public TeacherLessonPlanHours() {
		super();
	}


	public Long getCourse() {
		return course;
	}

	public void setCourse(Long course) {
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

}

