package org.myproject.model.entities;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.myproject.model.utils.BaseEntity;

@Entity
@Table(name = "tbl_SURVEY_SCALE_TYPE")
@AttributeOverride(name = "id", column = @Column(name = "ID"))
public class SurveyScaleType extends BaseEntity<Long> {

	private static final long serialVersionUID = 8632376866780506953L;
	
    @Column(name = "DESCRIPTION")
	private String description;

    @OneToMany(mappedBy = "scaleType", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @OrderBy("VALUE ASC")
    private List<SurveyAnswerScale> scaleList = new LinkedList<SurveyAnswerScale>();
    
    
	public SurveyScaleType() {
		super();
	}
	
	
	public SurveyScaleType(Long id, String description) {
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


	public List<SurveyAnswerScale> getScaleList() {
		return scaleList;
	}


	public void setScaleList(List<SurveyAnswerScale> scaleList) {
		this.scaleList = scaleList;
	} 

}
