package org.myproject.model.entities;

import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.myproject.model.utils.BaseEntity;

@Entity
@Table(name = "tbl_SURVEY_QUESTION")
@AttributeOverride(name = "id", column = @Column(name = "ID"))
public class SurveyQuestion extends BaseEntity<Long> {

	private static final long serialVersionUID = -4806043306042713447L;

    @ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "SURVEY_ID")
	private Survey survey;
	
    @OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "GROUP_ID")
	private SurveyQuestionGroup questionGroup;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SCALE_TYPE")
	private SurveyScaleType scaleType;

	@Column(name = "TEXT")
	private String text;


	public SurveyQuestion() {
		super();
	}


	public Survey getSurvey() {
		return survey;
	}


	public void setSurvey(Survey survey) {
		this.survey = survey;
	}


	public SurveyQuestionGroup getQuestionGroup() {
		return questionGroup;
	}


	public void setQuestionGroup(SurveyQuestionGroup questionGroup) {
		this.questionGroup = questionGroup;
	}


	public SurveyScaleType getScaleType() {
		return scaleType;
	}


	public void setScaleType(SurveyScaleType scaleType) {
		this.scaleType = scaleType;
	}


	public String getText() {
		return text;
	}


	public void setText(String text) {
		this.text = text;
	}



	
	
}
