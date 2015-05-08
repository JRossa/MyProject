package org.myproject.model.entities;



import javax.persistence.AttributeOverride;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.myproject.model.utils.BaseEntity;

@Entity
@Table(name = "tbl_SURVEY_TYPE_GROUP")
@AttributeOverride(name = "id", column = @Column(name = "ID"))
public class SurveyTypeGroup extends BaseEntity<Long> {


	private static final long serialVersionUID = 8522803884815748404L;

	@Column(name = "DESCRIPTION")
	private String description;

    
	public SurveyTypeGroup() {
		super();
	}
	
	
	public SurveyTypeGroup(Long id, String description) {
		super();
		
		this.setId(id);
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	
}
