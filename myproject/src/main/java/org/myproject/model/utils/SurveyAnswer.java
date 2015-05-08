package org.myproject.model.utils;


public class SurveyAnswer {

	private Long survey;
	
	private Long question;

	private Long course;

	private String value;
	

	
	
	public SurveyAnswer(Long survey, Long question, String value, Long course) {
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
