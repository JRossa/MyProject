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
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
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
    
    private List<TeacherLessonPlanTotHours> teacherLessonPlanTotHours;

    private List<TeacherLessonPlanTotHours> filteredTeacherLessonPlanTotHours;
    
    private TeacherLessonPlanTotHours selectProfessorshipLessonPlan;
    
    private Long Id;
    
    private String selectedExecutionYear;
    
	private Date startDate = new Date();
	
	private Date endDate = new Date();
   
	
	
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
                this.selectProfessorshipLessonPlan = (TeacherLessonPlanTotHours) ev.getObject();
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

        
	public List<TeacherLessonPlanTotHours> getTeacherLesssonPlanTotHours() {
		return teacherLessonPlanTotHours;
	}


	public void setTeacherLessonPlanTotHours(
			List<TeacherLessonPlanTotHours> teacherLessonPlanHours) {
		this.teacherLessonPlanTotHours = teacherLessonPlanHours;
	}


	public List<TeacherLessonPlanTotHours> getTeacherLessonPlanTotHours() {
		return teacherLessonPlanTotHours;
	}



	public TeacherLessonPlanTotHours getSelectProfessorshipLessonPlan() {
		return selectProfessorshipLessonPlan;
	}


	public void setSelectProfessorshipLessonPlan(
			TeacherLessonPlanTotHours selectProfessorshipLessonPlan) {
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

    public void onLoadProfessorshipLessonPlanTotHours() {
        System.out.println("onLoadProfessorshipLessonPlanTotHours");

		Teacher teacher = new Teacher();
		Course course = new Course();
		

		this.teacherLessonPlanTotHours = new ArrayList<TeacherLessonPlanTotHours>();
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		
		try {
	        TimeZone timezone = TimeZone.getTimeZone("Europe/Lisbon");

	        // 01:00:00 because of daylight computation
			this.startDate = sdf.parse("01-10-2015 01:00:00");
	        Boolean daylight = timezone.inDaylightTime(this.startDate);
	        
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
        System.out.println("Start Time : " + sdf.format(this.startDate));
        System.out.println("End Time   : " + sdf.format(this.endDate));
		
        
        List<Object[]> teacherHours = 
            		this.teacherLessonPlanHoursRepository.findTeacherCoursesHoursBetweenStartDateAndEndDate(this.startDate, this.endDate);

        Long id = 0L;
        for (Object[] c : teacherHours) {
            System.out.println("TeacherId    :  " + c[0].toString());
            System.out.println("CourseId     :  " + c[1].toString());
            System.out.println("Hours        :  " + c[2].toString());

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
            this.teacherLessonPlanTotHours.add(new TeacherLessonPlanTotHours(id, teacher, course, startDate, endDate, Integer.parseInt(c[2].toString())));
        }
    }



	public List<TeacherLessonPlanTotHours> getFilteredTeacherLessonPlanTotHours() {
		return filteredTeacherLessonPlanTotHours;
	}



	public void setFilteredTeacherLessonPlanTotHours(
			List<TeacherLessonPlanTotHours> filteredTeacherLessonPlanTotHours) {
		this.filteredTeacherLessonPlanTotHours = filteredTeacherLessonPlanTotHours;
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


    public Date correctDayLight (Date date) {
    	
        TimeZone timezone = TimeZone.getTimeZone("Europe/Lisbon");
        Boolean dayLight = timezone.inDaylightTime(date);
        
        Long timeOffset = dayLight ? timezone.getOffset(System.currentTimeMillis()) : 0L;

        Calendar calendar = Calendar.getInstance();
        
        calendar.setTime(date);
        
        calendar.setTimeZone(timezone);

        return calendar.getTime();
    }
    
    
    public String executeLessonPlan (String compileFileName) {
    	
    	// Call JasperReport method to do it
        try {
            
            if (this.startDate != null) {
            	String strStartDate = new SimpleDateFormat("yyyy-MM-dd").format(this.getStartDate());
            	
            }
 
            if (this.endDate != null) {
           	String strEndDate = new SimpleDateFormat("yyyy-MM-dd").format(this.getEndDate());
           }


        } catch (Exception e) {
            // make your own exception handling
            e.printStackTrace();
        }

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

}
