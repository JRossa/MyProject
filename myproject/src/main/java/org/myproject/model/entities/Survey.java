package org.myproject.model.entities;


import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.myproject.model.utils.BaseEntity;

@Entity
@Table(name = "tbl_SURVEY")
@AttributeOverride(name = "id", column = @Column(name = "ID"))
public class Survey  extends BaseEntity<Long> {

	private static final long serialVersionUID = 5357405129398754161L;

	@Column(name = "ACTIVE")
	private Boolean active;

    @OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "SURVEY_TYPE")
	private SurveyType surveyType;
	
	@Column(name = "DESCRIPTION")
	private String description;
	
	@Column(name = "DATE")
	private Date surveyDate;
	
	@Column(name = "END_DATE")
	private Date endDate;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "EXECUTION_YEAR")
	private ExecutionYear executionYear;
	
 
    
	public Survey() {
		super();
	}

	
	
	
	public Boolean getActive() {
		return active;
	}


	public void setActive(Boolean active) {
		this.active = active;
	}


	public SurveyType getSurveyType() {
		return surveyType;
	}


	public void setSurveyType(SurveyType surveyType) {
		this.surveyType = surveyType;
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


	public ExecutionYear getExecutionYear() {
		return executionYear;
	}


	public void setExecutionYear(ExecutionYear executionYear) {
		this.executionYear = executionYear;
	}

}
