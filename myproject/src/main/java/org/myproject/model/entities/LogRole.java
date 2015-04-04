package org.myproject.model.entities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.myproject.model.utils.BaseEntity;



 
@Entity
@Table(name = "tbl_ROLE")
@AttributeOverride(name = "id", column = @Column(name = "ID"))
public class LogRole extends BaseEntity<Long>  {
 
    private static final long serialVersionUID = 6874667425302308430L;
    
    static Logger logger = LoggerFactory.getLogger(LogRole.class);
 
    @NotNull(message = "{rolename null}")
    @NotEmpty(message = "{rolename empty}")
    @Size(max = 50, message = "{rolename max}")
    @Column(name = "NAME", length = 50)
    private String rolename;
	
    @NotNull(message = "{description null}")
    @NotEmpty(message = "{description empty}")
    @Size(max = 50, message = "{description max}")
    @Column(name = "DESCRIPTION", length = 100)	
    private String description;  

    
    public LogRole() {
        this.rolename = "";
        this.description = "";
    }
  
    public LogRole(String rolename, String description) {
	super();
	this.rolename = rolename;
	this.description = description;
    }
	
	
	public String getRolename() {
       return rolename;
    }

    public void setRolename(String rolename) {
    	this.rolename = rolename;
    }
    

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

  
    
    @Override
    public String toString() {
        return String.format("%s(id=%d, rolename='%s')", 
                this.getClass().getSimpleName(), 
                this.getId(), this.getRolename());
    }
 
    @Override
    public boolean equals(Object obj) {
        
        if (this == obj)
            return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
 
	if (obj instanceof LogRole) {
        final LogRole other = (LogRole) obj;
 
        if (getId() == null) {
            if (other.getId() != null)
    				return false;
    		} else if (!getId().equals(other.getId()))
    			return false;
            
            if (getRolename() == null) {
    			if (other.getRolename() != null)
    				return false;
    		} else if (!getRolename().equals(other.getRolename()))
    			return false;
 
            if (getDescription() == null) {
    			if (other.getDescription() != null)
    				return false;
    		} else if (!getDescription().equals(other.getDescription()))
    			return false;
 	    }
        return false;
    }
 
    @Override
    public int hashCode() {
    	final int prime = 31;
    	
    	int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		result = prime * result + ((getRolename() == null) ? 0 : getRolename().hashCode());  
		result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());  
        return result;
    }
   
 }
