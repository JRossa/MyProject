package org.myproject.model.entities;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.myproject.model.utils.BaseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
@Table(name = "tbl_SCIENTIFIC_FIELD")
@AttributeOverride(name = "id", column = @Column(name = "ID"))
public class ScientificField extends BaseEntity<Long> {

    private static final long serialVersionUID = 4797385063510283118L;

    static Logger logger = LoggerFactory.getLogger(Professorship.class);
    
    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

    public ScientificField() {
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
