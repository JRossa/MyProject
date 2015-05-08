package org.myproject.model.entities;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.myproject.model.utils.BaseEntity;

@Entity
@Table(name = "tbl_SURVEY_TEACHER")
@AttributeOverride(name = "id", column = @Column(name = "ID"))
public class SurveyTeacher  extends BaseEntity<Long> {

	private static final long serialVersionUID = 43779914397023823L;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "SURVEY_ID")
	private Survey survey;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "TEACHER_ID")
	private Teacher teacher;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "COURSE_ID")
	private Course course;

	@Column(name = "DATE")
	private Date date;
	
	
	public SurveyTeacher() {
		super();
	}

	public Survey getSurvey() {
		return survey;
	}

	public void setSurvey(Survey survey) {
		this.survey = survey;
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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}


	
}
