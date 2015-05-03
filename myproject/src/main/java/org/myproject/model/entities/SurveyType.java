package org.myproject.model.entities;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.myproject.model.utils.BaseEntity;

@Entity
@Table(name = "tbl_SURVEY_TYPE")
@AttributeOverride(name = "id", column = @Column(name = "ID"))
public class SurveyType extends BaseEntity<Long> {


	private static final long serialVersionUID = 8522803884815748404L;

	@Column(name = "DESCRIPTION")
	private String description;

    
	public SurveyType() {
		super();
	}
	
	
	public SurveyType(Long id, String description) {
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
