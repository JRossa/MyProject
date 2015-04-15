package org.myproject.support.lessonplan;



import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.TimeZone;

import javax.annotation.PostConstruct;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

 
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.myproject.model.entities.Course;
import org.myproject.model.entities.Degree;
import org.myproject.model.entities.DegreeCurricularPlansData;
import org.myproject.model.entities.LessonPlan;
import org.myproject.model.entities.LogUser;
import org.myproject.model.entities.Teacher;
import org.myproject.model.repositories.CourseRepository;
import org.myproject.model.repositories.DegreeCurricularPlansDataRepository;
import org.myproject.model.repositories.DegreeRepository;
import org.myproject.model.repositories.LessonPlanRepository;
import org.myproject.model.repositories.TeacherRepository;
import org.myproject.model.repositories.UserRepository;
import org.myproject.model.utils.BaseBean;
import org.myproject.model.utils.Stamp;
import org.primefaces.context.RequestContext;
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
	
	private static final Long voidDegree = 28L;
	
	private static final Long voidCourse = 261L;

    @Inject
    private LessonPlanRepository lessonPlanRepository;

    @Inject
    private TeacherRepository teacherRepository;

    @Inject
    private CourseRepository courseRepository;

    @Inject
    private DegreeRepository degreeRepository;
    
    @Inject 
    DegreeCurricularPlansDataRepository degreeCurricularPlansDataRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private SchoolTimesMBean mbSchoolTimesMBean;

    private ScheduleModel eventModel;
    
    private LessonPlan lessonPlan;
  
    private List<LessonPlan> lessonPlans;

    private LessonPlan selectedLessonPlan;
    
    private Long id;
    

    private LogUser user;
    
    private Long teacherId;
    
    private Long courseId;

    private Long degreeId;
    
    private boolean newEvent;
    
	private Boolean checkError = false;
	
    private Integer numberOfWeeks;

    private Date initialStartDate;
    
    private Date initialEndDate;
    
    // lessonPlan - control buttons
    private Boolean renderedInit = true;
   
    private Boolean summaryLock = false;

    private Boolean renderedNumWeeks = false;
    
    private Boolean teacherSelected = false;
    
    private Boolean degreeSelected = false;

    private Boolean courseSelected = false;

    private Boolean enableTeacherSelect = false;
    
    // listLessonPlan - control buttons
    private Boolean renderedUpdate;

    private Boolean renderedDelete;


    private Boolean disableButtons;

    
    
    public LessonPlanMBean() {
		super();
		
		this.disableButtons = true;
		
        this.lessonPlan = new LessonPlan();
        this.lessonPlan.setStamp(new Stamp());
        this.lessonPlan.setTeacher(new Teacher());
        this.lessonPlan.setCourse(new Course());
        this.lessonPlan.setDegree(new Degree());
	}


	@PostConstruct
    public void init () {

    	// setup objects
        
        this.renderedInit = false;
        this.summaryLock = false;
        
        if (this.teacherSelected) {
        	if (this.courseSelected) {
        		if (this.degreeSelected) {
        			this.lessonPlans = this.lessonPlanRepository.findLessonPlanByTeacherAndCourseAndDegree(this.teacherId,
        					                                                                    this.courseId, this.degreeId);
        		} else {
        			this.lessonPlans = this.lessonPlanRepository.findLessonPlanByTeacherAndCourse(this.teacherId, this.courseId);
        		}
        	} else {
        		if (this.degreeSelected) {
        			this.lessonPlans = this.lessonPlanRepository.findLessonPlanByTeacherAndDegree(this.teacherId,
        					                                                                      this.degreeId);
        		} else {
        			this.lessonPlans = this.lessonPlanRepository.findLessonPlanByTeacher(this.teacherId);
        		}
        	}
        // not teacher	
        } else {
        	if (this.courseSelected) {
        		if (this.degreeSelected) {
        			this.lessonPlans = this.lessonPlanRepository.findLessonPlanByCourseAndDegree(this.courseId, this.degreeId);
        		} else {
        			this.lessonPlans = this.lessonPlanRepository.findLessonPlanByCourse(this.courseId);
        		}
        	} else {
        		if (this.degreeSelected) {
        			this.lessonPlans = this.lessonPlanRepository.findLessonPlanByDegree(this.degreeId);
        		} else {
        			this.lessonPlans = this.lessonPlanRepository.findAll();
        		}
        	}
        }
		

        
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

            if (lp.getDegree().getId() != null) {
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
	            	case 28:
	            		ev.setStyleClass("proj");
	            		break;
	            	default:	
	            		ev.setStyleClass("armas");
	            		
	            }
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
                
                Boolean locked = this.lessonPlan.getLocked();
                
//                if (this.lessonPlan != null) {
//                	System.out.println("LOcked  " + locked);
//                	System.out.println("End date  " + this.lessonPlan.getEndDate());
//                }
                
                if (locked == null || locked == true) {
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
 
    
    private void setupUser () {
    	  
        String username = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username");

        this.user = this.userRepository.findByUserName(username);
        
        this.setSummaryLock(false);
        this.mbSchoolTimesMBean.setSelectedSchoolTimes(null);
        
        if (this.user.getTeacher().getId() != null) {
        	
            
        	if ((this.user.getLogRole().getRolename().equals("ROLE_ADMIN")) || 
        			(this.user.getLogRole().getRolename().equals("ROLE_USER_U")) ||
        			(this.user.getLogRole().getRolename().equals("ROLE_USER_T"))) {
        		this.setRenderedNumWeeks(true);
        		this.setEnableTeacherSelect(true);
        	}
        	else {
        		this.setRenderedNumWeeks(false);
        		this.setEnableTeacherSelect(false);
        	}
            
        	this.setTeacherId(this.user.getTeacher().getId());
        	if (!this.getEnableTeacherSelect()) {
        		this.convertTeacherId();
        	}
        	
            this.setCourseId(-1L);
            this.setDegreeId(-1L);
        }
        else {
        	this.setRenderedNumWeeks(false);
        	this.setTeacherId(-1L);
            this.setCourseId(-1L);
            this.setDegreeId(-1L);
        }

    }
    
    
   public void onDateSelect(SelectEvent selectEvent) {
        ScheduleEvent event = new DefaultScheduleEvent("", (Date) selectEvent.getObject(), (Date) selectEvent.getObject());

        this.lessonPlan = new LessonPlan();

        this.setupUser();
        
//        this.teacherId = 173L;
//        this.courseId = 261L;
//        this.degreeId = 1L;
        
        this.lessonPlan.setStartDate(new Date(event.getStartDate().getTime()));
        this.lessonPlan.setEndDate(new Date(event.getEndDate().getTime() + (55 * 60 * 1000)));
        
        this.initialStartDate =  this.lessonPlan.getStartDate();
        this.initialEndDate =  this.lessonPlan.getEndDate();
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
            
            if (course == null) {
            	course = new Course();
            	course.setId(261L);
            }
            
           	System.out.println(course.getName());
           	this.lessonPlan.setCourse(course);
        }
    }
    
    
    public void convertDegreeId () {
        
        if (this.getDegreeId() != null) {
            Degree degree = this.degreeRepository.findOne(this.getDegreeId());
            
            if (degree == null) {
            	degree = new Degree();
            	degree.setId(28L);
            } 
            
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
 
    	if (this.lessonPlan.getTitle() == null) {
            String msg = getResourceProperty("labels", "lessonplan_title_null");

            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, msg, msg );
            addMessage(message);
            return;
    	}
    	
    	if (this.lessonPlan.getPlace() == null) {
            String msg = getResourceProperty("labels", "lessonplan_place_null");

            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, msg, msg );
            addMessage(message);
            return;
    	}

    	Stamp stamp = new Stamp();
    	
        this.convertTeacherId();
        this.convertCourseId();
        this.convertDegreeId();
        
        String username = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username");

        stamp.setModificationUser(username);
        stamp.setModificationDate(new Date());
        this.lessonPlan.setStamp(stamp);


        if (this.getNewEvent() == true) {
        	this.lessonPlan.setId(null);
        }

       
        if (this.lessonPlan.getId() == null) {
            if (this.lessonPlan.getStartDate().getTime() <= 
                       this.lessonPlan.getEndDate().getTime()) {

                stamp.setCreationUser(username);
                stamp.setCreationDate(new Date());
                this.lessonPlan.setStamp(stamp);

                this.lessonPlan.setLocked(true);
                
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


    public void addSemester() {
    	String msg = "Sumário da aula do dia ";
    	
    	this.addEvent();
    	
    	// TODO    	
    	for (int i = 1; i < this.getNumberOfWeeks(); i++) {
    		
    		this.lessonPlan.setId(null);
    		this.lessonPlan.setStartDate(new Date(this.lessonPlan.getStartDate().getTime() + (7 * 24 * 60 * 60 * 1000)));
    		this.lessonPlan.setEndDate(new Date(this.lessonPlan.getEndDate().getTime() + (7 * 24 * 60 * 60 * 1000)));

    		String description = msg + this.lessonPlan.getStartDate(); 
    		this.lessonPlan.setDescription(description);
   		
    		this.addEvent();
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
    	
    	this.setLessonPlan(new LessonPlan());

    	this.setupUser();
    	
        this.lessonPlan.setStartDate(new Date());
        
        this.mbSchoolTimesMBean.setSelectedSchoolTimes(null);
    	this.setSummaryLock(false);
       
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE));
        calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR) + 1);

        this.lessonPlan.setEndDate(calendar.getTime());
        
        this.initialStartDate =  this.lessonPlan.getStartDate();
        this.initialEndDate =  this.lessonPlan.getEndDate();

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
            
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, msg, msg );
            addMessage(message);
        }
    }    

    
    public void valueChangedTeacher (ValueChangeEvent valueChangeEvent) {
    	String msg = valueChangeEvent.getNewValue().toString();
    	
    	System.out.println("value changed..." + msg);
    }

    
    private void checkDegreeCurricularPlan (Long courseId, Long degreeId) {

    	DegreeCurricularPlansData degreeCurricularPlan = 
    			this.degreeCurricularPlansDataRepository.findCurricularPlanByCourseAndDegree(courseId, degreeId);
    	
    	if (degreeCurricularPlan == null) {
    		String msg = getResourceProperty("labels", "lessonplan_degreecurricularplan_error");
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, msg, msg );
            addMessage(message);
    	} else {
    		String msg = "UC do " + degreeCurricularPlan.getCurricularYear() + "º ano / " +
    				         degreeCurricularPlan.getCurricularSemester() + "º semestre";
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg );
            addMessage(message);
    	}

    }
    
    
	public void valueChangedCourse (ValueChangeEvent valueChangeEvent) {
        String msg = valueChangeEvent.getNewValue().toString();
        Long  courseId = Long.parseLong(msg);
  
        Course course = this.courseRepository.findOne(courseId);
        
        System.out.println("value changed..." + course.getCode() + "   " + courseId);
        
        if (this.degreeId > 0L && !this.degreeId.equals(28L)) {
        	this.checkDegreeCurricularPlan (courseId, this.degreeId);
       }
        
        this.lessonPlan.setTitle(course.getCode());

    }    
    
	
    public void valueChangedDegree (ValueChangeEvent valueChangeEvent) {
    	String msg = valueChangeEvent.getNewValue().toString();
    	Long degreeId = Long.parseLong(msg);
    	
    	System.out.println("value changed degree..." + msg);
    	
    	if (this.courseId > 0L && !this.courseId.equals(261L)) {
    		this.checkDegreeCurricularPlan (this.courseId, degreeId);
    	}
    }
    

    public void valueChangedNewEvent (ValueChangeEvent valueChangeEvent) {
    	String msg = valueChangeEvent.getNewValue().toString();
    	
    	System.out.println("value changed..." + msg);
    }
    
    
    public void valueChangedNumWeeks (ValueChangeEvent valueChangeEvent) {
    	String msg = valueChangeEvent.getNewValue().toString();
    	
    	System.out.println("value changed..." + msg);
    }
    

    public void valueChangedSchoolTimes (AjaxBehaviorEvent ajaxBehaviorEvent) {
    	System.out.println("value changed..." );
    	
    	Boolean selectedST = false;
    	
    	Date stDate = null;
    	Date startDate = null;
    	Date endDate   = null;
    	
    	Boolean first = false;
    	Boolean second = false;
    	Boolean third = false;
    	Boolean fourth = false;
    	Boolean fifth = false;
    	Boolean sixth = false;
    	Boolean seventh = false;
  	
        TimeZone timezone = TimeZone.getTimeZone("Europe/Lisbon");
        Boolean dayLight = timezone.inDaylightTime(this.lessonPlan.getStartDate());
        
        Long timeOffset = dayLight ? timezone.getOffset(System.currentTimeMillis()) : 0L;
        
        Calendar calendar = Calendar.getInstance();
        
        System.out.println("Start Date :" + this.lessonPlan.getStartDate().getDate());
        System.out.println("Start Day  :" + this.lessonPlan.getStartDate().getDay());
        System.out.println("Day Light  :" + dayLight + "   Offset  " + timeOffset);
       
        calendar.setTime(this.lessonPlan.getStartDate());
        
        calendar.setTimeZone(timezone);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE),
        		      0, 0, 0);

    	this.checkError = false;
    	
    	for (String s : this.mbSchoolTimesMBean.getSelectedSchoolTimes()) {
    		if (s.toString().contains("1º")) {
    	        calendar.set(Calendar.HOUR_OF_DAY, 8);
    	        calendar.setTimeInMillis(calendar.getTimeInMillis() + timeOffset);
    	        calendar.set(Calendar.MINUTE, 00);
    	        
                startDate = calendar.getTime();
                endDate = new Date(startDate.getTime() + (55 * 60 * 1000));
       		
        		first = true;
        		selectedST = true;
    		}
    		
    		if (s.toString().contains("2º")) {
    	        calendar.set(Calendar.HOUR_OF_DAY, 9);
    	        calendar.setTimeInMillis(calendar.getTimeInMillis() + timeOffset);
    	        calendar.set(Calendar.MINUTE, 05);
    	        stDate = calendar.getTime();
    	        
    			if (startDate == null) {
            		startDate = stDate;
    			}
        		
        		endDate = new Date(stDate.getTime() + (55 * 60 * 1000));
        		
    			second = true;
        		selectedST = true;
    		}
    		
    		if (s.toString().contains("3º")) {
    	        calendar.set(Calendar.HOUR_OF_DAY, 10);
    	        calendar.setTimeInMillis(calendar.getTimeInMillis() + timeOffset);
    	        calendar.set(Calendar.MINUTE, 20);
    	        stDate = calendar.getTime();
    	        
    	        if (selectedST && !second) {
    	        	this.checkError = true;
    	        	break;
    	        }
    	        
    			if (startDate == null) {
            		startDate = stDate;
    			}

    	        endDate = new Date(stDate.getTime() + (55 * 60 * 1000));
    			
    	        third = true;
        		selectedST = true;
    		}
    		
    		if (s.toString().contains("4º") && (checkError == false)) {
    	        calendar.set(Calendar.HOUR_OF_DAY, 11);
    	        calendar.setTimeInMillis(calendar.getTimeInMillis() + timeOffset);
    	        calendar.set(Calendar.MINUTE, 25);
    	        stDate = calendar.getTime();
    	        
    	        if (selectedST && !third) {
    	        	this.checkError = true;
    	        	break;
    	        }
    	        
    			if (startDate == null)  {
            		startDate = stDate;
    			}

    	        endDate = new Date(stDate.getTime() + (55 * 60 * 1000));
    			
    			fourth = true;
        		selectedST = true;
    		}

    		if (s.toString().contains("5º") && (checkError == false)) {
    	        calendar.set(Calendar.HOUR_OF_DAY, 14);
    	        calendar.setTimeInMillis(calendar.getTimeInMillis() + timeOffset);
    	        calendar.set(Calendar.MINUTE, 00);
    	        stDate = calendar.getTime();
    	        
    	        if (selectedST && !fourth) {
    	        	this.checkError = true;
    	        	break;
    	        }
    	        
    			if (startDate == null)  {
            		startDate = stDate;
    			}

    	        endDate = new Date(stDate.getTime() + (55 * 60 * 1000));
    			
    	        fifth = true;
        		selectedST = true;
    		}

    		if (s.toString().contains("6º") && (checkError == false)) {
    	        calendar.set(Calendar.HOUR_OF_DAY, 15);
    	        calendar.setTimeInMillis(calendar.getTimeInMillis() + timeOffset);
    	        calendar.set(Calendar.MINUTE, 05);
    	        stDate = calendar.getTime();
    	        
    	        if (selectedST && !fifth) {
    	        	this.checkError = true;
    	        	break;
    	        }
    	        
    			if (startDate == null)  {
            		startDate = stDate;
    			}

    	        endDate = new Date(stDate.getTime() + (55 * 60 * 1000));
    			
    	        sixth = true;
        		selectedST = true;
    		}
    	
    		if (s.toString().contains("7º") && (checkError == false)) {
    	        calendar.set(Calendar.HOUR_OF_DAY, 16);
    	        calendar.setTimeInMillis(calendar.getTimeInMillis() + timeOffset);
    	        calendar.set(Calendar.MINUTE, 10);
    	        stDate = calendar.getTime();
    	        
       	        if (selectedST && !sixth) {
    	        	this.checkError = true;
    	        	break;
    	        }
    	        
    			if (startDate == null)  {
            		startDate = stDate;
    			}

    	        endDate = new Date(stDate.getTime() + (55 * 60 * 1000));

    	        seventh = true;
        		selectedST = true;
    		}
    	}

		if (this.checkError == true) {
			System.out.println("Error");
            String msg = getResourceProperty("labels", "lessonplan_schooltimes_error");

            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, msg, msg );
            addMessage(message);

		} else {
			System.out.println("Mudou");

			if (startDate != null) {
				this.lessonPlan.setStartDate(startDate);
			} else {
				this.lessonPlan.setStartDate(this.initialStartDate);
			}
			
			if (endDate != null) {
				this.lessonPlan.setEndDate(endDate);
			} else {
				this.lessonPlan.setEndDate(this.initialEndDate);
			}
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


	public Boolean getRenderedUpdate() {
		return renderedUpdate;
	}


	public void setRenderedUpdate(Boolean renderedUpdate) {
		this.renderedUpdate = renderedUpdate;
	}


	public Boolean getRenderedDelete() {
		return renderedDelete;
	}


	public void setRenderedDelete(Boolean renderedDelete) {
		this.renderedDelete = renderedDelete;
	}


	public Boolean getCheckError() {
		return checkError;
	}


	public void setCheckError(Boolean checkError) {
		this.checkError = checkError;
	}


	public Integer getNumberOfWeeks() {
		return numberOfWeeks;
	}


	public void setNumberOfWeeks(Integer numberOfWeeks) {
		this.numberOfWeeks = numberOfWeeks;
	}


	public Boolean getRenderedNumWeeks() {
		return renderedNumWeeks;
	}


	public void setRenderedNumWeeks(Boolean renderedNumWeeks) {
		this.renderedNumWeeks = renderedNumWeeks;
	}


	public Boolean getTeacherSelected() {
		return teacherSelected;
	}


	public void setTeacherSelected(Boolean teacherSelected) {
		this.teacherSelected = teacherSelected;
	}


	public Boolean getDegreeSelected() {
		return degreeSelected;
	}


	public void setDegreeSelected(Boolean degreeSelected) {
		this.degreeSelected = degreeSelected;
	}


	public Boolean getCourseSelected() {
		return courseSelected;
	}


	public void setCourseSelected(Boolean courseSelected) {
		this.courseSelected = courseSelected;
	}


	public Boolean getEnableTeacherSelect() {
		return enableTeacherSelect;
	}


	public void setEnableTeacherSelect(Boolean enableTeacherSelect) {
		this.enableTeacherSelect = enableTeacherSelect;
	}
	
	
}
