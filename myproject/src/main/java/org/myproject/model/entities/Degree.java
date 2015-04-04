package org.myproject.model.entities;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.myproject.model.utils.BaseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
@Table(name = "tbl_DEGREE")
@AttributeOverride(name = "id", column = @Column(name = "ID"))
public class Degree extends BaseEntity<Long> {

    private static final long serialVersionUID = 6079866596451617573L;
    
    static Logger logger = LoggerFactory.getLogger(Professorship.class);

    @Column(name = "CODE")
    private String code;

    @Column(name = "NAME")
    private String name;

    @Column(name = "MASTER")
    private Boolean master;

    @Column(name = "MAIN_SCIENTIFIC_AREA")
    private Integer mainScientificArea;

    @Column(name = "DEPARTMENT_ID")
    private Integer departmentId;

    @Column(name = "DEPARTMENT_ACRONYM")
    private String departmentAcronym;

    public Degree() {
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getMaster() {
        return this.master;
    }

    public void setMaster(Boolean master) {
        this.master = master;
    }

    public Integer getMainScientificArea() {
        return this.mainScientificArea;
    }

    public void setMainScientificArea(Integer mainScientificArea) {
        this.mainScientificArea = mainScientificArea;
    }

    public Integer getDepartmentId() {
        return this.departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentAcronym() {
        return this.departmentAcronym;
    }

    public void setDepartmentAcronym(String departmentAcronym) {
        this.departmentAcronym = departmentAcronym;
    }

}
