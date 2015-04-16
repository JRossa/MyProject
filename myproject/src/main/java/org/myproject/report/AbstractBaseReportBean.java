package org.myproject.report;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.myproject.dao.Database;
import org.myproject.model.utils.ReportConfig;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.j2ee.servlets.BaseHttpServlet;



public abstract class AbstractBaseReportBean {
	
  public enum ExportOption {FILE, PDF, HTML, EXCEL, CSV, XLSX, RTF}
  
  public enum CompileOption {DS, RS, DB, NONE}
  
  private ExportOption exportOption;
  private CompileOption compileOption;
  
  private String strQuery;
  
  private String xmlFileName;
  private String jasperReportsFileName;
  private String jasperCompileFileName;

  
  private final String REPORTS_DIR = "/reports/";
  private final String COMPILE_DIR = "/reports/compile/";


  
  protected abstract JRDataSource getJRDataSource();
  protected abstract String getCompileFileName();
  protected abstract void setCompileFileName(String compileFileName);


  
  public AbstractBaseReportBean() {
    super();
    
    setExportOption(ExportOption.PDF);
  }

  protected void prepareReport() throws JRException, IOException {
	  
    ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
    
    ServletContext context = (ServletContext) externalContext.getContext();
    HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
    HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
    
    setXmlFileName(context.getRealPath("/") + getReportsDir() + getCompileFileName() + ".jrxml");
    setJasperReportsFileName(context.getRealPath("/") + getReportsDir() + getCompileFileName() + ".jasper");
    setJasperCompileFileName(context.getRealPath("/") + getCompileDir() + getCompileFileName() + ".jasper");
    
    ReportConfig.compileReport(context, getReportsDir(), getJasperReportsFileName(), getJasperCompileFileName());
    
    File reportFile = new File(getJasperCompileFileName());
    
    JasperPrint jasperPrint = null;
    
    if (getCompileOption().equals(CompileOption.DB)) {
      Connection connection = Database.getConnection();
      
      jasperPrint = ReportConfig.fillReport(jasperCompileFileName, getReportParameters(), connection); 
      Database.close(connection);
    } else {
    	if (getCompileOption().equals(CompileOption.RS)) {
    	      Connection connection = Database.getConnection();
    	      jasperPrint = ReportConfig.fillReport(jasperCompileFileName, getStrQuery(), getReportParameters(), connection);
    	      Database.close(connection);
    	} else {
    		 jasperPrint = ReportConfig.fillReport(reportFile, getReportParameters(), getJRDataSource());
    	}
    }
      
    if (getExportOption().equals(ExportOption.HTML)) {
      ReportConfig.exportReportAsHtml(jasperPrint, response.getWriter());
    } else { 
    	if (getExportOption().equals(ExportOption.FILE)) { 
    		response.addHeader("Content-disposition", "attachment; filename=FileName.pdf");
    		ServletOutputStream servletOutputStream = response.getOutputStream(); 
    		JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
    	} else {
    		request.getSession().setAttribute(BaseHttpServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE, jasperPrint);    
    		response.sendRedirect(request.getContextPath()+"/servlets/report/"+getExportOption());
    	}
    }
    
    FacesContext.getCurrentInstance().responseComplete();
   
  }

  
  public boolean deleteCompileReport (String jasperCompileFileName) {
	  ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
//	  ServletContext context = (ServletContext) externalContext.getContext();
	  
	  System.out.println("Response  " + externalContext.getResponse().equals("title"));
	  
	  setJasperCompileFileName(externalContext.getRealPath("/") + getCompileDir() + jasperCompileFileName + ".jasper");
	  
	  return ReportConfig.deleteCompileReport(getJasperCompileFileName());
  }
 
  
  public ExportOption getExportOption() {
    return exportOption;
  }

  public void setExportOption(ExportOption exportOption) {
    this.exportOption = exportOption;
  }

  
  public CompileOption getCompileOption() {
	return compileOption;
  }
  
  public void setCompileOption(CompileOption compileOption) {
	this.compileOption = compileOption;
  }
  
  
  protected Map<String, Object> getReportParameters() {
	  Map<String, Object> reportParameters = new HashMap<String, Object>();
	  
    //parameterMap.put("hp", new Integer(750));
    //<parameter name="hp" class="java.lang.Integer" />
    //parameterMap.put("logoPath", "\\logo.jpg");
    //parameterMap.put("companyName", "Company Name");
    //parameterMap.put("REPORT_FILE_RESOLVER", fileResolver);
	  
    //$P{hp}
	  reportParameters.put("DEGREE_CODE", "AMIL");
	  reportParameters.put("COURSE_CODE", "N113");
  
    return reportParameters;
  }

  public void setReportParameter(String paramKey, String paramValue) {
	  
	
  }
  
  
  protected String getReportsDir() {
	    return REPORTS_DIR;
	  }
  
  protected String getCompileDir() {
    return COMPILE_DIR;
  }
  
  public String getJasperReportsFileName() {
	return jasperReportsFileName;
  }
  
  public void setJasperReportsFileName(String jasperReportsFileName) {
	this.jasperReportsFileName = jasperReportsFileName;
  }
  
  public String getJasperCompileFileName() {
	return jasperCompileFileName;
  }
  
  public void setJasperCompileFileName(String jasperCompileFileName) {
	this.jasperCompileFileName = jasperCompileFileName;
  }
  
  public String getXmlFileName() {
	return xmlFileName;
  }
  
  public void setXmlFileName(String xmlFileName) {
	this.xmlFileName = xmlFileName;
  }
  
  public String getStrQuery() {
	return strQuery;
  }
  
  public void setStrQuery(String strQuery) {
	this.strQuery = strQuery;
  }
  

} 