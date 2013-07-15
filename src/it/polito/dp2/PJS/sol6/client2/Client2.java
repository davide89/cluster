package it.polito.dp2.PJS.sol6.client2;

import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;

import it.polito.dp2.PJS.lab6.*;
import it.polito.dp2.PJS.lab6.tests.gen.jaxws.SuspendResponse;
import it.polito.dp2.PJS.sol6.client2.gen.jaxws.JobSubmissionR;
import it.polito.dp2.PJS.sol6.client2.gen.jaxws.PJSJobSubmission;
import it.polito.dp2.PJS.sol6.client2.gen.jaxws.PJSJobSubmissionPort;
import it.polito.dp2.PJS.sol6.client2.gen.jaxws.SubmitJobFault_Exception;
import it.polito.dp2.PJS.sol6.client2.gen.jaxws.SubmitJobResponse;

public class Client2 implements Submit{
	
		
	
	
		
	public Client2(){
		
	}
	
	
	@Override
	public int submit(String host, String cmdline, String stdin) {
		
		String urlTmp;
		QName qname;
		//String url="http://localhost:8182/PJSJobSubmission";
		String url="http://localhost:8182";
		URL urlC;
		BigInteger finalId;
		
		System.out.println("I am HERE");
		
		if(host==null || cmdline==null){
			System.out.println("Error: invalid job submission");
			return(-1);
		}
		
		urlTmp=System.getProperty("it.polito.dp2.PJS.sol6.URL2");
		
		
		qname=new QName("http://www.example.org/PJSMaster/","PJSJobSubmission");
				
				
//		if(urlTmp!=null){
//			url=urlTmp;
//		}
//		else{
//			url.concat("/PJSJobSubmission");
//		}
		
//		url.concat("?wsdl");
		String finalUrl=null;
		if(urlTmp!=null && urlTmp.isEmpty()==false){
			finalUrl=urlTmp.concat("/PJSJobSubmission?wsdl");
			System.out.println("Client 2 trying: "+finalUrl);
		}
		else{
			finalUrl=url.concat("/PJSJobSubmission?wsdl");
			System.out.println("Client 2 trying: "+finalUrl);
		}
		
		
		try {
			//urlC=new URL(url);
			urlC=new URL(finalUrl);
		} catch (MalformedURLException e) {
			System.out.println("Error: url malformed in client2");
			return(-1);
		}
		
		
		PJSJobSubmission service=new PJSJobSubmission(urlC,qname);
		
		PJSJobSubmissionPort port=service.getPJSJobSubmissionPort();
		
		JobSubmissionR r=null;
		
		
		try {
//			finalId=port.submitJob(host, cmdline, stdin, "default",1);
			r=port.submitJob(host, cmdline, stdin, "default",BigInteger.valueOf(1));
		} catch (SubmitJobFault_Exception e) {
			System.out.println("The job cannot be submitted");
			return(-1);
		}
		
		
		return(r.getJobId().intValue());
	}
	
	
	
//	public static void main(String[] args) {
//	
//		Client2 tmp=new Client2();
//		int id;
//		
//		id=tmp.submit("host3", "sudo apt-get", "update");
//		
//		System.out.println("Id is: "+id);
//	}

	
}
