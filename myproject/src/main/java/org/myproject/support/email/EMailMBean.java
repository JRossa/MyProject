package org.myproject.support.email;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.myproject.model.entities.Course;
import org.myproject.model.entities.Degree;
import org.myproject.model.entities.LessonPlan;
import org.myproject.model.entities.Teacher;
import org.myproject.model.repositories.CategoryLookupTableRepository;
import org.myproject.model.repositories.LessonPlanRepository;
import org.myproject.model.utils.BaseBean;
import org.myproject.model.utils.MailSender;
import org.myproject.support.clock.AlarmClock;
import org.myproject.support.clock.ClockMBean;
import org.myproject.support.clock.FetchMail;
import org.myproject.support.lessonplan.LessonPlanMBean;
import org.primefaces.context.RequestContext;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;

@Scope(value = WebApplicationContext.SCOPE_REQUEST)
@Named(value = "emailMBean")
public class EMailMBean extends BaseBean {

	private static final long serialVersionUID = 3377108518781380894L;

	private final static Long limitDays = 4L;


	@Inject
	private ClockMBean mbClockMBean;
	
    @Inject
    private LessonPlanMBean mbLessonPlanMBean;

    @Inject
    private LessonPlanRepository lessonPlanRepository;

    @Inject
    private CategoryLookupTableRepository  categoryLookupTableRepository;

	private String subject;
	
	private Teacher teacher;
	
	private String text;

    private Boolean selectAll;

	

	public EMailMBean() {
		super();
		
		this.teacher = new Teacher();
	}

	
    public Boolean getSelectAll() {
		return selectAll;
	}


	public void setSelectAll(Boolean selectAll) {
		this.selectAll = selectAll;
	}

	
	private String insertTxtBody () {
		
		return  this.getText() 
				+ "Agradecia que preenchesse o sumário da UC acima referida \n\n";
	}
	
	
	private String insertSignature () {
		
		return this.getText() 
				+ "Coms os melhores cumprimentos, \n\n"
				+ "X";
	}

	
	private String insertAutomaticProcess () {
		
		return this.getText() 
				+ "\n\nPS: E-Mail gerado e enviado por processo automático\n";
	}
	
	private String insertHeader (String academicName, String fullName, String email,
			                     Date date, String degreeName, String courseName) {
        return "Exmo(a) Senhor(a) \n"
                + academicName + "   " + fullName + "\n"
                + "\n\n" 
                + "EMail      : " + email + "\n"
                + "\n\n"
                + " Data      : " + date + "\n"
                + " Curso     : " + degreeName + "\n"
                + " UC        : " + courseName + "\n"
                + "\n\n";
		
	}
	
	
	public void email (String notification) {

		System.out.println("email : " + notification);
		
    	if (notification.equals("teacher")) {
    		this.selectAll = false;
    		this.teacher = this.mbLessonPlanMBean.getSelectedLessonPlan().getTeacher();

    		String academicName = this.categoryLookupTableRepository.findAcademicNameByCategory(this.teacher.getCategory());
    		String fullName = this.teacher.getFullName();
    		Date   date = this.mbLessonPlanMBean.getSelectedLessonPlan().getStartDate();
    		String email = this.teacher.getEMail();
    		String degreeName =  this.mbLessonPlanMBean.getSelectedLessonPlan().getDegree().getName();
    		String courseName =  this.mbLessonPlanMBean.getSelectedLessonPlan().getCourse().getName();
    		
    		this.setText(this.insertHeader(academicName, fullName, email, date, degreeName, courseName));
            this.setText(this.insertTxtBody());
            this.setText(this.insertSignature());
   		
    	} else {
    		this.selectAll = true;
    		this.teacher = new Teacher();
    		
            this.setText(this.insertTxtBody());
            this.setText(this.insertSignature());
   		    this.setText(this.insertAutomaticProcess());
    	}
    	
		this.subject =  "Envio de notificação para preenchimento de sumários";
		
   	
    }
  
	
	public void sendEMail () {
		
		if (this.mbLessonPlanMBean.getSelectedLessonPlan() != null) {
			Teacher teacher = this.mbLessonPlanMBean.getSelectedLessonPlan().getTeacher();
			Date    date    = this.mbLessonPlanMBean.getSelectedLessonPlan().getStartDate();
			Course  course  = this.mbLessonPlanMBean.getSelectedLessonPlan().getCourse();
			Degree  degree  = this.mbLessonPlanMBean.getSelectedLessonPlan().getDegree();
			
			this.sendTeacherEMail(teacher, date, course, degree);
		}
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

        String emailSubject = this.subject;
        String emailMsg = this.insertHeader(academicName, teacher.getFullName(),
        		                            teacher.getEMail(), date, degree.getName(), course.getName());
 
        emailMsg = emailMsg + this.getText();
 
        emailMsg = emailMsg.replaceAll("(\\r\\n|\\n)", "<br/>");

        // TODO - mudar e verificar se é válido o endereço de email no final
        mail.sendEmail("gepaq@academiamilitar.pt", 
                       "gepaq@academiamilitar.pt", emailSubject, emailMsg,
                       "gepaq@academiamilitar.pt", "chefegepaq");

        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null);
        FacesContext.getCurrentInstance().addMessage(null, message);

    }

	
	public static Boolean isValidEmailAddress(String email) {
		Boolean isValid = false;
		
		try {
		     InternetAddress emailAddr = new InternetAddress(email);
		     
		     emailAddr.validate();
		     isValid = true;
		} catch (AddressException ex) {
			System.out.println("Invali email :" + email);
		}
		
	   return isValid;
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
			
			if (this.computeDiffDays(lp.getEndDate(), date) > limitDays) {
//				System.out.println("Teacher : " + lp.getTeacher().getFullName());
				this.sendTeacherEMail(lp.getTeacher(), lp.getEndDate(), lp.getCourse(), lp.getDegree());
			}
		}
    }
	
	
    public void disableButtons()  {
   	    
    	mbClockMBean.setupTimeToSendNotification();
    	
    	this.subject = "";
    	this.teacher = new Teacher();
    	this.text = "";
    	
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


