package org.myproject.model.entities;

import org.myproject.model.utils.BaseEntity;

public class SurveyQuestionScale extends BaseEntity<Long> {

	private static final long serialVersionUID = -7442815737093444569L;

	private Integer scale;
	
	private Integer value;
	
	private String text;

	
	
	public SurveyQuestionScale(Long id, Integer scale, Integer value, String text) {
		super();
		
		this.setId(id);
		this.scale = scale;
		this.value = value;
		this.text = text;
	}


	public Integer getScale() {
		return scale;
	}



	public void setScale(Integer scale) {
		this.scale = scale;
	}



	public Integer getValue() {
		return value;
	}



	public void setValue(Integer value) {
		this.value = value;
	}



	public String getText() {
		return text;
	}



	public void setText(String text) {
		this.text = text;
	}
	
	
	
	
}
