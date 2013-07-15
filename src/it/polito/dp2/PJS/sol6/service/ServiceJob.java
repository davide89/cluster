package it.polito.dp2.PJS.sol6.service;

import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import it.polito.dp2.PJS.Host;
import it.polito.dp2.PJS.Job;
import it.polito.dp2.PJS.JobGroup;
import it.polito.dp2.PJS.sol6.service.gen.jaxws.SubmitJobFault_Exception;


public class ServiceJob implements Job{

	int jobId;
	JobState state;
	ServiceHost subHost,execHost;
	XMLGregorianCalendar subTime;
	ServiceJobGroup jobGroup;
	String command,stdIn;
	ServiceHost client;
	String uri;
	
	
	
	public ServiceJob(String command, String std, ServiceJobGroup sjg, ServiceHost h) throws SubmitJobFault_Exception{
	
		GregorianCalendar c=(GregorianCalendar) Calendar.getInstance();
		
		
		this.jobId=-1;
		this.state=JobState.PENDING;
	
		try {
			this.subTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
		} catch (DatatypeConfigurationException e) {
			System.out.println("Error in data conversion");
			throw new SubmitJobFault_Exception(null, null);
		}
		
		
		this.execHost=null;
		this.jobGroup=sjg;
		this.command=command;
		
		if(std!=null && std.isEmpty()==false){
			this.stdIn=std;
		}
		
		
		System.out.println("------->Submission time is: "+subTime);
		
		this.client=h;
		this.uri=null;
		
	}
	
	
	@Override
	public Host getExecutionHost() {
		return(execHost);
	}

	@Override
	public int getID() {
		return(jobId);
	}

	@Override
	public JobGroup getJobGroup() {
		return(jobGroup);
	}

	@Override
	public JobState getState() {
		return(state);
	}

	@Override
	public Host getSubmissionHost() {
		return(client);
	}

	@Override
	public Calendar getSubmissionTime() {
		return(null);
	}

	public XMLGregorianCalendar getXMLSubmissionTime() {
		return(subTime);
	}
	
	
	
	
	//Function to set uri: it is used for job control
	public void setURI(String uri){
		this.uri=uri;
		
		return;
	}
	
	
	public String getCommand(){
		return(command);
	}
	
	public String getStdIn(){
		return(stdIn);
	}
	
	
	
	public void setJobId(int i){
		this.jobId=i;
	}
	
	
	public void setRunning(){
		this.state=JobState.RUNNING;
	}
	
	
	
	public void setExecHost(ServiceHost s){
		this.execHost=s;
	}
	
	
	
	
	@Override
	public boolean equals(Object obj){
		if(!(obj instanceof ServiceJob)){
			return false;
		}
		
		ServiceJob job = (ServiceJob)obj;
		if(job.getID()==jobId){
			return(true);
		}
		return(false);
	}
	
	
	
}
