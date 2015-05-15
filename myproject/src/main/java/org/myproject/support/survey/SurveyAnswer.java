package org.myproject.support.survey;


public class SurveyAnswer {

	private Long survey;
	
	private Integer index;
	
	private Long question;

	private Long course;

	private Long answer;
	
	private String value;
	

	
	
	public SurveyAnswer(Long survey, Integer index, Long question, Long answer, String value, Long course) {
		
		this.survey = survey;
		this.index = index;
		this.question = question;
		this.answer = answer;
		this.value = value;
		this.course = course;
	}

	
	public Long getSurvey() {
		return survey;
	}

	public void setSurvey(Long survey) {
		this.survey = survey;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public Long getQuestion() {
		return question;
	}

	public void setQuestion(Long question) {
		this.question = question;
	}

	public Long getAnswer() {
		return answer;
	}

	public void setAnswer(Long answer) {
		this.answer = answer;
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
