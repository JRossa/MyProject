package org.myproject.support.email;

import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.myproject.model.entities.Course;
import org.myproject.model.entities.Degree;
import org.myproject.model.entities.LessonPlan;
import org.myproject.model.entities.Teacher;
import org.myproject.model.repositories.CategoryLookupTableRepository;
import org.myproject.model.repositories.LessonPlanRepository;
import org.myproject.model.utils.BaseBean;
import org.myproject.model.utils.MailSender;
import org.myproject.support.lessonplan.LessonPlanMBean;
import org.primefaces.context.RequestContext;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;

@Scope(value = WebApplicationContext.SCOPE_REQUEST)
@Named(value = "emailCRUDMBean")
public class EMailCRUDMBean extends BaseBean {

	private static final long serialVersionUID = 3377108518781380894L;

    @Inject
    private LessonPlanMBean mbLessonPlanMBean;

    @Inject
    private LessonPlanRepository lessonPlanRepository;

    @Inject
    private CategoryLookupTableRepository  categoryLookupTableRepository;

	private String subject;
	
	private Teacher teacher;
	
	private String text;

	
	
	
	public EMailCRUDMBean() {
		super();
		
		this.teacher = new Teacher();
		this.setText("\n\n");
		
        this.setText(this.insertTxt(this.getText()));
        this.setText(this.insertSignature(this.getText()));
		
	}

	
	public void sendEMail () {
        String msg = getResourceProperty("labels", "user_email_sent");
        String academicName = this.categoryLookupTableRepository.findAcademicNameByCategory(this.mbLessonPlanMBean.getSelectedLessonPlan().getTeacher().getCategory());
        
        
        System.out.println("Send EMail" + this.mbLessonPlanMBean.getSelectedLessonPlan().getTeacher().getCategory() + "  "
        		                        + academicName + "  "
        		                        + this.mbLessonPlanMBean.getSelectedLessonPlan().getTeacher().getFullName());

        MailSender mail = new MailSender();
        
        if (academicName.contains("Professor")) {
        	academicName = "Professor(a)";
        }
        
        String emailSubject = "Envio de notificação para preenchimento de sumários";
        String emailMsg = "Exmo Senhor <br/>"
                        + academicName + "   " + this.mbLessonPlanMBean.getSelectedLessonPlan().getTeacher().getFullName() + "<br/>"
                        + "<br/><br/>" 
                        + "EMail         : " + this.mbLessonPlanMBean.getSelectedLessonPlan().getTeacher().getEMail() + "<br/>"
                        + "<br/><br/>";
        
        emailMsg = emailMsg + this.getText();
        emailMsg = emailMsg + "<br/><br/>";
        
        emailMsg = emailMsg.replaceAll("(\\r\\n|\\n)", "<br/>");
        
     // TODO - mudar o endereço de email no final
        mail.sendEmail("gepaq@academiamilitar.pt", 
                       "gepaq@academiamilitar.pt", emailSubject, emailMsg,
                       "gepaq@academiamilitar.pt", "chefegepaq");

        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null);
        FacesContext.getCurrentInstance().addMessage(null, message);

    }

	
	public void sendTeacherEMail (Teacher teacher, Date date, Course course, Degree degree) {
        String msg = getResourceProperty("labels", "user_email_sent");
        String academicName = this.categoryLookupTableRepository.findAcademicNameByCategory(teacher.getCategory());
        
        
        System.out.println("Send EMail" + teacher.getCategory() + "  "
        		                        + academicName + "  "
        		                        + teacher.getFullName());

        MailSender mail = new MailSender();

        if (academicName.contains("Professor")) {
        	academicName = "Professor(a)";
        }

        String emailSubject = "Envio de notificação para preenchimento de sumários";
        String emailMsg = "Exmo Senhor(a) \n"
                        + academicName + "   " + teacher.getFullName() + "\n"
                        + "\n\n" 
                        + "EMail         : " + teacher.getEMail() + "\n"
                        + "\n\n"
                        + " Data      : " + date + "\n"
                        + " Curso     : " + degree.getName() + "\n"
                        + " UC        : " + course.getName() + "\n"
                        + "\n\n";
 
        
        
        emailMsg = emailMsg + this.getText();
 
        emailMsg = emailMsg.replaceAll("(\\r\\n|\\n)", "<br/>");

        // TODO - mudar o endereço de email no final
        mail.sendEmail("gepaq@academiamilitar.pt", 
                       "gepaq@academiamilitar.pt", emailSubject, emailMsg,
                       "gepaq@academiamilitar.pt", "chefegepaq");

        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null);
        FacesContext.getCurrentInstance().addMessage(null, message);

    }

	
	private String insertTxt (String text) {
	
		return text = text 
				+ "Agradecia que preenchesse o sumário da UC acima referida \n\n";
	}
	
	
	private String insertSignature (String text) {
		
		return text = text 
				+ "Coms os melhores cumprimentos, \n\n"
				+ "X";
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
			System.out.println("Teacher Fora : " + lp.getTeacher().getFullName() + "     " 
					+ this.computeDiffDays(lp.getEndDate(), date));
			
			if (this.computeDiffDays(lp.getEndDate(), date) > 10L) {
//				System.out.println("Teacher : " + lp.getTeacher().getFullName());
				this.sendTeacherEMail(lp.getTeacher(), lp.getEndDate(), lp.getCourse(), lp.getDegree());
			}
		}
    }

	
    public void disableButtons()  {
        
    	// widgetVar="sendEmailButton"
        if (this.mbLessonPlanMBean.getDisableButtons()) {
//      		RequestContext.getCurrentInstance().execute("sendEmailButton.disable();");
//       		RequestContext.getCurrentInstance().execute("deleteButton.disable();");
//       		RequestContext.getCurrentInstance().execute("dataTable.unselectAllRows();");
        }
        
    }


    public String getResourceProperty(String resource, String label) {
        Application application = FacesContext.getCurrentInstance().getApplication();
        ResourceBundle bundle = application.getResourceBundle(FacesContext.getCurrentInstance(), resource);

        return bundle.getString(label);
    }

    
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	

}
