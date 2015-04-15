package org.myproject.report;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.myproject.model.entities.Course;
import org.myproject.model.entities.Degree;
import org.myproject.model.repositories.CourseRepository;
import org.myproject.model.repositories.DegreeRepository;
import org.myproject.model.utils.ReportConfig;
import org.primefaces.event.SelectEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;


@Named(value = "jasperReportsMBean")
@Scope(value = WebApplicationContext.SCOPE_SESSION)
public class JasperReportsMBean extends AbstractBaseReportBean {

	@Inject
    private DegreeRepository degreeRepository;

	@Inject
    private CourseRepository courseRepository;

	private String COMPILE_FILE_NAME;

    private Map<String, Object> reportParameters = new HashMap<String, Object>();

    private Long courseId;

    private Long degreeId;

    private Date startDate;
    
    private Date endDate;
    
    
    public JasperReportsMBean() {
		super();
		
        this.setStartDate(new Date());
        
       
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 85);
        calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR));

        this.setEndDate(calendar.getTime());

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
        
        if (this.getStartDate().getTime() >= 
                this.getEndDate().getTime()) {
 
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