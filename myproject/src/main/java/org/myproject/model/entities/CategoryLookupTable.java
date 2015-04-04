package org.myproject.model.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.myproject.model.utils.BaseEntity;

@Entity
@Table(name = "tbl_CATEGORY_LOOKUP_TABLE")
public class CategoryLookupTable extends BaseEntity<Long> {

    private static final long serialVersionUID = -7003951589901626119L;


    @Column(name = "CATEGORY")
    private String category;

    @Column(name = "CATEGORY_GROUP")
    private String categoryGroup;

    @Column(name = "ACADEMIC_CATEGORY_NAME")
    private String academicCategoryName;

    
    
    public CategoryLookupTable() {
        super();
    }

    
    public CategoryLookupTable(String category, String categoryGroup, String academicCategoryName) {
        super();
        this.category = category;
        this.categoryGroup = categoryGroup;
        this.academicCategoryName = academicCategoryName;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategoryGroup() {
        return categoryGroup;
    }

    public void setCategoryGroup(String categoryGroup) {
        this.categoryGroup = categoryGroup;
    }

    public String getAcademicCategoryName() {
        return academicCategoryName;
    }

    public void setAcademicCategoryName(String academicCategoryName) {
        this.academicCategoryName = academicCategoryName;
    }

    
}
