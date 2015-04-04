package org.myproject.report.servlet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.myproject.dao.Database;

import java.sql.Connection;
import java.sql.SQLException;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

public class ReportSendToBrowserServlet extends HttpServlet {

	private static final long serialVersionUID = 7122695808926587451L;

	private final String COMPILE_FILE_NAME = "listJoin";
	private final String REPORTS_DIR = "/reports/";

	//PRINT PDF ON BROWSER CALLING SERVLET  
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		ServletOutputStream servletOutputStream = response.getOutputStream();

   

		ServletContext  context = getServletContext();
		System.out.println("Servlet Context :  " + context.getRealPath("/"));
		
 		String jasperReportsFileName = context.getRealPath("/") + getReportsDir() + getCompileFileName() + ".jrxml";
 		
 		//File jasper source
		String jasperCompileFileName = context.getRealPath("/") + getReportsDir() + getCompileFileName() + ".jasper";
		//File pdf  destination
		String pdfDestinationFileName = context.getRealPath("/") + getReportsDir() + getCompileFileName() + ".pdf";
		
		File reportFile = new File(jasperCompileFileName);
		
		// jasper file already exists, do not compile again
		if (!reportFile.exists()) {
			if (!compileReport(jasperReportsFileName, jasperCompileFileName))
				return;
		} 

			
		if (printPdfReport(jasperCompileFileName, pdfDestinationFileName)) {
		  System.out.println("Report created from an existing file jasper");
		}

		//Passing parameters to the jasper file.
		Map parameters = new HashMap();
		//pass predefined (with iReport) params 
		//parameters.put("paramName", new String(param1));
		
		byte[] bytes = null;
		// Method that generate and return PDF as byte[]
		// byte[] doc = getDocument();  
		// response.setContentLength(doc.length); 
		

		try {
			
			bytes = JasperRunManager.runReportToPdf(reportFile.getPath(), parameters, Database.getConnection());

			response.setContentType( "application/pdf" );
			// Change from "attachment" to "inline" for opening in browser (attachment should download directly)
			// response.setHeader("Content-Disposition", "attachment;filename=document.pdf");  
			// response.setContentLength(doc.length);
			
		    response.setContentLength(bytes.length);
		      
		    servletOutputStream.write(bytes, 0, bytes.length);
		    servletOutputStream.flush();
		    servletOutputStream.close();

			//ServletOutputStreamout = response.getOutputStream();
			//out.write(receipt);
			//out.flush();

		} catch (JRException e) {
			// display stack trace in the browser
			StringWriter stringWriter = new StringWriter();
			PrintWriter printWriter = new PrintWriter(stringWriter);
			e.printStackTrace(printWriter);
			response.setContentType("text/plain");
			response.getOutputStream().print(stringWriter.toString());
		}
	}

	
	public static boolean compileReport(String jasperReportsFileName, String jasperCompileFileName) {
		try {
			
			Connection connection = Database.getConnection();
			System.out.println("Done with connectToDatabase!");

  	        JasperDesign jasperDesign = JRXmlLoader.load(jasperReportsFileName);
  	        System.out.println("Done with load!");
  	        
			System.out.println("Compiling report...");
  	        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
  	        JasperCompileManager.compileReportToFile(jasperDesign, jasperCompileFileName);

			System.out.println("Done!");			

	        JRParameter[] jrParameters = jasperReport.getParameters();
		    for (JRParameter param : jrParameters) {
		      System.out.println("Parameter : " + param.getName());
		      System.out.println("    Class Name: " + param.getValueClassName());
		      System.out.println("    isSystemDefined: " + param.isSystemDefined());
		    }
		      
		    HashMap jasperParameter = new HashMap();

		    // BigDecimal cutoffAmt = new BigDecimal("5000.00");
		    // jasperParameter.put("CutoffAmt",cutoffAmt);
		       
		    //JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, jasperParameter, connection);
		    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperCompileFileName, jasperParameter, connection);
		    System.out.println("Done with fillReport!");
		    Database.close(connection);
/*		      
		    JRPdfExporter export = new JRPdfExporter();
		    export.setParameter(JRExporterParameter.JASPER_PRINT,jasperPrint);
		    export.setParameter(JRExporterParameter.OUTPUT_FILE_NAME,"output.pdf");
		    System.out.println("Done with setJasperPrint!");
		    export.exportReport();
		    System.out.println("Done with exportReport!");
*/		      
			return true;
		} catch (JRException e) {
			e.printStackTrace();

			return false;
		}
	}

	//PRINT PDF ON FILESYSTEM CALLING SERVLET
	public static boolean printPdfReport(String jasperReportsFileName, String pdfDestinationFileName) {
		
	
		try	{
			//File jasper source
			String fileName = jasperReportsFileName;
	 
			//File pdf  destination
			String destFileNamePdf = pdfDestinationFileName;
	 
			//Passing parameters to the jasper file.
			Map parameters = new HashMap();
	 
			//parameters.put("paramName", new String(param1));//pass predefined (with iReport) params
			//Preparing the file to compile 
			//getConnection() returns your db connection
	 
			JasperPrint jasperPrint=JasperFillManager.fillReport(fileName, parameters, Database.getConnection());
	 
	 
			//Creation PDF file
			JasperExportManager.exportReportToPdfFile(jasperPrint, destFileNamePdf); 

			return true;
		} catch (JRException e) {
			e.printStackTrace();
		}
		
	return false;	
	}

    
	protected String getCompileFileName() {
		return COMPILE_FILE_NAME;
	}

	protected String getReportsDir() {
		return REPORTS_DIR;
	}

}
