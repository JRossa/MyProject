package org.myproject.model.entities;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.myproject.model.utils.BaseEntity;

@Entity
@Table(name = "tbl_SURVEY_ANSWER_SCALE")
@AttributeOverride(name = "id", column = @Column(name = "ID"))
public class SurveyAnswerScale extends BaseEntity<Long> {

	private static final long serialVersionUID = -7442815737093444569L;

	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SCALE_TYPE")
	private SurveyScaleType scaleType;
	
    @Column(name = "VALUE")
	private Integer value;
	
    @Column(name = "TEXT")
	private String text;

	
	
	public SurveyAnswerScale() {
		super();
	}


	public SurveyAnswerScale(Long id, SurveyScaleType scale, Integer value, String text) {
		super();
		
		this.setId(id);
		this.scaleType = scale;
		this.value = value;
		this.text = text;
	}


	public SurveyScaleType getScale() {
		return scaleType;
	}



	public void setScale(SurveyScaleType scale) {
		this.scaleType = scale;
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
