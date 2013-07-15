package it.polito.dp2.PJS.sol6.service;



import it.polito.dp2.PJS.sol6.service.PJSMasterImpl;
import it.polito.dp2.PJS.sol6.service.PJSSubmissionPortImpl;

import it.polito.dp2.PJS.sol6.service.ServiceManager;

import java.io.FileNotFoundException;
import java.util.concurrent.Executors;

import javax.xml.ws.Endpoint;
import javax.xml.ws.http.HTTPBinding;

public class PJSMasterServer {
	
	
	public static void main(String args[]){
		
		
		//publish xsd file
		String xsdFilename="META-INF/PJSSchema.xsd";
		String xsdURL="http://localhost:8182/PJSSchema.xsd";
		
		Endpoint e;
				
		
		
		try{
			e=Endpoint.create(HTTPBinding.HTTP_BINDING, new XmlFileProvider(xsdFilename));
			e.publish(xsdURL);
	
		}catch(FileNotFoundException fnf){
			System.out.println("Unable to open xsd file");
			System.exit(1);
		}
		
		System.out.println("XSD published http://localhost:8182/PJSSchema.xsd");
		
		Endpoint endpoint = Endpoint.create(new PJSMasterImpl());
		Endpoint endpoint2 = Endpoint.create(new PJSSubmissionPortImpl());
		
		
		
		System.out.println("http://localhost:8182/PJSMaster");
		endpoint.setExecutor(Executors.newFixedThreadPool(10));
      	endpoint.publish("http://localhost:8182/PJSMaster");
       
      	
      	System.out.println("http://localhost:8182/PJSJobSubmission");
       	endpoint2.setExecutor(Executors.newFixedThreadPool(10));
      	endpoint2.publish("http://localhost:8182/PJSJobSubmission");
   	    
      	
      	
      	
   	    System.out.println("I am here");
   	    ServiceManager manager=ServiceManager.getInstance();
   	    
	}
	

}
