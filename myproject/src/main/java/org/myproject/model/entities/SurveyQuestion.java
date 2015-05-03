package org.myproject.model.entities;

import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.myproject.model.utils.BaseEntity;

@Entity
@Table(name = "tbl_SURVEY_QUESTION")
@AttributeOverride(name = "id", column = @Column(name = "ID"))
public class SurveyQuestion extends BaseEntity<Long> {

	private static final long serialVersionUID = -4806043306042713447L;

	@Column(name = "SURVEY_ID")
	private Long surveyId;
	
	@Column(name = "GROUP_ID")
	private Long groupId;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SCALE_TYPE")
	private SurveyScaleType scaleType;

	@Column(name = "TEXT")
	private String text;


	public SurveyQuestion() {
		super();
	}


	public Long getSurveyId() {
		return surveyId;
	}


	public void setSurveyId(Long surveyId) {
		this.surveyId = surveyId;
	}


	public Long getGroupId() {
		return groupId;
	}


	public void setGroupId(Long groupId) {
		this.groupId = groupId;
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
