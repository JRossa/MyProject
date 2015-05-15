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
@Table(name = "tbl_SURVEY_CHART")
@AttributeOverride(name = "id", column = @Column(name = "ID"))
public class SurveyChart  extends BaseEntity<Long> {

	private static final long serialVersionUID = -5006768119458947417L;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "SURVEY_ID")
	private Survey survey;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "QUESTION_ID")
	private SurveyQuestion question;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ANSWER_ID")
	private SurveyAnswerScale surveyAnswer;

	@Column(name = "ANSWER")
	private String answer;
	
	@Column(name = "FREQ")
	private Integer freq;

	
	public SurveyChart() {
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

	public SurveyAnswerScale getSurveyAnswer() {
		return surveyAnswer;
	}

	public void setSurveyAnswer(SurveyAnswerScale surveyAnswer) {
		this.surveyAnswer = surveyAnswer;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public Integer getFreq() {
		return freq;
	}

	public void setFreq(Integer freq) {
		this.freq = freq;
	}

	
}
