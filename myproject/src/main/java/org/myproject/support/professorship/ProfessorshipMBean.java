package org.myproject.support.professorship;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.myproject.model.entities.Professorship;
import org.myproject.model.entities.Teacher;
import org.myproject.model.repositories.TeacherRepository;
import org.myproject.model.utils.BaseBean;
import org.primefaces.event.SelectEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;

@Scope(value = WebApplicationContext.SCOPE_SESSION)
@Named(value = "professorshipMBean")
public class ProfessorshipMBean extends BaseBean {

    private static final long   serialVersionUID = 3133883459814058016L;

    private static final Logger logger           = Logger.getLogger(ProfessorshipMBean.class);

    private List<Professorship> professorship;
    
    
    @PostConstruct
    public void init() {
        System.out.println("A new backing bean has been created");
        this.professorship = new ArrayList<Professorship>();
    }


    public List<Professorship> getProfessorship() {
        return professorship;
    }


    public void setProfessorship(List<Professorship> professorship) {
        this.professorship = professorship;
    }
    
    


}
