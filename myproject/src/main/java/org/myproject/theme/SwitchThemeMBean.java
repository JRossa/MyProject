package org.myproject.theme;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Named;

import org.myproject.model.utils.BaseBean;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;
 
@Scope(value = WebApplicationContext.SCOPE_SESSION)
@Named(value = "switchThemeMBean")
public class SwitchThemeMBean extends BaseBean {
     
	private static final long serialVersionUID = 4682531775418709125L;

	private List<SelectItem> themes;
    
	private List<Theme> themeList;
	
	private GuestPreferences guestPreferences = new GuestPreferences();
	
	private String theme;
	
	private Boolean renderedThemeSwitch;
	
	
    public SwitchThemeMBean() {
    	
    	theme = guestPreferences.getTheme();
    	
        themes = new ArrayList<SelectItem>();
        
        setRenderedThemeSwitch();
        
        loadThemeList ();
        
        for (Theme theme: themeList) {
        	SelectItem selectItem = new SelectItem(theme.getName(), theme.getDisplayName());
            this.themes.add(selectItem);        	
        }
    }

    
	public void loadThemeList () {
        themeList = new ArrayList<Theme>();
        themeList.add(new Theme(0, "Afterdark", "afterdark"));
        themeList.add(new Theme(1, "Afternoon", "afternoon"));
        themeList.add(new Theme(2, "Afterwork", "afterwork"));
        themeList.add(new Theme(3, "Aristo", "aristo"));
        themeList.add(new Theme(4, "Black-Tie", "black-tie"));
        themeList.add(new Theme(5, "Blitzer", "blitzer"));
        themeList.add(new Theme(6, "Bluesky", "bluesky"));
        themeList.add(new Theme(7, "Bootstrap", "bootstrap"));
        themeList.add(new Theme(8, "Casablanca", "casablanca"));
        themeList.add(new Theme(9, "Cupertino", "cupertino"));
        themeList.add(new Theme(10, "Cruze", "cruze"));
        themeList.add(new Theme(11, "Dark-Hive", "dark-hive"));
        themeList.add(new Theme(12, "Delta", "delta"));
        themeList.add(new Theme(13, "Dot-Luv", "dot-luv"));
        themeList.add(new Theme(14, "Eggplant", "eggplant"));
        themeList.add(new Theme(15, "Excite-Bike", "excite-bike"));
        themeList.add(new Theme(16, "Flick", "flick"));
        themeList.add(new Theme(17, "Glass-X", "glass-x"));
        themeList.add(new Theme(18, "Home", "home"));
        themeList.add(new Theme(19, "Hot-Sneaks", "hot-sneaks"));
        themeList.add(new Theme(20, "Humanity", "humanity"));
        themeList.add(new Theme(21, "Le-Frog", "le-frog"));
        themeList.add(new Theme(22, "Midnight", "midnight"));
        themeList.add(new Theme(23, "Mint-Choc", "mint-choc"));
        themeList.add(new Theme(24, "Overcast", "overcast"));
        themeList.add(new Theme(25, "Pepper-Grinder", "pepper-grinder"));
        themeList.add(new Theme(26, "Redmond", "redmond"));
        themeList.add(new Theme(27, "Rocket", "rocket"));
        themeList.add(new Theme(28, "Sam", "sam"));
        themeList.add(new Theme(29, "Smoothness", "smoothness"));
        themeList.add(new Theme(30, "South-Street", "south-street"));
        themeList.add(new Theme(31, "Start", "start"));
        themeList.add(new Theme(32, "Sunny", "sunny"));
        themeList.add(new Theme(33, "Swanky-Purse", "swanky-purse"));
        themeList.add(new Theme(34, "Trontastic", "trontastic"));
        themeList.add(new Theme(35, "UI-Darkness", "ui-darkness"));
        themeList.add(new Theme(36, "UI-Lightness", "ui-lightness"));
        themeList.add(new Theme(37, "Vader", "vader"));
		
	}

    public void setRenderedThemeSwitch () {

	    ExternalContext context = FacesContext.getCurrentInstance().getExternalContext(); 
	    String rolename = (String) context.getSessionMap().get("rolename");

	    if (rolename.equals("ROLE_ADMIN")) {
	    	renderedThemeSwitch = true;
	    } else {
	    	renderedThemeSwitch = false;
	    }
	    
	}

//  <h:form rendered="#{switchThemeMBean.getRenderedThemeSwitch()}">
	public Boolean getRenderedThemeSwitch() {
		return renderedThemeSwitch;
	}


	public void setRenderedThemeSwitch(Boolean renderedThemeSwitch) {
		this.renderedThemeSwitch = renderedThemeSwitch;
	}


	public List<SelectItem> getThemes() {
		return themes;
	}


	public void setThemes(List<SelectItem> themes) {
		this.themes = themes;
	}


	public GuestPreferences getGuestPreferences() {
		return guestPreferences;
	}


	public void setGuestPreferences(GuestPreferences guestPreferences) {
		this.guestPreferences = guestPreferences;
	}


	public String getTheme() {
		return theme;
	}


	public void setTheme(String theme) {
		this.theme = theme;
	}
     
//	<p:ajax listener="#{switchThemeMBean.saveTheme}" />
    public void saveTheme() {
    	this.guestPreferences.setTheme(this.theme);
    }
}
