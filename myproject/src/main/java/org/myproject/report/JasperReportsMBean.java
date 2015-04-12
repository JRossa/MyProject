package org.myproject.report;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.myproject.report.AbstractBaseReportBean.ExportOption;
import org.primefaces.event.SelectEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;

@Named(value = "jasperReportsMBean")
@Scope(value = WebApplicationContext.SCOPE_SESSION)
public class JasperReportsMBean extends AbstractBaseReportBean {

	
    private String COMPILE_FILE_NAME;

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
        Map<String, Object> reportParameters = new HashMap<String, Object>();

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

    
    public String executeLessonPlan () {
        try {
            this.setCompileFileName("listLessonPlan");
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