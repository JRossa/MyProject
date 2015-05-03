package org.myproject.model.entities;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.myproject.model.utils.BaseEntity;

@Entity
@Table(name = "tbl_SURVEY_QUESTION_GROUP")
@AttributeOverride(name = "id", column = @Column(name = "ID"))
public class SurveyQuestionGroup extends BaseEntity<Long> {

	private static final long serialVersionUID = 9044215406135046286L;
	
    @Column(name = "TYPE")
	private Long type;
	
    @Column(name = "DESCRIPTION")
	private String description;

	public SurveyQuestionGroup() {
		super();
	}

	public Long getType() {
		return type;
	}

	public void setType(Long type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	
	
	

}
