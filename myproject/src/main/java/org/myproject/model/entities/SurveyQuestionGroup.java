package org.myproject.model.entities;

import org.myproject.model.utils.BaseEntity;

public class SurveyQuestionGroup extends BaseEntity<Long> {

	private static final long serialVersionUID = 9044215406135046286L;
	
	private String type;
	
	private String name;

	public SurveyQuestionGroup() {
		super();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	

}
