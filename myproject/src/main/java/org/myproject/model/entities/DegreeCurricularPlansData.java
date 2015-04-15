package org.myproject.model.entities;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.myproject.model.utils.BaseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
@Table(name = "tbl_DEGREE_CURRICULAR_PLANS_DATA")
@AttributeOverride(name = "id", column = @Column(name = "ID"))
public class DegreeCurricularPlansData extends BaseEntity<Long> {

	private static final long serialVersionUID = 240483007805620419L;

    static Logger logger = LoggerFactory.getLogger(Professorship.class);

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "DEGREE_CODE")
    private Degree degree;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "COURSE_CODE")
    private Course course;

    @Column(name = "CURRICULAR_YEAR")
    private Integer curricularYear;

    @Column(name = "CURRICULAR_SEMESTER")
    private Integer curricularSemester;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "START_EXECUTION_YEAR")
    private ExecutionYear startExecutionYear;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "END_EXECUTION_YEAR")
    private ExecutionYear endExecutionYear;

    
    
	public DegreeCurricularPlansData() {
		super();
	}

	
	public Degree getDegree() {
		return degree;
	}

	public void setDegree(Degree degree) {
		this.degree = degree;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public Integer getCurricularYear() {
		return curricularYear;
	}

	public void setCurricularYear(Integer curricularYear) {
		this.curricularYear = curricularYear;
	}

	public Integer getCurricularSemester() {
		return curricularSemester;
	}

	public void setCurricularSemester(Integer curricularSemester) {
		this.curricularSemester = curricularSemester;
	}

	public ExecutionYear getStartExecutionYear() {
		return startExecutionYear;
	}

	public void setStartExecutionYear(ExecutionYear startExecutionYear) {
		this.startExecutionYear = startExecutionYear;
	}

	public ExecutionYear getEndExecutionYear() {
		return endExecutionYear;
	}

	public void setEndExecutionYear(ExecutionYear endExecutionYear) {
		this.endExecutionYear = endExecutionYear;
	}
	
    
    
}
