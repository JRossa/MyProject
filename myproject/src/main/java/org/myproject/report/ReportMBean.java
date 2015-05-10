package org.myproject.report;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;


import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;



import org.myproject.model.entities.Teacher;
import org.myproject.model.repositories.TeacherRepository;
import org.myproject.model.utils.BaseBean;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Scope(value = WebApplicationContext.SCOPE_SESSION)
@Named(value = "reportMBean")
public class ReportMBean extends BaseBean {

	private static final long serialVersionUID = 2218631568966050273L;

	@PersistenceContext
	EntityManager em;
	
	@Inject
    TeacherRepository teacherRepository;
	
	JasperPrint jasperPrint;

    public enum ExportOption {PDF, HTML, EXCEL, RTF}
    
	private final String COMPILE_DIR = "/reports";
   
  	private String message;

  	
  	
    public ReportMBean () {
        super();
        
        setExportOption(ExportOption.PDF);
    }
  	
    
    private ExportOption exportOption;
    
    public ExportOption getExportOption() {
		return exportOption;
	}

	public void setExportOption(ExportOption exportOption) {
		this.exportOption = exportOption;
	}
	

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void init() throws JRException{ 
		
		List <Teacher> teachers = teacherRepository.findAll();
		
//		for (Teacher t: teachers) {
//			System.out.println(t.getFullName());
//		}
		
		// • Remove the empty field descriptions from the JRXML.
		// • Set the field descriptions to match the bean property names.
		// • Pass false as isUseFieldDescription when creating the bean data source, e.g. new JRBeanCollectionDataSource(data, false).
		JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(teachers, false);  

		String  reportPath=  FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reports/listTest.jasper");       
		
		jasperPrint = JasperFillManager.fillReport(reportPath, new HashMap(), beanCollectionDataSource); 
	}
	
	public void pdf() throws JRException, IOException{  
		
		init();  
		
		HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();  
		httpServletResponse.addHeader("Content-disposition", "attachment; filename=teachers.pdf");  
		
		ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream(); 
		
		JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);  
		
		FacesContext.getCurrentInstance().responseComplete();  
}

}
