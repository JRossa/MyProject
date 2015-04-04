package org.myproject.support.lessonplan;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.myproject.model.utils.BaseBean;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;

@Scope(value = WebApplicationContext.SCOPE_SESSION)
@Named(value = "schoolTimeMBean")
public class SchoolTimesMBean extends BaseBean {

	private static final long serialVersionUID = -8519746580974488551L;

	private String[] selectedSchoolTimes;
	private List<String> schoolTimes;

	@PostConstruct
	public void init() {
		
		schoolTimes = new ArrayList<String>();
		schoolTimes.add(getResourceProperty("labels", "schooltimes_1"));
		schoolTimes.add(getResourceProperty("labels", "schooltimes_2"));
		schoolTimes.add(getResourceProperty("labels", "schooltimes_3"));
		schoolTimes.add(getResourceProperty("labels", "schooltimes_4"));
		schoolTimes.add(getResourceProperty("labels", "schooltimes_5"));
		schoolTimes.add(getResourceProperty("labels", "schooltimes_6"));
		schoolTimes.add(getResourceProperty("labels", "schooltimes_7"));
		schoolTimes.add(getResourceProperty("labels", "schooltimes_8"));
		// schoolTimes.add("9º TE");
	}

	public String[] getSelectedSchoolTimes() {
		return selectedSchoolTimes;
	}

	public void setSelectedSchoolTimes(String[] selectedSchoolTimes) {
		this.selectedSchoolTimes = selectedSchoolTimes;
	}

	public List<String> getSchoolTimes() {
		return schoolTimes;
	}

	public void setSchoolTimes(List<String> schoolTimes) {
		this.schoolTimes = schoolTimes;
	}

	public String getResourceProperty(String resource, String label) {
		Application application = FacesContext.getCurrentInstance().getApplication();
		ResourceBundle bundle = application.getResourceBundle(FacesContext.getCurrentInstance(), resource);

		return bundle.getString(label);
	}

}
