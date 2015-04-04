package org.myproject.report;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Named;

import org.myproject.report.AbstractBaseReportBean.ExportOption;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;

@Named(value = "jasperReportsMBean")
@Scope(value = WebApplicationContext.SCOPE_SESSION)
public class JasperReportsMBean extends AbstractBaseReportBean {

	
    private String COMPILE_FILE_NAME;

    
    
    public JasperReportsMBean() {
		super();
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

}