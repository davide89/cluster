package it.polito.dp2.PJS.sol6.service;



import it.polito.dp2.PJS.sol6.service.gen.jaxws.GetHostsFault;
import it.polito.dp2.PJS.sol6.service.gen.jaxws.GetHostsFault_Exception;
import it.polito.dp2.PJS.sol6.service.gen.jaxws.GetHostsNameFault;
import it.polito.dp2.PJS.sol6.service.gen.jaxws.GetHostsNameFault_Exception;
import it.polito.dp2.PJS.sol6.service.gen.jaxws.GetJobGroupsFault;
import it.polito.dp2.PJS.sol6.service.gen.jaxws.GetJobGroupsFault_Exception;
import it.polito.dp2.PJS.sol6.service.gen.jaxws.GetJobGroupsNameFault_Exception;
import it.polito.dp2.PJS.sol6.service.gen.jaxws.GetJobIdFault;
import it.polito.dp2.PJS.sol6.service.gen.jaxws.GetJobIdFault_Exception;
import it.polito.dp2.PJS.sol6.service.gen.jaxws.GetJobsFault;
import it.polito.dp2.PJS.sol6.service.gen.jaxws.GetJobsFault_Exception;
import it.polito.dp2.PJS.sol6.service.gen.jaxws.HostType;
import it.polito.dp2.PJS.sol6.service.gen.jaxws.JobGroupType;
import it.polito.dp2.PJS.sol6.service.gen.jaxws.JobTerminatedNotificationFault_Exception;
import it.polito.dp2.PJS.sol6.service.gen.jaxws.JobType;
import it.polito.dp2.PJS.sol6.service.gen.jaxws.PJSMasterPort;
import it.polito.dp2.PJS.sol6.service.gen.jaxws.ServiceUnavailable_Exception;
import it.polito.dp2.PJS.sol6.service.gen.jaxws.UnknownHost;
import it.polito.dp2.PJS.sol6.service.gen.jaxws.UnknownHost_Exception;
import it.polito.dp2.PJS.sol6.service.gen.jaxws.UnknownJob;
import it.polito.dp2.PJS.sol6.service.gen.jaxws.UnknownJobGroup;
import it.polito.dp2.PJS.sol6.service.gen.jaxws.UnknownJobGroup_Exception;
import it.polito.dp2.PJS.sol6.service.gen.jaxws.UnknownJob_Exception;


import java.math.BigInteger;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;




@WebService(name = "PJSMaster", 
			endpointInterface="it.polito.dp2.PJS.sol6.service.gen.jaxws.PJSMasterPort",
			wsdlLocation="META-INF/PJSMaster.wsdl",
			portName="PJSMasterPort",
			serviceName="PJSMaster",
			targetNamespace = "http://www.example.org/PJSMaster/"
			)
			

public class PJSMasterImpl implements PJSMasterPort {
	
	

	@Override
	@WebMethod(operationName = "GetHostsName")
	@WebResult(name = "HostsName", targetNamespace = "")
	@RequestWrapper(localName = "GetHostsName", targetNamespace = "http://www.example.org/PJSMaster/", className = "it.polito.dp2.PJS.sol6.service.GetHostsName")
	@ResponseWrapper(localName = "GetHostsNameResponse", targetNamespace = "http://www.example.org/PJSMaster/", className = "it.polito.dp2.PJS.sol6.service.GetHostsNameResponse")
	public List<String> getHostsName() throws GetHostsNameFault_Exception {
		
		List<String> hosts;
		
		
		
		
		 try{
			 ServiceManager manager=ServiceManager.getInstance();
			 
			 if(manager.getMasterHost().getStatus().toString().equals("OK")==false){
				 System.out.println("Cluster UNAVAIL");
				 GetHostsNameFault syserr = new GetHostsNameFault();
				 syserr.setGetHostsNameFault("Cluster UNAVAIL");
		         throw new GetHostsNameFault_Exception("Cluster UNAVAIL", syserr);
			 }
			 
			 hosts=manager.getHostsName();
		 }catch(Exception e){
	        	System.out.println("Internal server error: getHostsName()");
	        	GetHostsNameFault syserr = new GetHostsNameFault();
	        	syserr.setGetHostsNameFault("Internal server error");
	        	throw new GetHostsNameFault_Exception(e.getMessage(), syserr);
	        }
		return(hosts);
	}
	


	@Override
	@WebMethod(operationName = "GetHosts")
	@WebResult(name = "HostResponse", targetNamespace = "")
	@RequestWrapper(localName = "GetHosts", targetNamespace = "http://www.example.org/PJSMaster/", className = "it.polito.dp2.PJS.sol6.service.GetHosts")
	@ResponseWrapper(localName = "GetHostsResponse", targetNamespace = "http://www.example.org/PJSMaster/", className = "it.polito.dp2.PJS.sol6.service.GetHostsResponse")
	public List<HostType> getHosts(
			@WebParam(name = "HostRequest", targetNamespace = "") List<String> hostRequest)
			throws GetHostsFault_Exception, UnknownHost_Exception {
		

		List<HostType> hosts;
		
		try{
			 ServiceManager manager=ServiceManager.getInstance();
			 
			 
			 if(manager.getMasterHost().getStatus().toString().equals("OK")==false){
				 System.out.println("Cluster UNAVAIL");
				 GetHostsFault syserr = new GetHostsFault();
				 syserr.setGetHostsFault("Cluster UNAVAIL");
		         throw new GetHostsFault_Exception("Cluster UNAVAIL", syserr);
			 }
			 
			 
			 
			 System.out.println("Host requested: "+hostRequest);
			 hosts=manager.getHostsByName(hostRequest);
			 
		 }
		catch(UnknownHost_Exception uhe){
			System.out.println("Host requested is not present");
        	UnknownHost syserr = new UnknownHost();
        	syserr.setUnknownHost("An host inserted is not present");
        	throw new UnknownHost_Exception(uhe.getMessage(), syserr);
		}
		catch(Exception e){
	        	System.out.println("Internal server error: getHosts");
	        	GetHostsFault syserr = new GetHostsFault();
	        	syserr.setGetHostsFault("Internal server error");
	        	throw new GetHostsFault_Exception(e.getMessage(), syserr);
	        }
		return(hosts);
	}

	


	
	
	
	
	@Override
	@WebMethod(operationName = "GetJobId")
	@WebResult(name = "JobId", targetNamespace = "")
	@RequestWrapper(localName = "GetJobId", targetNamespace = "http://www.example.org/PJSMaster/", className = "it.polito.dp2.PJS.sol6.service.GetJobId")
	@ResponseWrapper(localName = "GetJobIdResponse", targetNamespace = "http://www.example.org/PJSMaster/", className = "it.polito.dp2.PJS.sol6.service.GetJobIdResponse")
	public List<BigInteger> getJobId() throws GetJobIdFault_Exception {
		
		List<BigInteger> jobs;
		 try{
			 ServiceManager manager=ServiceManager.getInstance();
			 
			 
			 if(manager.getMasterHost().getStatus().toString().equals("OK")==false){
				 System.out.println("Cluster UNAVAIL");
				 GetJobIdFault syserr = new GetJobIdFault();
				 syserr.setGetJobIdFault("Cluster UNAVAIL");
		         throw new GetJobIdFault_Exception("Cluster UNAVAIL", syserr);
			 }
			 
			 jobs=manager.getJobIds();
		 }catch(Exception e){
	        	System.out.println("Internal server error: GetJobId");
	        	GetJobIdFault syserr = new GetJobIdFault();
	        	syserr.setGetJobIdFault("Internal server error");
	        	throw new GetJobIdFault_Exception(e.getMessage(), syserr);
	        }
		return(jobs);
	}
	
	
	
	
	
	
	

	@Override
	@WebMethod(operationName = "GetJobs")
	@WebResult(name = "JobsResponse", targetNamespace = "")
	@RequestWrapper(localName = "GetJobs", targetNamespace = "http://www.example.org/PJSMaster/", className = "it.polito.dp2.PJS.sol6.service.GetJobs")
	@ResponseWrapper(localName = "GetJobsResponse", targetNamespace = "http://www.example.org/PJSMaster/", className = "it.polito.dp2.PJS.sol6.service.GetJobsResponse")
	public List<JobType> getJobs(
			@WebParam(name = "JobRequest", targetNamespace = "") List<BigInteger> jobRequest)
			throws GetJobsFault_Exception, UnknownJob_Exception {
		
		List<JobType> jobs;
		
		System.out.println("Job requested are: "+jobRequest);
		
		try{
			 ServiceManager manager=ServiceManager.getInstance();
			 
			 
			 if(manager.getMasterHost().getStatus().toString().equals("OK")==false){
				 System.out.println("Cluster UNAVAIL");
				 GetJobsFault syserr = new GetJobsFault();
				 syserr.setGetJobsFault("Cluster UNAVAIL");
		         throw new GetJobsFault_Exception("Cluster UNAVAIL", syserr);
			 }
			 
			 
			 System.out.println("Access GARANTED");
			 jobs=manager.getJobsById(jobRequest);
		 }
		catch(UnknownJob_Exception uje){
			System.out.println("Job requested is not present");
        	UnknownJob syserr = new UnknownJob();
        	//syserr.setUnknownJob("A job inserted is not present");
        	throw new UnknownJob_Exception(uje.getMessage(), null);
		}
		catch(Exception e){
	        	System.out.println("Internal server error: getJobs");
	        	GetJobsFault syserr=new GetJobsFault();
	        	syserr.setGetJobsFault("Internal server error");
	        	throw new GetJobsFault_Exception(e.getMessage(), syserr);
	        }
		return(jobs);
	}

	
	
	
	
	
	
	
	
	
	
	
	@Override
	@WebMethod(operationName = "GetJobGroupsName")
	@WebResult(name = "JobGroupsName", targetNamespace = "")
	@RequestWrapper(localName = "GetJobGroupsName", targetNamespace = "http://www.example.org/PJSMaster/", className = "it.polito.dp2.PJS.sol6.service.GetJobGroupsName")
	@ResponseWrapper(localName = "GetJobGroupsNameResponse", targetNamespace = "http://www.example.org/PJSMaster/", className = "it.polito.dp2.PJS.sol6.service.GetJobGroupsNameResponse")
	public List<String> getJobGroupsName()
			throws GetJobGroupsNameFault_Exception {
		
		List<String> jobGroup=null;
		 try{
			 ServiceManager manager=ServiceManager.getInstance();
			 
			 if(manager.getMasterHost().getStatus().toString().equals("OK")==false){
				 System.out.println("Cluster UNAVAIL");
				 GetJobGroupsFault syserr = new GetJobGroupsFault();
				 syserr.setGetJobGroupsFault("Cluster UNAVAIL");
		         throw new GetJobGroupsFault_Exception("Cluster UNAVAIL", syserr);
			 }
			 
			 
			 jobGroup=manager.getJobGroupsName();
		 }catch(Exception e){
	        	System.out.println("Internal server error");
	        	GetJobGroupsFault syserr = new GetJobGroupsFault();
	        	syserr.setGetJobGroupsFault("Internal server error: getJobGroupsName");
	        	try {
					throw new GetJobGroupsFault_Exception(e.getMessage(), syserr);
				} catch (GetJobGroupsFault_Exception e1) {
				}
	        }
		return(jobGroup);
	}
	
	
	
	
	
	
	
	
	
	
	

	@Override
	@WebMethod(operationName = "GetJobGroups")
	@WebResult(name = "JobGroupResponse", targetNamespace = "")
	@RequestWrapper(localName = "GetJobGroups", targetNamespace = "http://www.example.org/PJSMaster/", className = "it.polito.dp2.PJS.sol6.service.GetJobGroups")
	@ResponseWrapper(localName = "GetJobGroupsResponse", targetNamespace = "http://www.example.org/PJSMaster/", className = "it.polito.dp2.PJS.sol6.service.GetJobGroupsResponse")
	public List<JobGroupType> getJobGroups(
			@WebParam(name = "JobGroupRequest", targetNamespace = "") List<String> jobGroupRequest)
			throws GetJobGroupsFault_Exception, UnknownJobGroup_Exception {
		
		List<JobGroupType> groups;
		
		try{
			 ServiceManager manager=ServiceManager.getInstance();
			 
			 if(manager.getMasterHost().getStatus().toString().equals("OK")==false){
				 System.out.println("Cluster UNAVAIL");
				 GetJobGroupsFault syserr = new GetJobGroupsFault();
				 syserr.setGetJobGroupsFault("Cluster UNAVAIL");
		         throw new GetJobGroupsFault_Exception("Cluster UNAVAIL", syserr);
			 }
			 
			 
			 groups=manager.getjobGroups(jobGroupRequest);
			 
		 }
		catch(UnknownJobGroup_Exception uje){
			System.out.println("Job group requested is not present");
        	UnknownJobGroup syserr = new UnknownJobGroup();
        	syserr.setUnknownJobGroup("An host inserted is not present");
        	throw new UnknownJobGroup_Exception(uje.getMessage(), syserr);
		}
		catch(Exception e){
	        	System.out.println("Internal server error");
	        	GetJobGroupsFault syserr = new GetJobGroupsFault();
	        	syserr.setGetJobGroupsFault("Internal server error: getJobGroups");
	        	throw new GetJobGroupsFault_Exception(e.getMessage(), syserr);
	        }
		return(groups);
	}




}
