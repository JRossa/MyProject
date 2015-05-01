package org.myproject.model.entities;


import java.util.Date;

import org.myproject.model.utils.BaseEntity;

public class Survey  extends BaseEntity<Long> {

	private static final long serialVersionUID = 5357405129398754161L;

	public enum SurveyType {TEACHER, TEACHER_UC, STUDENT, STUDENT_UC}

	private SurveyType sType;
	
	private String description;
	
	private Date surveyDate;
	
	private Date endDate;

	
	
	public Survey() {
		super();
	}

	public SurveyType getsType() {
		return sType;
	}

	public void setsType(SurveyType sType) {
		this.sType = sType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getSurveyDate() {
		return surveyDate;
	}

	public void setSurveyDate(Date surveyDate) {
		this.surveyDate = surveyDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	
	
}
