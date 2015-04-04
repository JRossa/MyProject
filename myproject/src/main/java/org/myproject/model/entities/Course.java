package org.myproject.model.entities;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.myproject.model.utils.BaseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
@Table(name = "tbl_COURSE")
@AttributeOverride(name = "id", column = @Column(name = "ID"))
public class Course extends BaseEntity<Long> {

    private static final long serialVersionUID = -8975757469310208561L;

    static Logger logger = LoggerFactory.getLogger(Professorship.class);
    
    @Column(name = "CODE")
    private String code;
    
    @Column(name = "NEW_CODE")
    private String newCode;
    
    @Column(name = "START_EXECUTION_YEAR")
    private Integer startExecutionYear;
    
    @Column(name = "END_EXECUTION_YEAR")
    private Integer endExecutionYear;
    
    @Column(name = "NAME")
    private String name;
    
    @Column(name = "UNIT_ID")
    private Integer unitId;
    
    @Column(name = "UNIT_ACRONYM")
    private String unitAcronym;
    
    @Column(name = "THEORETICAL_HOURS")
    private Integer theoreticalHours;
    
    @Column(name = "THEORETICAL_PROBLEMS_HOURS", precision = 22, scale = 0)
    private Double theoreticalProblemsHours;
    
    @Column(name = "PROBLEMS_HOURS", precision = 22, scale = 0)
    private Double problemsHours;

    @Column(name = "LABORATORIAL_HOURS", precision = 22, scale = 0)
    private Double laboratorialHours;
    
    @Column(name = "SEMINARY_HOURS", precision = 22, scale = 0)
    private Double seminaryHours;
    
    @Column(name = "FIELD_WORK_HOURS", precision = 22, scale = 0)
    private Double fieldWorkHours;
    
    @Column(name = "TRAINING_PERIOD_HOURS", precision = 22, scale = 0)
    private Double trainingPeriodHours;
    
    @Column(name = "TUTORIAL_ORIENTATION_HOURS", precision = 22, scale = 0)
    private Double tutorialOrientationHours;
    
    @Column(name = "AUTONOMOUS_WORK_HOURS", precision = 22, scale = 0)
    private Double autonomousWorkHours;
    
    @Column(name = "WEEK_HOURS", precision = 22, scale = 0)
    private Double weekHours;
    
    @Column(name = "ECTS_CREDITS", precision = 22, scale = 0)
    private Double ectsCredits;



    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public String getNewCode() {
        return this.newCode;
    }

    public void setNewCode(String newCode) {
        this.newCode = newCode;
    }


    public Integer getStartExecutionYear() {
        return this.startExecutionYear;
    }

    public void setStartExecutionYear(Integer startExecutionYear) {
        this.startExecutionYear = startExecutionYear;
    }


    public Integer getEndExecutionYear() {
        return this.endExecutionYear;
    }

    public void setEndExecutionYear(Integer endExecutionYear) {
        this.endExecutionYear = endExecutionYear;
    }


    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Integer getUnitId() {
        return this.unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }


    public String getUnitAcronym() {
        return this.unitAcronym;
    }

    public void setUnitAcronym(String unitAcronym) {
        this.unitAcronym = unitAcronym;
    }


    public Integer getTheoreticalHours() {
        return this.theoreticalHours;
    }

    public void setTheoreticalHours(Integer theoreticalHours) {
        this.theoreticalHours = theoreticalHours;
    }


    public Double getTheoreticalProblemsHours() {
        return this.theoreticalProblemsHours;
    }

    public void setTheoreticalProblemsHours(Double theoreticalProblemsHours) {
        this.theoreticalProblemsHours = theoreticalProblemsHours;
    }


    public Double getProblemsHours() {
        return this.problemsHours;
    }

    public void setProblemsHours(Double problemsHours) {
        this.problemsHours = problemsHours;
    }


    public Double getLaboratorialHours() {
        return this.laboratorialHours;
    }

    public void setLaboratorialHours(Double laboratorialHours) {
        this.laboratorialHours = laboratorialHours;
    }

 
    public Double getSeminaryHours() {
        return this.seminaryHours;
    }

    public void setSeminaryHours(Double seminaryHours) {
        this.seminaryHours = seminaryHours;
    }

   
    public Double getFieldWorkHours() {
        return this.fieldWorkHours;
    }

    public void setFieldWorkHours(Double fieldWorkHours) {
        this.fieldWorkHours = fieldWorkHours;
    }

    
    public Double getTrainingPeriodHours() {
        return this.trainingPeriodHours;
    }

    public void setTrainingPeriodHours(Double trainingPeriodHours) {
        this.trainingPeriodHours = trainingPeriodHours;
    }

    
    public Double getTutorialOrientationHours() {
        return this.tutorialOrientationHours;
    }

    public void setTutorialOrientationHours(Double tutorialOrientationHours) {
        this.tutorialOrientationHours = tutorialOrientationHours;
    }

    
    public Double getAutonomousWorkHours() {
        return this.autonomousWorkHours;
    }

    public void setAutonomousWorkHours(Double autonomousWorkHours) {
        this.autonomousWorkHours = autonomousWorkHours;
    }

   
    public Double getWeekHours() {
        return this.weekHours;
    }

    public void setWeekHours(Double weekHours) {
        this.weekHours = weekHours;
    }

    
    public Double getEctsCredits() {
        return this.ectsCredits;
    }

    public void setEctsCredits(Double ectsCredits) {
        this.ectsCredits = ectsCredits;
    }

}
