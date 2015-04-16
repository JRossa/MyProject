package org.myproject.support.lessonplan;

import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;

import org.myproject.model.entities.CategoryGroup;
import org.myproject.model.entities.Course;
import org.myproject.model.entities.Degree;
import org.myproject.model.entities.LessonPlan;
import org.myproject.model.entities.Teacher;
import org.myproject.model.repositories.CategoryLookupTableRepository;
import org.myproject.model.repositories.LessonPlanRepository;
import org.myproject.model.utils.BaseBean;
import org.myproject.model.utils.MailSender;
import org.myproject.model.utils.Stamp;
import org.myproject.support.teacher.TeacherMBean;
import org.primefaces.context.RequestContext;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;

@Scope(value = WebApplicationContext.SCOPE_REQUEST)
@Named(value = "lessonPlanCRUDMBean")
public class LessonPlanCRUDMBean extends BaseBean {

	private static final long serialVersionUID = -7772755916689935910L;

    @Inject
    private LessonPlanMBean mbLessonPlanMBean;

    @Inject
    private LessonPlanRepository lessonPlanRepository;

    @Inject
    private CategoryLookupTableRepository  categoryLookupTableRepository;


    private LessonPlan lessonPlan;

    private String title;
    
    
    public LessonPlanCRUDMBean() {
        super();
        
        this.lessonPlan = new LessonPlan();
        this.lessonPlan.setTeacher(new Teacher());
        this.lessonPlan.setCourse(new Course());
        this.lessonPlan.setDegree(new Degree());
    }

    
    public void update() {
        System.out.println("UpDate");
 

        this.title = getResourceProperty("labels", "lessonplan_update");

        System.out.println("Title  :  " + this.title);
        
        String username = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username");
       
        this.lessonPlan = this.mbLessonPlanMBean.getSelectedLessonPlan();

    }
 
    
    public LessonPlan getLessonPlan() {
		return lessonPlan;
	}


	public void setLessonPlan(LessonPlan lessonPlan) {
		this.lessonPlan = lessonPlan;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


    public void cancel() {
        this.mbLessonPlanMBean.unselectLessonPlan();
    }

    
    public void close() {
        this.mbLessonPlanMBean.unselectLessonPlan();
    }

    
	private UIComponent findComponent(String id, UIComponent where) {
		
		if (where == null) {
		   return null;
		}
		else if (where.getId().equals(id)) {
		   return where;
		}
		else {
		   List<UIComponent> childrenList = where.getChildren();
		   
		   if (childrenList == null || childrenList.isEmpty()) {
		      return null;
		   }
		   
		   for (UIComponent child : childrenList) {
			   
		      UIComponent result = null;
		      result = findComponent(id, child);
		      
		      if(result != null) {
		         return result;
		      }
		   }    
		return null;
		}   
	}
		
		
    public void disableButtons()  {
        
    	// widgetVar="sendEmailButton"
        if (this.mbLessonPlanMBean.getDisableButtons()) {
      		RequestContext.getCurrentInstance().execute("sendEmailButton.disable();");
//       		RequestContext.getCurrentInstance().execute("deleteButton.disable();");
//       		RequestContext.getCurrentInstance().execute("dataTable.unselectAllRows();");
        }
        
    }
 
    
    public void delete (boolean delete) {

        disableButtons();
        
        if (delete == true) {
            this.mbLessonPlanMBean.delete();
        } else {
            this.lessonPlan = this.mbLessonPlanMBean.getSelectedLessonPlan();
        }
    }


    public void save() {
        String msg = "Registo Inserido";

//		<p:commandButton icon="ui-icon-mySave" style="width: 100px;"
//                value="#{labels.lessonplan_save}"
//				 action="#{lessonPlanCRUDMBean.save}" 
//        
//       ATTENTION: This (process="@this") is for clean the form - 
//        		       - DO NOT use in save/update Buttons
//                     - It's for reset and close Buttons, to clean the last information (it initializes the Bean)
//
//				 process="@this" />

        System.out.println("Save  " + this.lessonPlan.getLocked() + "   " + this.lessonPlan.getId());

        String username = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username");

    	Stamp stamp = new Stamp();

        stamp.setModificationUser(username);
        stamp.setModificationDate(new Date());
        this.lessonPlan.setStamp(stamp);

        if (this.lessonPlan != null) {
            if (this.lessonPlan.getId() == null) {
                // Add - TODO
                msg = msg + "   Adicionado";
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null);
                FacesContext.getCurrentInstance().addMessage(null, message);

            } else {
                // Update
                this.lessonPlan = this.mbLessonPlanMBean.getSelectedLessonPlan();
                
                if (this.lessonPlan != null) {
                    this.lessonPlanRepository.save(this.lessonPlan);
                    
                    msg = msg + "   Atualizado";
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null);
                    FacesContext.getCurrentInstance().addMessage(null, message);

                } else {    
                    msg = "Erro na inserção do registo";
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, null);
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }    
            }
        }
        
       // System.out.printf("Log ID is %d and for returned account ID is %d\n", this.user.getId(), insertUser.getId());
        
    }

	public void sendEMail () {
        String msg = getResourceProperty("labels", "user_email_sent");
        String academicName = this.categoryLookupTableRepository.findAcademicNameByCategory(this.mbLessonPlanMBean.getLessonPlan().getTeacher().getCategory());
        
        
        System.out.println("Send EMail" + this.mbLessonPlanMBean.getSelectedLessonPlan().getTeacher().getCategory() + "  "
        		                        + academicName + "  "
        		                        + this.mbLessonPlanMBean.getSelectedLessonPlan().getTeacher().getFullName());

        MailSender mail = new MailSender();
        
        String emailSubject = "Envio de notificação para preenchimento de sumários";
        String emailMsg = "Exmo Senhor <br/>"
                        + academicName + "   " + this.mbLessonPlanMBean.getSelectedLessonPlan().getTeacher().getFullName() + "<br/>"
                        + "<br/><br/>" 
                        + "EMail         : " + this.mbLessonPlanMBean.getSelectedLessonPlan().getTeacher().getEMail() + "<br/>"
                        + "<br/><br/>";
        
     // TODO - mudar o endereço de email no final
        mail.sendEmail("gepaq@academiamilitar.pt", 
                       "gepaq@academiamilitar.pt", emailSubject, emailMsg,
                       "gepaq@academiamilitar.pt", "chefegepaq");

        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null);
        FacesContext.getCurrentInstance().addMessage(null, message);

    }


	public void sendTeacherEMail (Teacher teacher) {
        String msg = getResourceProperty("labels", "user_email_sent");
        String academicName = this.categoryLookupTableRepository.findAcademicNameByCategory(teacher.getCategory());
        
        
        System.out.println("Send EMail" + teacher.getCategory() + "  "
        		                        + academicName + "  "
        		                        + teacher.getFullName());

        MailSender mail = new MailSender();
        
        String emailSubject = "Envio de notificação para preenchimento de sumários";
        String emailMsg = "Exmo Senhor <br/>"
                        + academicName + "   " + teacher.getFullName() + "<br/>"
                        + "<br/><br/>" 
                        + "EMail         : " + teacher.getEMail() + "<br/>"
                        + "<br/><br/>";
        
        // TODO - mudar o endereço de email no final
        mail.sendEmail("gepaq@academiamilitar.pt", 
                       "gepaq@academiamilitar.pt", emailSubject, emailMsg,
                       "gepaq@academiamilitar.pt", "chefegepaq");

        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null);
        FacesContext.getCurrentInstance().addMessage(null, message);

    }

	
	private Long computeDiffDays (Date date1, Date date2) {
		
        Long diffTime = date2.getTime() - date1.getTime();
        Long numDays  =  diffTime / (24 * 60 * 60 * 1000);
		
        return numDays;
	}
	
	
	public void sendListEMail () { 
		List<LessonPlan> lessonPlans = this.lessonPlanRepository.findAllMissing();
		Date date = new Date();
		
		for (LessonPlan lp: lessonPlans) {
			if (this.computeDiffDays(this.lessonPlan.getEndDate(), date) > 10L) {
				this.sendTeacherEMail(lp.getTeacher());
			}
		}
    }
    
    
    public String getResourceProperty(String resource, String label) {
        Application application = FacesContext.getCurrentInstance().getApplication();
        ResourceBundle bundle = application.getResourceBundle(FacesContext.getCurrentInstance(), resource);

        return bundle.getString(label);
    }

}
