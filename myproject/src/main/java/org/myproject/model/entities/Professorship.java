package org.myproject.model.entities;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.myproject.model.entities.Teacher;
import org.myproject.model.utils.BaseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
@Table(name = "tbl_PROFESSORSHIP")
@AttributeOverride(name = "id", column = @Column(name = "ID"))
public class Professorship extends BaseEntity<Long> {

    private static final long serialVersionUID = -7361068079022983283L;

    static Logger logger = LoggerFactory.getLogger(Professorship.class);

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TEACHER_CODE")
    private Teacher teacher;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "COURSE_CODE")
    private Course course;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "DEGREE_CODE")
    private Degree degree;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "EXECUTION_YEAR")
    private ExecutionYear year;

    @Column(name = "SEMESTER")
    private Short semester;

    @Column(name = "LECTURED_HOURS")
    private Float lecturedHours;

    @Column(name = "RESPONSIBLE_FOR_COURSE")
    private Boolean responsibleForCourse;

    @Temporal(TemporalType.DATE)
    @Column(name = "BEGIN_DATE", length = 10)
    private Date beginDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "END_DATE", length = 10)
    private Date endDate;

    public Professorship() {
        this.setId(0L);
        this.teacher = new Teacher();
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Course getCourse() {
        return this.course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Degree getDegree() {
        return this.degree;
    }

    public void setDegree(Degree degree) {
        this.degree = degree;
    }

    public ExecutionYear getYear() {
        return this.year;
    }

    public void setYear(ExecutionYear Year) {
        this.year = Year;
    }

    public Short getSemester() {
        return this.semester;
    }

    public void setSemester(Short semester) {
        this.semester = semester;
    }

    public Float getLecturedHours() {
        return this.lecturedHours;
    }

    public void setLecturedHours(Float lecturedHours) {
        this.lecturedHours = lecturedHours;
    }

    public Boolean getResponsibleForCourse() {
        return this.responsibleForCourse;
    }

    public void setResponsibleForCourse(Boolean responsibleForCourse) {
        this.responsibleForCourse = responsibleForCourse;
    }

    public Date getBeginDate() {
        return this.beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return this.endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

}
