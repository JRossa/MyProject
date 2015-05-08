package org.myproject.model.entities;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.myproject.model.utils.BaseEntity;

@Entity
@Table(name = "tbl_SURVEY_TEACHER_ANSWER")
@AttributeOverride(name = "id", column = @Column(name = "ID"))
public class SurveyTeacherAnswer  extends BaseEntity<Long> {

	private static final long serialVersionUID = -4095720467314693256L;

	@Column(name = "STAMP")
	private String stamp;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "SURVEY_ID")
	private Survey survey;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "QUESTION_ID")
	private SurveyQuestion question;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "COURSE_ID")
	private Course course;

	@Column(name = "ANSWER")
	private String value;

	
	
	public SurveyTeacherAnswer() {
		super();
	}

	public Survey getSurvey() {
		return survey;
	}

	public void setSurvey(Survey survey) {
		this.survey = survey;
	}

	public SurveyQuestion getQuestion() {
		return question;
	}

	public void setQuestion(SurveyQuestion question) {
		this.question = question;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public String getStamp() {
		return stamp;
	}

	public void setStamp(String stamp) {
		this.stamp = stamp;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	
	
}
