package org.myproject.support.controller;

import javax.inject.Named;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.myproject.model.utils.BaseBean;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;

@Scope(value = WebApplicationContext.SCOPE_SESSION)
@Named(value = "agencyResource")
public class AgencyResource extends BaseBean {
    
	private static final long serialVersionUID = -6132808632599768364L;
	
	private Integer id;
    private String name;
    private String EIN;
   
    
    public AgencyResource() {
		super();
	}

	public AgencyResource(Integer id, String name, String eIN) {
        super();
        this.id = id;
        this.name = name;
        EIN = eIN;
    }

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEIN() {
		return EIN;
	}

	public void setEIN(String eIN) {
		EIN = eIN;
	}
    
    
    
}

