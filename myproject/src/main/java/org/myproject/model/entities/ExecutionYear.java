package org.myproject.model.entities;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.myproject.model.utils.BaseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@Entity
@Table(name = "tbl_EXECUTION_YEAR")
@AttributeOverride(name = "id", column = @Column(name = "ID"))
public class ExecutionYear extends BaseEntity<Long>  {

    
    private static final long serialVersionUID = 4820257534042861526L;
    
    static Logger logger = LoggerFactory.getLogger(Professorship.class);
    

    @Column(name = "EXECUTION_YEAR", nullable = false)
    private String executionYear;
    
    @Column(name = "TRANSITION_TABLE")
    private String transitionTable;

    public ExecutionYear() {
    }

    public String getExecutionYear() {
        return this.executionYear;
    }

    public void setExecutionYear(String executionYear) {
        this.executionYear = executionYear;
    }

   
    public String getTransitionTable() {
        return this.transitionTable;
    }

    public void setTransitionTable(String transitionTable) {
        this.transitionTable = transitionTable;
    }

}
