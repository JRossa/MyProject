package org.myproject.support.professorship;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.TimeZone;

import javax.annotation.PostConstruct;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Weeks;
import org.myproject.model.entities.Course;
import org.myproject.model.entities.Degree;
import org.myproject.model.entities.Professorship;
import org.myproject.model.entities.Teacher;
import org.myproject.model.entities.TeacherLessonPlanHours;
import org.myproject.model.repositories.CourseRepository;
import org.myproject.model.repositories.ProfessorshipRepository;
import org.myproject.model.repositories.TeacherLessonPlanHoursRepository;
import org.myproject.model.repositories.TeacherRepository;
import org.myproject.model.utils.BaseBean;
import org.myproject.report.AbstractBaseReportBean.CompileOption;
import org.myproject.support.teacher.TeacherMBean;
import org.primefaces.event.SelectEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;

@Scope(value = WebApplicationContext.SCOPE_SESSION)
@Named(value = "professorshipMBean")
public class ProfessorshipMBean extends BaseBean {

    private static final long   serialVersionUID = 3133883459814058016L;

    private static final Logger logger           = Logger.getLogger(ProfessorshipMBean.class);

	@Inject
	private TeacherLessonPlanHoursRepository teacherLessonPlanHoursRepository;

	@Inject
	private TeacherRepository teacherRepository;
	
	@Inject
	private CourseRepository courseRepository;

    @Inject
    private ProfessorshipRepository professorshipRepository;

    @Inject
    private TeacherMBean mbTeacherMBean;
    
    private List<Professorship> professorships;
    
    private Professorship selectProfessorship;

    private List<ProfessorshipCourseHours> professorshipCourseHours;
    
    private List<ProfessorshipLessonPlanCourseHours> professorshipLessonPlanCourseHours;

    private List<ProfessorshipLessonPlanCourseHours> filteredProfessorshipLessonPlanCourseHours;
    
    private ProfessorshipLessonPlanCourseHours selectProfessorshipLessonPlan;
    
    private Long Id;
    
    private String selectedExecutionYear;
    
	private Date startDate = new Date();
	
	private Date endDate = new Date();
   
	private Integer numberOfWeeks;
	
	private Boolean correctDate = true;
	
	
	
    public ProfessorshipMBean() {
		super();
	}

   
    
    public Long getId() {
		return Id;
	}


	public void setId(Long id) {
		Id = id;
	}


    public String getSelectedExecutionYear() {
		return selectedExecutionYear;
	}


	public void setSelectedExecutionYear(String selectedExecutionYear) {
		this.selectedExecutionYear = selectedExecutionYear;
	}


	public void selectProfessorship(SelectEvent ev) {
        try {
            if (ev.getObject() != null) {
                this.selectProfessorship = (Professorship) ev.getObject();
                System.out.println("Passou");
            } else {
                this.selectProfessorship = null;
            }
        } catch (Exception e) {
            this.selectProfessorship = null;

            logger.error(e.getMessage(), e);
        }
    }

    public void unselectProfessorship() {
        this.selectProfessorship = null;
    }


	public void selectProfessorshipLessonPlan(SelectEvent ev) {
        try {
            if (ev.getObject() != null) {
                this.selectProfessorshipLessonPlan = (ProfessorshipLessonPlanCourseHours) ev.getObject();
                System.out.println("Passou");
            } else {
                this.selectProfessorshipLessonPlan = null;
            }
        } catch (Exception e) {
            this.selectProfessorshipLessonPlan = null;

            logger.error(e.getMessage(), e);
        }
    }

    public void unselectProfessorshipLessonPlan() {
        this.selectProfessorshipLessonPlan = null;
    }

    public Professorship getSelectProfessorship() {
		return selectProfessorship;
	}



	public void setSelectProfessorship(Professorship selectProfessorship) {
		this.selectProfessorship = selectProfessorship;
	}



	public List<Professorship> getProfessorships() {
        return professorships;
    }

    
    public void setProfessorships(List<Professorship> professorships) {
        this.professorships = professorships;
    }

    
    public List<ProfessorshipCourseHours> getProfessorshipCourseHours() {
        return professorshipCourseHours;
    }
   
    
    public void setProfessorshipCourseHours(List<ProfessorshipCourseHours> professorshipCourseHours) {
        this.professorshipCourseHours = professorshipCourseHours;
    }

        
	public List<ProfessorshipLessonPlanCourseHours> getProfessorshipLessonPlanCourseHours() {
		return professorshipLessonPlanCourseHours;
	}


	public void setProfessorshipLessonPlanCourseHours(
			List<ProfessorshipLessonPlanCourseHours> professorshipLessonPlanCourseHours) {
		this.professorshipLessonPlanCourseHours = professorshipLessonPlanCourseHours;
	}


	public ProfessorshipLessonPlanCourseHours getSelectProfessorshipLessonPlan() {
		return selectProfessorshipLessonPlan;
	}


	public void setSelectProfessorshipLessonPlan(
			ProfessorshipLessonPlanCourseHours selectProfessorshipLessonPlan) {
		this.selectProfessorshipLessonPlan = selectProfessorshipLessonPlan;
	}


//	@PostConstruct
	public void init() {
        System.out.println("A new backing bean has been created");
        this.professorships = new ArrayList<Professorship>();
        
        
}


    public void onLoadProfessorshipCourseHours() {
        System.out.println("onLoadProfessorshipCourseHours");

        this.professorshipCourseHours = new ArrayList<ProfessorshipCourseHours>();
        this.selectedExecutionYear = this.mbTeacherMBean.getSelectedExecutionYear();

        if (this.mbTeacherMBean.getSelectedTeacher() != null) {
            System.out.println("Select Teacher Id :" + this.mbTeacherMBean.getSelectedTeacher().getId());

            this.selectedExecutionYear = this.mbTeacherMBean.getSelectedExecutionYear();
            
            List<Object[]> course = 
            		this.professorshipRepository.findCoursesByUserAndExecutionYear(
            				                                 this.mbTeacherMBean.getSelectedTeacher().getId(),
            				                                 this.selectedExecutionYear);

            for (Object[] c : course) {
                System.out.println("Code    :  " + c[1].toString());
                System.out.println("Name    :  " + c[2].toString());

                this.professorshipCourseHours.add(new ProfessorshipCourseHours(c[1].toString(), c[2].toString(), 0.0));
            }

            // Verificação
            for (ProfessorshipCourseHours ch : professorshipCourseHours) {
                System.out.println("Final Code    :  " + ch.getCode());
            }
        }
    }

    
    public void onLoadProfessorship() {
        System.out.println("onLoadProfessorship");

        if (this.mbTeacherMBean.getSelectedTeacher() != null) {
            System.out.println("Select Teacher Id :" + this.mbTeacherMBean.getSelectedTeacher().getId());

            this.selectedExecutionYear = this.mbTeacherMBean.getSelectedExecutionYear();
            if (this.selectedExecutionYear != null && this.selectedExecutionYear.length() == 9) {
            	this.professorships =  
            		this.professorshipRepository.findByTeacherIdAndExecutionYear(
            				   this.mbTeacherMBean.getSelectedTeacher().getId(), this.selectedExecutionYear);
            } else {
                this.professorships = this.mbTeacherMBean.getSelectedTeacher().getProfessorship();
            }
        }
    }

    
    public void onLoadProfessorshipLessonPlanTotHours(Boolean showCourse) {
        System.out.println("onLoadProfessorshipLessonPlanTotHours");

		Teacher teacher = new Teacher();
		Course course = new Course();
		Calendar calendar = Calendar.getInstance();

		this.professorshipLessonPlanCourseHours = new ArrayList<ProfessorshipLessonPlanCourseHours>();
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		
        TimeZone timezone = TimeZone.getTimeZone("Europe/Lisbon");
        
        if (this.correctDate.equals(true)) {
        	
			try {
				this.startDate = sdf.parse("01-10-2015 00:00:00");
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        
//        Boolean daylight = timezone.inDaylightTime(this.startDate);

        Long timeOffset = Integer.toUnsignedLong(timezone.getOffset(startDate.getTime()));  
        
        calendar.setTime(this.getStartDate());
        calendar.setTimeZone(timezone);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE),
        		      0, 0, 0);
       	calendar.setTimeInMillis(calendar.getTimeInMillis() + timeOffset);
       	
       	this.startDate.setTime(calendar.getTimeInMillis());
		
		
        System.out.println("Start Time : " + sdf.format(this.startDate));
        System.out.println("End Time   : " + sdf.format(this.endDate));

        if (this.correctDate.equals(true)) {
	       	DateTime dateTime1 = new DateTime(this.startDate);
	       	DateTime dateTime2 = new DateTime(this.endDate);
	
	       	this.numberOfWeeks = (int) Math.round(Days.daysBetween(dateTime1, dateTime2).getDays() / 7.0);
	
	    	this.correctDate = false;
        }
        
        if (showCourse.equals(true)) {
	        List<Object[]> teacherHours = 
	            		this.teacherLessonPlanHoursRepository.findTeacherCoursesHoursBetweenStartDateAndEndDate(this.startDate, this.endDate);
	
	        Long id = 0L;
	        for (Object[] c : teacherHours) {
	            System.out.println("TeacherId    :  " + c[0].toString());
	            System.out.println("CourseId     :  " + c[1].toString());
	            System.out.println("Hours        :  " + c[2].toString());
	            System.out.println("Weeks        :  " + this.numberOfWeeks);
	            
	            teacher = teacherRepository.findOne(Long.parseLong(c[0].toString()));
	            course = courseRepository.findOne(Long.parseLong(c[1].toString()));

	            id++;
/*            
	            try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
*/            
	            this.professorshipLessonPlanCourseHours.add(new ProfessorshipLessonPlanCourseHours(id, teacher, course, 
	            		              this.startDate, this.endDate, this.numberOfWeeks, Integer.parseInt(c[2].toString())));
	        } 
	        
        } else {
		    List<Object[]> teacherHours = 
	            		this.teacherLessonPlanHoursRepository.findTeacherHoursBetweenStartDateAndEndDate(this.startDate, this.endDate);
	
	        Long id = 0L;
	        for (Object[] c : teacherHours) {
	            System.out.println("TeacherId    :  " + c[0].toString());
	            System.out.println("Hours        :  " + c[1].toString());
	
	            teacher = teacherRepository.findOne(Long.parseLong(c[0].toString()));
	            course = new Course();
	            
	            id++;

	            this.professorshipLessonPlanCourseHours.add(new ProfessorshipLessonPlanCourseHours(id, teacher, course, 
	            					  this.startDate, this.endDate, this.numberOfWeeks, Integer.parseInt(c[1].toString())));
	        }
        }
    }


	public List<ProfessorshipLessonPlanCourseHours> getFilteredProfessorshipLessonPlanCourseHours() {
		return filteredProfessorshipLessonPlanCourseHours;
	}


	public void setFilteredProfessorshipLessonPlanCourseHours(
			List<ProfessorshipLessonPlanCourseHours> filteredProfessorshipLessonPlanCourseHours) {
		this.filteredProfessorshipLessonPlanCourseHours = filteredProfessorshipLessonPlanCourseHours;
	}


	public Date getStartDate() {
		return startDate;
	}


	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}


	public Date getEndDate() {
		return endDate;
	}


	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}


    public Integer getNumberOfWeeks() {
		return numberOfWeeks;
	}


	public void setNumberOfWeeks(Integer numberOfWeeks) {
		this.numberOfWeeks = numberOfWeeks;
	}


	public String executeLessonPlan (String compileFileName) {
    	
    	// TODO - Call JasperReport method to do it

        return null;
    }

    
    public void valueChangedDate (SelectEvent selectEvent) {
        Date date = (Date) selectEvent.getObject();
        
        System.out.println("Start Time : " + this.getStartDate());
        System.out.println("End   Time : " + this.getEndDate());
        System.out.println("Compare : " + (this.getStartDate().getTime() >= 
                                           this.getEndDate().getTime()));
        System.out.println("Message : " + date);
        
        if (this.getStartDate().getTime() >= this.getEndDate().getTime()) {
 
        	String msg = getResourceProperty("labels", "lessonplan_change_dates");
            
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, msg, msg );
            addMessage(message);
        } else {
	       	DateTime dateTime1 = new DateTime(this.startDate);
	       	DateTime dateTime2 = new DateTime(this.endDate);
	
	       	this.numberOfWeeks = (int) Math.round(Days.daysBetween(dateTime1, dateTime2).getDays() / 7.0);

        }
    }    

    
    public String getResourceProperty(String resource, String label) {
        Application application = FacesContext.getCurrentInstance().getApplication();
        ResourceBundle bundle = application.getResourceBundle(FacesContext.getCurrentInstance(), resource);

        return bundle.getString(label);
    }

    
    private void addMessage(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    

    public void valueChangedNumWeeks (ValueChangeEvent valueChangeEvent) {
    	String msg = valueChangeEvent.getNewValue().toString();
    	
    	this.onLoadProfessorshipLessonPlanTotHours(false);
    	
    	System.out.println("value changed..." + msg);
    }

    
    public void valueChangedCourseNumWeeks (ValueChangeEvent valueChangeEvent) {
    	String msg = valueChangeEvent.getNewValue().toString();
    	
    	this.onLoadProfessorshipLessonPlanTotHours(true);
    	
    	System.out.println("value changed..." + msg);
    }
}
