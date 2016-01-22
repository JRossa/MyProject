package org.myproject.support.settings;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Named;

import org.primefaces.component.selectonemenu.SelectOneMenu;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;

@Scope(value = WebApplicationContext.SCOPE_SESSION)
@Named(value = "languageMBean")
public class LanguageMBean implements Serializable {

    private static final long serialVersionUID = -3157342453676049668L;

    private Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
    
    private String localeCode;

    private String languageName = "English";
    
    private static Map<String, Locale> countries;

    static {
        countries = new LinkedHashMap<String, Locale>();
        countries.put("English", new Locale("en"));
        countries.put("Português", new Locale("pt"));
    }

    public LanguageMBean() {
        
    }

    public Map<String, Locale> getCountries() {
        return countries;
    }

    public String getLocaleCode() {
        if ((this.localeCode == null) && (FacesContext.getCurrentInstance().getViewRoot() != null)
                && (FacesContext.getCurrentInstance().getViewRoot().getLocale() != null)) {
            this.localeCode = FacesContext.getCurrentInstance().getViewRoot().getLocale().getLanguage();
        }

        return this.localeCode;
    }

    public String getLanguageName() {
 
        this.getLocaleCode();
 
        if (this.localeCode.equals("en")) {
        	if (this.localeCode.equals("en")) {
	            this.languageName = "Português";
	        } else {
	            this.languageName = "English";
	        }    
        }

        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public String getLanguage() {
        return locale.getLanguage();
    }

    public void setLanguage(String language) {
        this.locale = new Locale(language);
        
        if (language.equals("en")) {
            this.languageName = "Português";
        } else {
            this.languageName = "English";
        }    
        FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public void setLocaleCode(String localeCode) {
        this.localeCode = localeCode;
    }

    public void localeCodeChanged(AjaxBehaviorEvent e) {
        String newLocaleValue = ((SelectOneMenu) e.getSource()).getValue() + "";

        for (Entry<String, Locale> entry : countries.entrySet()) {
            if (entry.getValue().toString().equals(newLocaleValue)) {
                this.languageName = entry.getValue().getCountry();
                
                FacesContext.getCurrentInstance().getViewRoot().setLocale((Locale) entry.getValue());
            }
        }
    }
}