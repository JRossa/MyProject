package org.myproject.model.entities;

import org.myproject.model.utils.BaseEntity;

public class SurveyAnswer  extends BaseEntity<Long> {

	private static final long serialVersionUID = -5070534644569347219L;
	

	private Long survey;
	
	private Long question;
	
	private String value;
	
	private Long course;

	
	
	public SurveyAnswer(Long id, Long survey, Long question, String value, Long course) {
		super();
		
		this.setId(id);
		this.survey = survey;
		this.question = question;
		this.value = value;
		this.course = course;
	}

	
	public Long getSurvey() {
		return survey;
	}

	public void setSurvey(Long survey) {
		this.survey = survey;
	}

	public Long getQuestion() {
		return question;
	}

	public void setQuestion(Long question) {
		this.question = question;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Long getCourse() {
		return course;
	}

	public void setCourse(Long course) {
		this.course = course;
	}
	
	
	
}
