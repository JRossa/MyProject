package org.myproject.theme;

import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.myproject.model.utils.BaseBean;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;


@Scope(value = WebApplicationContext.SCOPE_SESSION)
@Named(value = "guestPreferences")
public class GuestPreferences extends BaseBean {

	private static final long serialVersionUID = -5374830629646153457L;
	
	private String theme = "bootstrap";
	

	public String getTheme () {
		
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		
		if (params.containsKey("theme")) {
			
			this.theme = params.get("theme");
		}
		return this.theme;
	}

	
	public void setTheme (String theme) {
		this.theme = theme;
	}

}
