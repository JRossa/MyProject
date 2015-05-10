package org.myproject.support.scientificfield;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("scientificFieldConverter")
public class ScientificFieldConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		// TODO Auto-generated method stub
		
		System.out.println("AsString " + value);
		
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		// TODO Auto-generated method stub

		// No updateTeacher - scientificFieldId - passa aqui
		System.out.println("AsObject " + value);
		
		
		return value.toString();
	}

}
