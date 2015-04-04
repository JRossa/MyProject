package org.myproject.support.degree;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;

import org.myproject.model.entities.Degree;
import org.myproject.model.repositories.DegreeRepository;
import org.myproject.model.utils.BaseBean;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;

@Scope(value = WebApplicationContext.SCOPE_SESSION)
@Named(value = "degreeMBean")
public class DegreeMBean extends BaseBean{

    private static final long serialVersionUID = 7979033414618829290L;

    private static final Logger logger = Logger.getLogger(DegreeMBean.class);

    @Inject
    private DegreeRepository degreeRepository;
   
    private List<SelectItem> selectOneItemsDegree;
    
    List<Degree> degrees;

    
    public void onLoad() {
        this.degrees = this.degreeRepository.findAll();
    }
    
    
    public List<SelectItem> getSelectOneItemsDegree () {
        this.selectOneItemsDegree = new ArrayList<SelectItem>();

        List<Degree> selectedDegrees = this.degreeRepository.findAll();
        
        for (Degree degree : selectedDegrees) {
            
            SelectItem selectItem = new SelectItem(degree.getId(), degree.getName());
            this.selectOneItemsDegree.add(selectItem);
        }

        return this.selectOneItemsDegree;
    }

    
    public List<SelectItem> getSelectOneItemsMasterDegree () {
        this.selectOneItemsDegree = new ArrayList<SelectItem>();

        List<Degree> selectedDegrees = this.degreeRepository.findAllMasterDegrees();
        
        for (Degree degree : selectedDegrees) {
//            System.out.println("Course :" + degree.getId() + "  " + degree.getName());
            
            SelectItem selectItem = new SelectItem(degree.getId(), degree.getCode() + " - " + degree.getName());
            this.selectOneItemsDegree.add(selectItem);
        }

        return this.selectOneItemsDegree;
    }

}
