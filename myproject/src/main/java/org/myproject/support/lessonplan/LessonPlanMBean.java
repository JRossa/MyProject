package org.myproject.support.lessonplan;



import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

 
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.myproject.model.entities.Course;
import org.myproject.model.entities.Degree;
import org.myproject.model.entities.LessonPlan;
import org.myproject.model.entities.Teacher;
import org.myproject.model.repositories.CourseRepository;
import org.myproject.model.repositories.DegreeRepository;
import org.myproject.model.repositories.LessonPlanRepository;
import org.myproject.model.repositories.TeacherRepository;
import org.myproject.model.utils.BaseBean;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.LazyScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;
 
@Scope(value = WebApplicationContext.SCOPE_SESSION)
@Named(value = "lessonPlanMBean")
public class LessonPlanMBean extends BaseBean {

    private static final long serialVersionUID = -463490225130705048L;

    private static final Logger logger = Logger.getLogger(LessonPlanMBean.class);

	private static final int maxDiffDays = 7;

    @Inject
    private LessonPlanRepository lessonPlanRepository;

    @Inject
    private TeacherRepository teacherRepository;

    @Inject
    private CourseRepository courseRepository;

    @Inject
    private DegreeRepository degreeRepository;

    @Inject
    private SchoolTimesMBean mbSchoolTimesMBean;

    private ScheduleModel eventModel;
    
    private LessonPlan lessonPlan;
  
    private List<LessonPlan> lessonPlans;

    private LessonPlan selectedLessonPlan;
    
    private Long id;
    
    private Long teacherId;
    
    private Long courseId;

    private Long degreeId;
    
    private boolean newEvent;
    
    
    // lessonPlan - control buttons
    private Boolean renderedInit = true;
   
    private Boolean summaryLock = false;
 
    // listLessonPlan - control buttons
    private Boolean renderedUpdate;

    private Boolean renderedDelete;

    private Boolean disableButtons;

    
    
    public LessonPlanMBean() {
		super();
		
		this.disableButtons = true;
		
        this.lessonPlan = new LessonPlan();
        this.lessonPlan.setTeacher(new Teacher());
        this.lessonPlan.setCourse(new Course());
        this.lessonPlan.setDegree(new Degree());
	}


	@PostConstruct
    public void init () {

    	// setup objects
        
        this.renderedInit = false;
        this.summaryLock = false;
        
        this.lessonPlans = this.lessonPlanRepository.findAll();
        
        logger.info("lp:" + this.lessonPlans);
        
        eventModel = new DefaultScheduleModel();
        
//        Schedule assumes whole set of events are eagerly provided in ScheduleModel, if you have a huge
//        data set of events, lazy loading features would help to improve performance. In lazy loading mode,
//        only the events that belong to the displayed time frame are fetched whereas in default eager more
//        all events need to be loaded. 
//        eventModel = new LazyScheduleModel();
        
        for (LessonPlan lp: this.lessonPlans) {
            DefaultScheduleEvent ev = new DefaultScheduleEvent(); 
            
            System.out.println("StartDate :" + lp.getStartDate());
            System.out.println("EndDate :" + lp.getEndDate());
            System.out.println("Title :" + lp.getTitle());
            
            ev.setEndDate(lp.getEndDate());
            ev.setStartDate(lp.getStartDate());
            ev.setTitle("\n" + lp.getTitle());
            ev.setData(lp.getId());
            ev.setDescription("Descr");
            ev.setAllDay(false);
            ev.setEditable(true);

            switch (lp.getDegree().getId().intValue()) {
            	case 1:
            		ev.setStyleClass("amil");
            		break;
            	case 2:
            		ev.setStyleClass("armas");
            		break;
            	case 3:
            		ev.setStyleClass("art");
            		break;
            	default:	
            		ev.setStyleClass("armas");
            		
            }
            
            eventModel.addEvent(ev);
        }
    }

    
	public void onEventSelect(SelectEvent selectEvent)  {

    	ScheduleEvent event = (ScheduleEvent) selectEvent.getObject();
        
        this.setSummaryLock(false);
        
        for (LessonPlan lp: lessonPlans) {
            if (lp.getId() == (Long) event.getData()) {
                this.lessonPlan = this.lessonPlanRepository.findOne(lp.getId());

                
//                oldstring =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this.lessonPlan.getStartDate());
//                this.calendarDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(oldstring);
//                
//                System.out.printf("%s %tB %<te, %<tY\n", "Due date:", this.lessonPlan.getStartDate());
//                System.out.println("Calendar date : " + this.calendarDate);
//                24H = 1 * 24 * 60 * 60 * 1000
                
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - maxDiffDays);
//                calendar.add(Calendar.DAY_OF_YEAR, -maxDiffDays);
                
                Date lockDate = calendar.getTime();
               
//                System.out.println("End Date  : " + this.lessonPlan.getEndDate().getTime());
//                System.out.println("Lock Date  : " + lockDate.getTime());
//                System.out.println("End Date  : " + this.lessonPlan.getEndDate());
//                System.out.println("Lock Date  : " + lockDate);
//                System.out.println("Lock  : " + (this.lessonPlan.getEndDate().getTime() - lockDate.getTime()));
//                System.out.println("Lock  : " + (this.lessonPlan.getEndDate().getTime() < lockDate.getTime()));

                if (this.lessonPlan.getLocked() == true) {
                	if (this.lessonPlan.getEndDate().getTime() < lockDate.getTime()) {
                    	this.setSummaryLock(true);
                    }
                } 
                
                this.setTeacherId(this.lessonPlan.getTeacher().getId());
                this.setCourseId(this.lessonPlan.getCourse().getId());
                this.setDegreeId(this.lessonPlan.getDegree().getId());
                
                break;
            }
        }
    }
 
    
    public void onDateSelect(SelectEvent selectEvent) {
        ScheduleEvent event = new DefaultScheduleEvent("", (Date) selectEvent.getObject(), (Date) selectEvent.getObject());

        this.lessonPlan = new LessonPlan();

    	this.teacherId = 173L;
        this.courseId = 261L;

        this.lessonPlan.setStartDate(new Date(event.getStartDate().getTime()));
        this.lessonPlan.setEndDate(new Date(event.getEndDate().getTime()));
    }
   
 
    public void convertTeacherId () {
        

        if (this.getTeacherId() != null) {
            Teacher teacher = this.teacherRepository.findOne(this.getTeacherId());
            
            System.out.println(teacher.getFullName());
            this.lessonPlan.setTeacher(teacher);

        }
        
    }
    
    public void convertCourseId () {
        
        if (this.getCourseId() != null) {
            Course course = this.courseRepository.findOne(this.getCourseId());
            
            System.out.println(course.getName());
            this.lessonPlan.setCourse(course);
        }
    }
    
    
    public void convertDegreeId () {
        
        if (this.getDegreeId() != null) {
            Degree degree = this.degreeRepository.findOne(this.getDegreeId());
            
            System.out.println(degree.getName());
            this.lessonPlan.setDegree(degree);
        }
    }

    
    public void addEvent() {
        
//        System.out.println("Title : " + this.lessonPlan.getTitle());
//        System.out.println("Get Id : " + this.lessonPlan.getId());
//        
//     
//        System.out.println("Start Time : " + this.lessonPlan.getStartDate().getTime());
//        System.out.println("End   Time : " + this.lessonPlan.getEndDate().getTime());
//        System.out.println("Compare : " + (this.lessonPlan.getStartDate().getTime() <= 
//                                           this.lessonPlan.getEndDate().getTime()));
//        System.out.println("Teacher Id : " + this.getTeacherId());
//        System.out.println("Course Id : " + this.getCourseId());
//    	System.out.println("New Event  : " + this.getNewEvent());
//    	System.out.println("Local  : " + this.lessonPlan.getPlace());
//    	System.out.println("Description  : " + this.lessonPlan.getDescription());
    	
        this.convertTeacherId();
        this.convertCourseId();
        this.convertDegreeId();
        
        String username = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username");

        this.lessonPlan.setModificationUser(username);
        this.lessonPlan.setModificationDate(new Date());

        if (this.getNewEvent() == true) {
        	this.lessonPlan.setId(null);
        }

       
        if (this.lessonPlan.getId() == null) {
            if (this.lessonPlan.getStartDate().getTime() <= 
                       this.lessonPlan.getEndDate().getTime()) {
                
                this.lessonPlan.setCreationUser(username);
                this.lessonPlan.setCreationDate(new Date());

                LessonPlan retLessonPlan = this.lessonPlanRepository.save(this.lessonPlan);
                
                System.out.printf("Log ID is %d and for returned account ID is %d\n", lessonPlan.getId(), retLessonPlan.getId());
                
                String msg = getResourceProperty("labels", "lessonplan_save_ok");
                
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
                addMessage(message);

                this.init();
            } else {
                    String msg = getResourceProperty("labels", "lessonplan_change_dates");

                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg );
                    addMessage(message);
                }
            
            } else {
                LessonPlan retLessonPlan = this.lessonPlanRepository.saveAndFlush(this.lessonPlan);
                System.out.printf("Log ID is %d and for returned account ID is %d\n", lessonPlan.getId(), retLessonPlan.getId());
 
                String msg = getResourceProperty("labels", "lessonplan_update_ok");

                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg );
                addMessage(message);

                this.init();
            }
     }

    
    public void deleteEvent() {
 
        System.out.println("Get Id : " + this.lessonPlan.getId());

    	if (this.lessonPlan.getId() != null) {
    		this.lessonPlanRepository.delete(this.lessonPlan.getId());
    		
    		this.init();
    	}
    }
  
    
    public void cleanEvent() {
    	
    	this.setSummaryLock(false);
    	
    	this.setLessonPlan(new LessonPlan());
    	
    	this.setTeacherId(173L);
        this.setCourseId(261L);
        this.lessonPlan.setStartDate(new Date());
        
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE));
        calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR) + 1);

        this.lessonPlan.setEndDate(calendar.getTime());
        
    	System.out.println("Clean Event  : ");
    	
    }
    
    public void onEventMove(ScheduleEntryMoveEvent scheduleEntryMoveEvent) {
    	
        for (LessonPlan lp: lessonPlans) {
            if (lp.getId() == (Long) scheduleEntryMoveEvent.getScheduleEvent().getData()) {
            	this.lessonPlan = this.lessonPlanRepository.findOne(lp.getId());
            	
                Date startDate = new Date(this.lessonPlan.getStartDate().getTime() 
              		     + (scheduleEntryMoveEvent.getDayDelta() * 24 * 60 * 60 * 1000)
              		     + (scheduleEntryMoveEvent.getMinuteDelta() * 60 * 1000));
        
                this.lessonPlan.setStartDate(startDate);
                
                Date endDate = new Date(this.lessonPlan.getEndDate().getTime() 
             		     + (scheduleEntryMoveEvent.getDayDelta() * 24 * 60 * 60 * 1000)
             		     + (scheduleEntryMoveEvent.getMinuteDelta() * 60 * 1000));
                
                this.lessonPlan.setEndDate(endDate);
                
                LessonPlan retLessonPlan = this.lessonPlanRepository.saveAndFlush(this.lessonPlan);
                System.out.printf("Log ID is %d and for returned account ID is %d\n", lessonPlan.getId(), retLessonPlan.getId());
 
            	this.init();
            	
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, 
              		  "Event moved", "Day delta:" + scheduleEntryMoveEvent.getDayDelta() 
              		+ ", Minute delta:" + scheduleEntryMoveEvent.getMinuteDelta());
               
                addMessage(message);
            }
        }
        
    }
     
    public void onEventResize(ScheduleEntryResizeEvent scheduleEntryResizeEvent) {
    	
        for (LessonPlan lp: lessonPlans) {
            if (lp.getId() == (Long) scheduleEntryResizeEvent.getScheduleEvent().getData()) {
            	this.lessonPlan = this.lessonPlanRepository.findOne(lp.getId());
            	
                Date startDate = new Date(this.lessonPlan.getStartDate().getTime() 
              		     + (scheduleEntryResizeEvent.getDayDelta() * 24 * 60 * 60 * 1000)
              		     + (scheduleEntryResizeEvent.getMinuteDelta() * 60 * 1000));
        
                this.lessonPlan.setStartDate(startDate);
                
                Date endDate = new Date(this.lessonPlan.getEndDate().getTime() 
             		     + (scheduleEntryResizeEvent.getDayDelta() * 24 * 60 * 60 * 1000)
             		     + (scheduleEntryResizeEvent.getMinuteDelta() * 60 * 1000));
                
                this.lessonPlan.setEndDate(endDate);
                
                LessonPlan retLessonPlan = this.lessonPlanRepository.saveAndFlush(this.lessonPlan);
                System.out.printf("Log ID is %d and for returned account ID is %d\n", lessonPlan.getId(), retLessonPlan.getId());
 
            	this.init();
            	
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, 
              		  "Event resized", "Day delta:" + scheduleEntryResizeEvent.getDayDelta() 
              		+ ", Minute delta:" + scheduleEntryResizeEvent.getMinuteDelta());
               
                addMessage(message);
            }
        }
 	
    }
     
    private void addMessage(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public LessonPlanRepository getLessonPlanRepository() {
        return lessonPlanRepository;
    }

    public void setLessonPlanRepository(LessonPlanRepository lessonPlanRepository) {
        this.lessonPlanRepository = lessonPlanRepository;
    }

    public ScheduleModel getEventModel() {
        return eventModel;
    }

    public void setEventModel(ScheduleModel eventModel) {
        this.eventModel = eventModel;
    }

    public LessonPlan getLessonPlan() {
        return lessonPlan;
    }

    public void setLessonPlan(LessonPlan lessonPlan) {
        this.lessonPlan = lessonPlan;
    }

    public List<LessonPlan> getLessonPlans() {
        return lessonPlans;
    }

    public void setLessonPlans(List<LessonPlan> lessonPlans) {
        this.lessonPlans = lessonPlans;
    }


    public Long getTeacherId() {
        return teacherId;
    }


    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }


    public Long getCourseId() {
        return courseId;
    }


    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }


    public Long getDegreeId() {
		return degreeId;
	}


	public void setDegreeId(Long degreeId) {
		this.degreeId = degreeId;
	}


	public boolean getNewEvent() {
		return newEvent;
	}


	public void setNewEvent(boolean newEvent) {
		this.newEvent = newEvent;
	}


	public Boolean getRenderedInit() {
        return renderedInit;
    }


    public void setRenderedInit(Boolean renderedInit) {
        this.renderedInit = renderedInit;
    }
  
    
   public Boolean getSummaryLock() {
		return summaryLock;
	}


	public void setSummaryLock(Boolean summaryLock) {
		this.summaryLock = summaryLock;
	}


    public void valueChangedDate (SelectEvent selectEvent) {
        Date date = (Date) selectEvent.getObject();
        
        System.out.println("Start Time : " + this.lessonPlan.getStartDate());
        System.out.println("End   Time : " + this.lessonPlan.getEndDate());
        System.out.println("Compare : " + (this.lessonPlan.getStartDate().getTime() >= 
                                           this.lessonPlan.getEndDate().getTime()));
        System.out.println("Message : " + date);
        
        if (this.lessonPlan.getStartDate().getTime() >= 
                this.lessonPlan.getEndDate().getTime()) {
 
        	String msg = getResourceProperty("labels", "lessonplan_change_dates");
            
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg );
            addMessage(message);
        }
    }    

    
    public void valueChangedTeacher (ValueChangeEvent valueChangeEvent) {
    	String msg = valueChangeEvent.getNewValue().toString();
    	
    	System.out.println("value changed..." + msg);
    }

    
	public void valueChangedCourse (ValueChangeEvent valueChangeEvent) {
        String msg = valueChangeEvent.getNewValue().toString();
        Long  courseId =  Long.parseLong(msg);
  
        Course course = this.courseRepository.findOne(courseId);
        
        this.lessonPlan.setTitle(course.getCode());

    }    
    
	
    public void valueChangedDegree (ValueChangeEvent valueChangeEvent) {
    	String msg = valueChangeEvent.getNewValue().toString();
    	
    	System.out.println("value changed..." + msg);
    }
    

    public void valueChangedNewEvent (ValueChangeEvent valueChangeEvent) {
    	String msg = valueChangeEvent.getNewValue().toString();
    	
    	System.out.println("value changed..." + msg);
    }
    

    public void valueChangedSchoolTimes (AjaxBehaviorEvent ajaxBehaviorEvent) {

    	System.out.println("value changed..." );
    	
    	// TODO
    	for (String s : this.mbSchoolTimesMBean.getSelectedSchoolTimes()) {
    		System.out.println("TE : " + s);
    		this.lessonPlan.setStartDate(new Date());
    	}
    }


    public String getResourceProperty(String resource, String label) {
        Application application = FacesContext.getCurrentInstance().getApplication();
        ResourceBundle bundle = application.getResourceBundle(FacesContext.getCurrentInstance(), resource);

        return bundle.getString(label);
    }

 
    
    
    public void onLoad () {
    	
    	this.lessonPlans = this.lessonPlanRepository.findAll();
    }
    

    public void delete() {
        if (this.selectedLessonPlan != null) {
            this.teacherRepository.delete(this.selectedLessonPlan.getId());
        }
    }

    
    public void selectLessonPlan(SelectEvent ev) {
        try {
            if (ev.getObject() != null) {
                this.selectedLessonPlan = (LessonPlan) ev.getObject();
                System.out.println("Passou");
            } else {
                this.selectedLessonPlan = null;
            }
        } catch (Exception e) {
            this.selectedLessonPlan = null;

            logger.error(e.getMessage(), e);
        }
    }

    public void valueChangedLocked (ValueChangeEvent e) {
        String msg = e.getNewValue().toString();
        
        System.out.println("Novo valor :  " + e.getNewValue().toString());
        System.out.println("Novo valor :  " + (Boolean) e.getNewValue());
        
        this.selectedLessonPlan.setLocked((Boolean) e.getNewValue());
       
        /* To test
        System.out.println("Novo valor :  " + e.getNewValue().toString());
        
        msg = teacher.getEMail().toString() + "  (" + userId.toString() + ")";
        
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null);
        FacesContext.getCurrentInstance().addMessage(null, message);
        */
    }
 
    
    public void unselectLessonPlan () {
        this.selectedLessonPlan = null;
    }
    
    
    public LessonPlan getSelectedLessonPlan() {
		return selectedLessonPlan;
	}


	public void setSelectedLessonPlan(LessonPlan selectedLessonPlan) {
		this.selectedLessonPlan = selectedLessonPlan;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


    public Boolean getDisableButtons() {
		return disableButtons;
	}


	public void setDisableButtons(Boolean disableButtons) {
		this.disableButtons = disableButtons;
	}


}
