package org.myproject.report;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TimeZone;

import javax.annotation.PostConstruct;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;

import org.myproject.model.entities.Course;
import org.myproject.model.entities.Degree;
import org.myproject.model.entities.DegreeCurricularPlansData;
import org.myproject.model.repositories.CourseRepository;
import org.myproject.model.repositories.DegreeCurricularPlansDataRepository;
import org.myproject.model.repositories.DegreeRepository;
import org.myproject.model.utils.ReportConfig;
import org.primefaces.event.SelectEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;


@Named(value = "jasperReportsMBean")
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class JasperReportsMBean extends AbstractBaseReportBean {

	@Inject
    private DegreeRepository degreeRepository;

	@Inject
    private CourseRepository courseRepository;
	
	@Inject
	private DegreeCurricularPlansDataRepository degreeCurricularPlansDataRepository;
	

	private String COMPILE_FILE_NAME;

    private Map<String, Object> reportParameters = new HashMap<String, Object>();

    private Long courseId;

    private Long degreeId;

    private Date startDate;
    
    private Date endDate;
    
    
    public JasperReportsMBean() {
		super();
	
    	this.courseId = -1L;
    	this.degreeId = -1L;

        this.setStartDate(new Date());
       
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 85);
        calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR));

        this.setEndDate(calendar.getTime());

	}

    
    public void onLoad () {
    	
    	
    }


    public ExportOption getExportOption () {
		return super.getExportOption();
	}

	public void setExportOption (ExportOption exportOption) {
		super.setExportOption(exportOption);
	}

	@Override
    protected String getCompileFileName () {
        return COMPILE_FILE_NAME;
    }

    @Override
    protected void setCompileFileName (String compileFileName) {
        COMPILE_FILE_NAME = compileFileName;
    }

    @Override
    protected Map<String, Object> getReportParameters () {

        reportParameters.put("ReportTitle", "Hello JasperReports");

        return reportParameters;
    }

    @Override
    protected JRDataSource getJRDataSource () {
        // return your custom datasource implementation
        // JasperReportsDataSource dataSource = new JasperReportsDataSource();

        // return empty
        JRDataSource dataSource = new JREmptyDataSource();

        return dataSource;
    }

    public String execute () {
        try {
            this.setCompileFileName("listTest");
            super.setCompileOption(CompileOption.RS);

            super.setStrQuery("SELECT CATEGORY AS category, CONTRACT AS contract, FULL_NAME AS fullName, "
                    + "ID_NUMBER AS idNumber, MILITARY_SITUATION AS militarySituation FROM tbl_TEACHER;");

            super.prepareReport();

        } catch (Exception e) {
            // make your own exception handling
            e.printStackTrace();
        }

        return null;
    }

    
    public void deleteReport (String compileFileName) {
    	super.deleteCompileReport(compileFileName);
    	
    }
  
    
    public String executeListJoin () {
    	
        try {
            this.setCompileFileName("listJoin");
            super.setCompileOption(CompileOption.DB);

            super.prepareReport();

        } catch (Exception e) {
            // make your own exception handling
            e.printStackTrace();
        }

        return null;
    }

    
    private String computeExecutionYear(Date date) {
    	String executionYear = null;
    	
    	if (date == null) {
    		return "";
    	}
    	
        SimpleDateFormat yearDate = new SimpleDateFormat("yyyy");
        SimpleDateFormat mounthDate = new SimpleDateFormat("MM");

        Integer intMounth = Integer.parseInt(mounthDate.format(date).toString());
        Integer intYear = Integer.parseInt(yearDate.format(date).toString());
        
        if (intMounth >= 10) {
        	executionYear = intYear + "/" + (intYear+1);
        } else {
        	executionYear = (intYear-1) + "/" + intYear;
        }
    	
    	return executionYear;
    }
    
    
    public Date correctDayLight (Date date) {
    	
        TimeZone timezone = TimeZone.getTimeZone("Europe/Lisbon");
        Boolean dayLight = timezone.inDaylightTime(date);
        
        Long timeOffset = Integer.toUnsignedLong(timezone.getOffset(date.getTime()));

        Calendar calendar = Calendar.getInstance();
        
        calendar.setTime(date);
        
        calendar.setTimeZone(timezone);

        return calendar.getTime();
    }
    
    
    public String executeLessonPlan (String compileFileName) {
    	
    	
        try {
            this.setCompileFileName(compileFileName);
            
            super.setCompileOption(CompileOption.DB);

            Degree degree = this.degreeRepository.findOne(this.degreeId);
            
            if (degree != null) {
            	reportParameters.put("DEGREE_CODE", degree.getCode());
            }
            
            Course course = this.courseRepository.findOne(this.courseId);
            
            if (course != null) {
            	reportParameters.put("COURSE_CODE", course.getCode());
            }
            
            if (this.startDate != null) {
            	String strStartDate = new SimpleDateFormat("yyyy-MM-dd").format(this.getStartDate());
            	reportParameters.put("STR_START_DATE", strStartDate);
            	
            	reportParameters.put("EXECUTION_YEAR", this.computeExecutionYear(this.startDate));
            }
 
            if (this.endDate != null) {
           	String strEndDate = new SimpleDateFormat("yyyy-MM-dd").format(this.getEndDate());
            	reportParameters.put("STR_END_DATE", strEndDate);
            }

            
//            ReportConfig.listMap(reportParameters);
           
            super.prepareReport();

        } catch (Exception e) {
            // make your own exception handling
            e.printStackTrace();
        }

        return null;
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

    
    private void checkDegreeCurricularPlan (Long courseId, Long degreeId) {
    	
    	DegreeCurricularPlansData degreeCurricularPlan = 
    			this.degreeCurricularPlansDataRepository.findCurricularPlanByCourseAndDegree(courseId, degreeId);
    	
    	if (degreeCurricularPlan == null) {
    		String msg = getResourceProperty("labels", "lessonplan_degreecurricularplan_error");
    		
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, msg, msg );
            addMessage(message);
    	} else {
        	String curricularYear = getResourceProperty("labels", "lessonplan_degreecurricularplan_year");
        	String curricularSemester = getResourceProperty("labels", "lessonplan_degreecurricularplan_semester");

    		String msg = "UC do " + degreeCurricularPlan.getCurricularYear() + "º " + curricularYear + " / " +
    				         degreeCurricularPlan.getCurricularSemester() + "º " + curricularSemester;
    		
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg );
            addMessage(message);
    	}

    }
    
    
	public void valueChangedCourse (ValueChangeEvent valueChangeEvent) {
        String msg = valueChangeEvent.getNewValue().toString();
        Long  courseId = Long.parseLong(msg);
  
        Course course = this.courseRepository.findOne(courseId);
        
        System.out.println("value changed..." + course.getCode() + "   " + courseId + "   " + this.degreeId);
        
        if (this.degreeId > 0L && !this.degreeId.equals(28L)) {
        	this.checkDegreeCurricularPlan (courseId, this.degreeId);
       }
        
    }    
    
	
    public void valueChangedDegree (ValueChangeEvent valueChangeEvent) {
    	String msg = valueChangeEvent.getNewValue().toString();
    	Long degreeId = Long.parseLong(msg);
    	
    	System.out.println("value changed degree..." + msg + "   " + this.courseId);
    	
    	if (this.courseId > 0L && !this.courseId.equals(261L)) {
    		this.checkDegreeCurricularPlan (this.courseId, degreeId);
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