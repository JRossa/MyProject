package org.myproject.model.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.myproject.model.utils.BaseEntity;

@Entity
@Table(name = "tbl_CATEGORY")
public class CategoryGroup extends BaseEntity<Long> {

    private static final long serialVersionUID = 1889665881647363607L;

    @Column(name = "GROUP")
    private String group;

    @Column(name = "NAMES")
    private String names;

    @Column(name = "DESIGNATION")
    private String designation;

    public CategoryGroup() {
        
    }

    public CategoryGroup(String group, String names, String designation) {

        this.group = group;
        this.names = names;
        this.designation = designation;
    }

    public String getGroup() {
        return this.group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getNames() {
        return this.names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getDesignation() {
        return this.designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

}
