package org.myproject.support.role;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;

import org.myproject.model.entities.LogRole;
import org.myproject.model.repositories.RoleRepository;
import org.myproject.model.utils.BaseBean;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;

@Scope(value = WebApplicationContext.SCOPE_SESSION)
@Named(value = "roleMBean")
public class RoleMBean extends BaseBean {

    
    private static final long serialVersionUID = -8184929898353506854L;

    @Inject
    private RoleRepository roleRepository;    
    
    private List<LogRole> roles;
    
    private List<SelectItem> selectOneItemsRole;
    
    public void onLoad () {
        this.roles = roleRepository.findAll();
    }

    public List<LogRole> getRoles() {
        return roles;
    }

    public void setRoles(List<LogRole> roles) {
        this.roles = roles;
    }

    public List<SelectItem> getSelectOneItemsRole() {
        this.selectOneItemsRole = new ArrayList<SelectItem>();
        
        List <LogRole> selectesRoles = roleRepository.findAll();
        
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext(); 
        String rolename = (String) context.getSessionMap().get("rolename");
        
        System.out.println("Role Log User  :  " + rolename);
        
        for (LogRole role: selectesRoles) {
            SelectItem selectItem = new SelectItem(role.getId(), role.getRolename());
            this.selectOneItemsRole.add(selectItem);
        }
            
        return  this.selectOneItemsRole;
    }


}


