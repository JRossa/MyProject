package org.myproject.support.scientificfield;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;

import org.myproject.model.entities.ScientificField;
import org.myproject.model.repositories.ScientificFieldRepository;
import org.myproject.model.utils.BaseBean;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;

@Scope(value = WebApplicationContext.SCOPE_SESSION)
@Named(value = "scientificFieldMBean")
public class ScientificFieldMBean extends BaseBean {

	private static final long serialVersionUID = 3852164271506739032L;
	
    @Inject
    private ScientificFieldRepository scientificFieldRepository;    
    
    private List<ScientificField> scientificFields;
    
    private List<SelectItem> selectOneItemsScientifField;
    
    public void onLoad () {
        this.scientificFields = this.scientificFieldRepository.findAll();
    }


    public List<ScientificField> getScientificFields() {
		return scientificFields;
	}


	public void setScientificFields(List<ScientificField> scientificFields) {
		this.scientificFields = scientificFields;
	}


	public List<SelectItem> getSelectOneItemsScientificField() {
        this.selectOneItemsScientifField = new ArrayList<SelectItem>();
        
        List <ScientificField> selectesScientificFields = scientificFieldRepository.findAll();
        
        for (ScientificField scientificField: selectesScientificFields) {
            SelectItem selectItem = new SelectItem(scientificField.getId(), scientificField.getDescription());
            this.selectOneItemsScientifField.add(selectItem);
        }
            
        return  this.selectOneItemsScientifField;
    }

	
}