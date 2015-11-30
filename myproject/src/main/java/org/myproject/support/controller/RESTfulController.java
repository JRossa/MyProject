package org.myproject.support.controller;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.json.JSONArray;
import org.json.XML;
import org.myproject.model.utils.BaseBean;
import org.myproject.webservice.SOAPClient;
import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

//RESTful Servlet Test
// @Controller -> @ResponseBody <=> @RestController
@RestController
@RequestMapping(value = { "/rest" })
public class RESTfulController {

	
	 @RequestMapping(value = "/hello/{name}", method = RequestMethod.GET)
	 public @ResponseBody String getGreeting(@PathVariable String name) {
		 String result="Hello "+name; 
	
		 return result;
	
	 }
	

    
    @ResponseBody   
    @RequestMapping("/add")  // rest/?num1=55&num2=45  @Path("/add/{a}/{b}")
    public String addTwo(@RequestParam("num1") int num1, @RequestParam("num2") int num2) {
         
	    System.out.println("num1 :" + num1 + "   num2 :" + num2);
	    
   	return num1 + num2 + "";
    
    }
 

    private List<AgencyResource> getListing() {

        List<AgencyResource> resources = new ArrayList<>();
        resources.add(new AgencyResource(1, "All State", "123"));
        resources.add(new AgencyResource(2, "FCCI Insurance Group", "456"));
        resources.add(new AgencyResource(3, "Farmers", "789"));
        resources.add(new AgencyResource(4, "Met life", "167"));

        return resources;
    }
    
    
    
    @RequestMapping(value = "/agencies",  method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<AgencyResource> getAgencies() {

    	System.out.println("In");
    	ObjectMapper mapper = new ObjectMapper();
        List<AgencyResource> agencies = getListing();
       
		String jsonInString = "";

		try {
			//Convert object to JSON string
			jsonInString = mapper.writeValueAsString(agencies);
			System.out.println(jsonInString);
			
			//Convert object to JSON string and pretty print
			jsonInString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(agencies);
			System.out.println(jsonInString);
			
//			JSONObject o = new JSONObject(jsonInString);
			JSONArray o = new JSONArray(jsonInString);
			String xml = org.json.XML.toString(o);

			SOAPClient soapClient = new SOAPClient();
			System.out.println(soapClient.prettyPrintXMLAsString(xml, 4));
			
	 	} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     
        return agencies;

    }
	
}
