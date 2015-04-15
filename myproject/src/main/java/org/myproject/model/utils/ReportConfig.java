package org.myproject.model.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintWriter;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import javax.servlet.ServletContext;

import org.myproject.model.entities.Professorship;
import org.myproject.report.AbstractBaseReportBean.ExportOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.Exporter;
import net.sf.jasperreports.export.ExporterInput;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleHtmlReportConfiguration;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

public class ReportConfig {

	
	static Logger logger = LoggerFactory.getLogger(ReportConfig.class);
	
    /*
     * PRIVATE METHODS
     */

    private static void setCompileTempDir(ServletContext context, String uri) {
	System.setProperty("jasper.reports.compile.temp", context.getRealPath(uri));
    }

    
    /*
     * PUBLIC METHODS
     */
    public static boolean compileReport(ServletContext context, String reportsDir, String jasperReportsFileName,
	    								String jasperCompileFileName) throws JRException {

		System.out.println("File Name  : " + jasperCompileFileName);
	
		if (jasperCompileFileName != null) {
	
		    File jasperFile = new File(jasperCompileFileName);
	
		    // if jasper file already exists, do not compile again
		    if (jasperFile.exists()) {
			return true;
		    }
	
		    try {
				// jasper file has not been constructed yet, so compile the xml
				// file
				setCompileTempDir(context, reportsDir);
				String xmlFileName = jasperReportsFileName.substring(0,	jasperReportsFileName.indexOf(".jasper")) + ".jrxml";
		
				JasperCompileManager.compileReportToFile(xmlFileName, jasperReportsFileName);
		
				// This is only on server side
				Path moveFrom = FileSystems.getDefault().getPath(jasperReportsFileName);
				Path moveTo = FileSystems.getDefault().getPath(jasperCompileFileName);
				Files.move(moveFrom, moveTo, StandardCopyOption.REPLACE_EXISTING);
		
				return true;
		    } catch (Exception e) {
				e.printStackTrace();
				return false;
		    }
		}

		return false;
    }

    
    public static boolean deleteCompileReport (String jasperCompileFileName) {
    	
		System.out.println("File Name  : " + jasperCompileFileName);
		
		if (jasperCompileFileName != null) {
			
		    File jasperFile = new File(jasperCompileFileName);
	
		    // if jasper file already exists, do not compile again
		    if (jasperFile.exists()) {
		    	jasperFile.delete();
		    	return true;
		    }
		}
		
		return false;
    }
    

    public static JasperPrint fillReport(String compileFile, String strQuery, Map<String, Object> parameters,
	                                                                                    Connection connection) throws JRException {
		File file = new File(compileFile);
		parameters.put("BaseDir", file.getParentFile());
	
		
		try {
		    Statement statement = connection.createStatement();
		    ResultSet resultSet = statement.executeQuery(strQuery);
	
		    JRResultSetDataSource resultSetDataSource = new JRResultSetDataSource(resultSet);
	
		    JasperPrint jasperPrint = JasperFillManager.fillReport(compileFile, parameters, resultSetDataSource);
		    
		    resultSet.close();
		    statement.close();
	
		    return jasperPrint;
		} catch (SQLException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		    return null;
		}
	}

    
    public static JasperPrint fillReport(File reportFile, Map<String, Object> parameters, JRDataSource jrDataSource)
	    throws JRException {
		parameters.put("BaseDir", reportFile.getParentFile());
	
 
    	JasperPrint jasperPrint = JasperFillManager.fillReport(reportFile.getPath(), parameters, jrDataSource);
	
		return jasperPrint;
    }

 
    public static void listMap (Map<String, Object> parameters) {
 
		for (Map.Entry<String, Object> entry : parameters.entrySet()) {
		    String key = entry.getKey();
		    Object value = entry.getValue();
		    System.out.println("Key  : "  + key + "    -  value  : " + value);
		}		

    }
    
    public static JasperPrint fillReport(String compileFile, Map<String, Object> parameters, Connection connection)
	    throws JRException {
		File file = new File(compileFile);
		parameters.put("BaseDir", file.getParentFile());
	
		listMap(parameters);
		
		JasperPrint jasperPrint = JasperFillManager.fillReport(compileFile,	parameters, connection);
	
		return jasperPrint;
    }

    public static JasperPrint fillReport(String compileFile, Map<String, Object> parameters,
	                                                      JRResultSetDataSource resultSetDataSource) throws JRException {
		File file = new File(compileFile);
	
		parameters.put("BaseDir", file.getParentFile());
	
		JasperPrint jasperPrint = JasperFillManager.fillReport(compileFile, parameters, resultSetDataSource);
	
		return jasperPrint;
    }

    public static String getJasperFilePath(ServletContext context, String compileDir, String jasperFile) {
    	return context.getRealPath(compileDir + jasperFile);
    }

    
    private static void exportReport(JRAbstractExporter exporter, JasperPrint jasperPrint, PrintWriter out) throws JRException {
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_WRITER, out);
	
		exporter.exportReport();
    }

    
    public static void exportReportAsHtml(JasperPrint jasperPrint, PrintWriter out) throws JRException {
		JRHtmlExporter exporter = new JRHtmlExporter();
		
		exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN,	Boolean.FALSE);
//	<property name="net.sf.jasperreports.export.html.using.images.to.align" value="false"/>
		exporter.setParameter(JRHtmlExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
		exporter.setParameter(JRHtmlExporterParameter.CHARACTER_ENCODING, "ISO-8859-9");
	
		exportReport(exporter, jasperPrint, out);
	}
    
    
    public static void exportReportAsXls(JasperPrint jasperPrint, PrintWriter out) throws JRException {
        JRXlsxExporter exporter = new JRXlsxExporter();

        SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();

        exporter.setExporterInput((ExporterInput) jasperPrint);
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput("build/reports/BatchExportReport.xlsx"));
        
        configuration.setOnePagePerSheet(false);
        exporter.setConfiguration(configuration);

        exportReport(exporter, jasperPrint, out);

    }
    
    
   public static void exportReportGeneric(JasperPrint jasperPrint, PrintWriter outPW, ExportOption format) throws JRException {
       
//     JasperPrint jasperPrint = JasperFillManager.fillReport(compileFile, parameters, dataSource != null ? 
//     new JRMapArrayDataSource(dataSource) : 
//     new JREmptyDataSource());

       ByteArrayOutputStream out = new ByteArrayOutputStream();

       @SuppressWarnings("rawtypes")
       Exporter exporter = null;
       
       switch (format) {
       case PDF:
           exporter = new JRPdfExporter();
           exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
           break;
       case CSV:
           exporter = new JRCsvExporter();
           exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
           break;
       case XLSX:
           exporter = new JRXlsxExporter();
           exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
           
           SimpleXlsReportConfiguration xlsConfiguration = new SimpleXlsReportConfiguration();
           xlsConfiguration.setOnePagePerSheet(true);
           xlsConfiguration.setDetectCellType(true);
           xlsConfiguration.setCollapseRowSpan(false);
           
           exporter.setConfiguration(xlsConfiguration);
           break;
       case HTML:
           //The HtmlExport can not be configured with SimpleOutputStreamExporterOutput
           exporter = new HtmlExporter();
           exporter.setExporterOutput(new SimpleHtmlExporterOutput(out));
           
           SimpleHtmlReportConfiguration htmlConfiguration = new SimpleHtmlReportConfiguration();
          
           
           htmlConfiguration.setRemoveEmptySpaceBetweenRows(true);
           
           exporter.setConfiguration(htmlConfiguration);
           break;
       default:
//           throw new ReportException("Unknown export format");
       }
       
       exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
       
       exporter.exportReport();

   }
    
}


/*
 * 
 *

private static final Logger logger = LoggerFactory.getLogger(YOURCLASS.class);
JRDataSource dataSource = getDataSource(jsonData);//pass jsonData to populate the dataSource
JasperReport jasperReport = null;
JasperPrint jasperPrint = null;
//String type = Any of the types mentioned above
//jrxmlSource is the the jrxml generated using the iReport

Map<String, Object> parameters = new HashMap<String, Object>();
//Add any parameters that are referenced in the jrxml to this map

try {
jasperReport = JasperCompileManager.compileReport(jRXMLSource);
jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
} catch (JRException ex) {
ex.printStackTrace();
}

if ("pdf".equals(type)) {
JRPdfExporter exporter = new JRPdfExporter();
try {
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));
        exporter.exportReport();
} catch (IOException e) {
        logger.error("IOException occured", e);
        e.printStackTrace();
} catch (JRException e) {
        logger.error("JRException while exporting for pdf format", e);
        e.printStackTrace();
}

} else if ("xls".equals(type)) {

JRXlsExporter exporter = new JRXlsExporter();
try {
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));
        SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
        configuration.setOnePagePerSheet(true);
        exporter.setConfiguration(configuration);
        exporter.exportReport();
} catch (JRException e) {
        logger.error("JRException while exporting for xls format", e);
        e.printStackTrace();
} catch (IOException e) {
        logger.error("IOException occured", e);
        e.printStackTrace();
}

} else if ("csv".equals(type)) {
JRCsvExporter exporter = new JRCsvExporter();
try {
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleWriterExporterOutput(response.getOutputStream()));
        exporter.exportReport();
} catch (IOException e) {
        logger.error("IOException occured", e);
        e.printStackTrace();
} catch (JRException e) {
        logger.error("JRException while exporting report csv format", e);
        e.printStackTrace();
}
} else if ("html".equals(type)) {
request.getSession().setAttribute(ImageServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE,jasperPrint);

HtmlExporter exporterHTML = new HtmlExporter();
SimpleExporterInput exporterInput = new SimpleExporterInput(jasperPrint);
exporterHTML.setExporterInput(exporterInput);

SimpleHtmlExporterOutput exporterOutput;
try {
        exporterOutput = new SimpleHtmlExporterOutput(response.getOutputStream());
        exporterOutput.setImageHandler(new WebHtmlResourceHandler("image?image={0}"));
        exporterHTML.setExporterOutput(exporterOutput);
        
        SimpleHtmlReportConfiguration reportExportConfiguration = new SimpleHtmlReportConfiguration();
        reportExportConfiguration.setWhitePageBackground(false);
        reportExportConfiguration.setRemoveEmptySpaceBetweenRows(true);
        exporterHTML.setConfiguration(reportExportConfiguration);
        exporterHTML.exportReport();
} catch (IOException e) {
        logger.error("IOException occured", e);
        e.printStackTrace();
} catch (JRException e) {
        logger.error("JRException while exporting for html format", e);
        e.printStackTrace();
}
} else if ("docx".equals(type)) {
JRDocxExporter exporter = new JRDocxExporter();

try {
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));
        exporter.exportReport();
} catch (IOException e) {
        logger.error("IOException occured", e);
        e.printStackTrace();
} catch (JRException e) {
        logger.error("JRException while exporting for docx format", e);
        e.printStackTrace();
}
}


public JRDataSource getDataSource(String jsonData) {
logger.info("jsonData = " + jsonData);
JRDataSource dataSource = null;

if ("null".equals(jsonData) || jsonData == null || "".equals(jsonData)) {
        logger.info("jsonData parameter value is null. Creating JREmptyDataSource");
        dataSource = new JREmptyDataSource();
        return dataSource;
}

InputStream jsonInputStream = null;
try {
        // Convert the jsonData string to inputStream
        jsonInputStream = IOUtils.toInputStream(jsonData, "UTF-8");
        // selectExpression is based on the jsonData that your string contains
        dataSource = new JsonDataSource(jsonInputStream, "data");
} catch (IOException ex) {
        logger.error("Couldn't covert string into inputStream", ex);
        ex.printStackTrace();
} catch (JRException e) {
        logger.error("Couldn't create JsonDataSource", e);
        e.printStackTrace();
}

if (dataSource == null) {
        dataSource = new JREmptyDataSource();
        logger.info("dataSource is null. Request parameter jsondData is null");
}

return dataSource;
}

*/
