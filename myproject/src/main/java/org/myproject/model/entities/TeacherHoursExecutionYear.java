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
@Table(name = "tbl_TEACHER_HOURS_EXECUTION_YEAR")
@AttributeOverride(name = "id", column = @Column(name = "ID"))
public class TeacherHoursExecutionYear extends BaseEntity<Long> {

	private static final long serialVersionUID = 916071810621243186L;

	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TEACHER_ID")
    private Teacher teacher;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "EXECUTION_YEAR")
    private ExecutionYear year;

    @Column(name = "YEAR_HOURS")
    private Float  hours;

    @Column(name = "SURVEY_DONE")
    private Boolean  surveyDone;
   
    
    
	public TeacherHoursExecutionYear() {
        super();
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public ExecutionYear getYear() {
        return year;
    }

    public void setYear(ExecutionYear year) {
        this.year = year;
    }

    public Float getHours() {
        return hours;
    }

    public void setHours(Float hours) {
        this.hours = hours;
    }

    public Boolean getSurveyDone() {
        return surveyDone;
    }

    public void setSurveyDone(Boolean surveyDone) {
        this.surveyDone = surveyDone;
    }
    
}
