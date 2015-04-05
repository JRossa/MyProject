package org.myproject.model.entities;

/*
 *  TODO - 
 */

import org.myproject.model.utils.BaseEntity;


public class ProfessorshipCourseHours extends BaseEntity<Long> {

    private static final long serialVersionUID = 3300473196513946471L;

    private String code;

    private String name;

    private Double hours;

    
    public ProfessorshipCourseHours(String code, String name, Double hours) {
        super();
        this.code = code;
        this.name = name;
        this.hours = hours;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getHours() {
        return hours;
    }

    public void setHours(Double hours) {
        this.hours = hours;
    }

}
