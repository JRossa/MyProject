package org.myproject.model.entities;

import java.util.List;

import org.myproject.model.utils.BaseEntity;

public class SurveyQuestion extends BaseEntity<Long> {

	private static final long serialVersionUID = -4806043306042713447L;

	
	private Survey survey;
	
	private SurveyQuestionGroup group;
	
	private List<SurveyQuestionScale> scale;
	
	private String text;

	
	


	public SurveyQuestion(Long id, Survey survey, SurveyQuestionGroup group,
			                                      List<SurveyQuestionScale> scale, String text) {
		super();
		
		this.setId(id);
		this.survey = survey;
		this.group = group;
		this.scale = scale;
		this.text = text;
	}



	public Survey getSurvey() {
		return survey;
	}



	public void setSurvey(Survey survey) {
		this.survey = survey;
	}



	public SurveyQuestionGroup getGroup() {
		return group;
	}



	public void setGroup(SurveyQuestionGroup group) {
		this.group = group;
	}



	public List<SurveyQuestionScale> getScale() {
		return scale;
	}



	public void setScale(List<SurveyQuestionScale> scale) {
		this.scale = scale;
	}



	public String getText() {
		return text;
	}



	public void setText(String text) {
		this.text = text;
	}
	
	
	
}
