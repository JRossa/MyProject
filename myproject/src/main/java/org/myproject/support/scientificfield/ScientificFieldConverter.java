package org.myproject.support.scientificfield;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import org.myproject.model.entities.ScientificField;
import org.myproject.model.repositories.ScientificFieldRepository;
import org.myproject.support.teacher.TeacherCRUDMBean;
import org.springframework.beans.factory.annotation.Autowired;



@FacesConverter("scientificFieldConverter")
public class ScientificFieldConverter implements Converter {


	@Override
	public Object getAsObject(FacesContext context, UIComponent uiComponent, String value) {
		// TODO Auto-generated method stub
		
		System.out.println("AsString " + value);
		// No updateTeacher - scientificFieldid - tem de retornar um objecto iigual ao value do
		// <p:selectOneMenu id="scientificfieldid" value="#{teacherCRUDMBean.teacher.scientificFieldId.id}"
		
 		return Long.valueOf(value);
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		// TODO Auto-generated method stub

		// No updateTeacher - scientificFieldid - retorna sempre a conversão do objecto 'itemValue=' para string (Long)
		System.out.println("AsObject " + value);
		
		return value.toString();
	}

}
