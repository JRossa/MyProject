package org.myproject.model.entities;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.myproject.model.utils.BaseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
@Table(name = "tbl_MASTER_DEGREE_TYPE")
@AttributeOverride(name = "id", column = @Column(name = "ID"))
public class MasterDegreeType extends BaseEntity<Long> {

    private static final long serialVersionUID = 4797385063510283118L;

    static Logger logger = LoggerFactory.getLogger(MasterDegreeType.class);
    
    @Column(name = "DEGREE_TYPE", nullable = false)
    private String degreeType;

    public MasterDegreeType() {
    }

	public String getDegreeType() {
		return degreeType;
	}

	public void setDegreeType(String degreeType) {
		this.degreeType = degreeType;
	}


}
