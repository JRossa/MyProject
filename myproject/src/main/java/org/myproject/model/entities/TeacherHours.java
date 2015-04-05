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
@Table(name = "tbl_TEACHER_HOURS")
@AttributeOverride(name = "id", column = @Column(name = "ID"))
public class TeacherHours extends BaseEntity<Long> {

    private static final long serialVersionUID = -3523046839968854829L;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TEACHER_ID")
    private Teacher teacher;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "COURSE_ID")
    private Course course;
    
    @Column(name = "SEMESTER")
    private Short semester;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "EXECUTION_YEAR")
    private ExecutionYear year;

    @Column(name = "RESPONSIBLE_FOR_COURSE")
    private Boolean responsible;
    
    @Column(name = "LECTURED_HOURS")
    private Float  hours;

    @Column(name = "SURVEY_DONE")
    private Boolean  surveyDone;
   
    
    
	public TeacherHours() {
        super();
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Short getSemester() {
        return semester;
    }

    public void setSemester(Short semester) {
        this.semester = semester;
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

    public Boolean getResponsible() {
        return responsible;
    }

    public void setResponsible(Boolean responsible) {
        this.responsible = responsible;
    }

    public Boolean getSurveyDone() {
        return surveyDone;
    }

    public void setSurveyDone(Boolean surveyDone) {
        this.surveyDone = surveyDone;
    }
    
}
