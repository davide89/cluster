package it.polito.dp2.PJS.sol6.service;

import java.math.BigInteger;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebParam.Mode;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.Holder;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;



import it.polito.dp2.PJS.sol6.service.gen.jaxws.*;


@WebService	(name = "PJSJobSubmission", 
			endpointInterface="it.polito.dp2.PJS.sol6.service.gen.jaxws.PJSJobSubmissionPort",
			wsdlLocation="META-INF/PJSMaster.wsdl",
			portName="PJSJobSubmissionPort",
			serviceName="PJSJobSubmission",
			targetNamespace = "http://www.example.org/PJSMaster/"
			)
public class PJSSubmissionPortImpl implements PJSJobSubmissionPort{

	@Override
	@WebMethod(operationName = "SubmitJob")
	@WebResult(name = "SubmitJobResponse", targetNamespace = "")
	@RequestWrapper(localName = "SubmitJob", targetNamespace = "http://www.example.org/PJSMaster/", className = "it.polito.dp2.PJS.sol6.service.gen.jaxws.SubmitJob")
	@ResponseWrapper(localName = "SubmitJobResponse", targetNamespace = "http://www.example.org/PJSMaster/", className = "it.polito.dp2.PJS.sol6.service.gen.jaxws.SubmitJobResponse")
	public JobSubmissionR submitJob(
			@WebParam(name = "client", targetNamespace = "") String client,
			@WebParam(name = "Command", targetNamespace = "") String command,
			@WebParam(name = "StdIn", targetNamespace = "") String stdIn,
			@WebParam(name = "JobGroup", targetNamespace = "") String jobGroup,
			@WebParam(name = "Version", targetNamespace = "") BigInteger version)
			throws SubmitJobFault_Exception {
		
			BigInteger idJob=BigInteger.valueOf(-1);
	
			 
			 
			 
			 if(client==null || client.trim().isEmpty()==true || command==null || command.trim().isEmpty()==true){
				 System.out.println("Internal server error: wrong request");
	       	SubmitJobFault syserr = new SubmitJobFault();
	       	syserr.setSubmitJobFault("Internal server error");
	       	throw new SubmitJobFault_Exception("Wrong request", syserr);
			 }
			 
			 ServiceJob tmpJob=null;
			 ServiceJobGroup sjg=null;
			 
			 try{
				 ServiceManager manager=ServiceManager.getInstance();
				 
	
				 sjg=manager.getDefaultJobGroup();
				 ServiceHost host=manager.getSubmissionHost(client);
				 
				 
				 tmpJob=new ServiceJob(command,stdIn,sjg,host);
				 
				
				 
				 idJob=manager.tryDispatch(tmpJob);
				 manager.incrementId();	 
						 
				 
			 }catch(Exception e){
		        	System.out.println("Internal server error: "+e.getMessage());
		        	SubmitJobFault syserr = new SubmitJobFault();
		        	syserr.setSubmitJobFault("Internal server error");
		        	throw new SubmitJobFault_Exception(e.getMessage(), syserr);
		        }
			 
			 
			 if(idJob.intValue()!=-1){
				 sjg.addJob(tmpJob);
			 }
			 else{
				 tmpJob=null;
			 }
			 
			 JobSubmissionR tmp=new JobSubmissionR();
			 tmp.setJobId(idJob);
			 tmp.setTransactionId(BigInteger.valueOf(0));
	
		return(tmp);
	}
	
	
	

	@Override
	@WebMethod(operationName = "SuspendJob")
	@RequestWrapper(localName = "SuspendJob", targetNamespace = "http://www.example.org/PJSMaster/", className = "it.polito.dp2.PJS.sol6.service.gen.jaxws.SuspendJob")
	@ResponseWrapper(localName = "SuspendJobResponse", targetNamespace = "http://www.example.org/PJSMaster/", className = "it.polito.dp2.PJS.sol6.service.gen.jaxws.SuspendJobResp")
	public void suspendJob(
			@WebParam(name = "JobId", targetNamespace = "") BigInteger jobId,
			@WebParam(name = "Client", targetNamespace = "") String client,
			@WebParam(name = "TransactionID", targetNamespace = "") BigInteger transactionID,
			@WebParam(name = "Version", targetNamespace = "") BigInteger version)
			throws ServiceUnavailable_Exception, SuspendFault {
		// TODO Auto-generated method stub
		
	}









	@Override
	@WebMethod(operationName = "ResumeJob")
	@RequestWrapper(localName = "ResumeJob", targetNamespace = "http://www.example.org/PJSMaster/", className = "it.polito.dp2.PJS.sol6.service.gen.jaxws.ResumeJob")
	@ResponseWrapper(localName = "ResumeJobResponse", targetNamespace = "http://www.example.org/PJSMaster/", className = "it.polito.dp2.PJS.sol6.service.gen.jaxws.ResumeJobResp")
	public void resumeJob(
			@WebParam(name = "JobId", targetNamespace = "") BigInteger jobId,
			@WebParam(name = "Client", targetNamespace = "") String client,
			@WebParam(name = "TransactionID", targetNamespace = "") BigInteger transactionID,
			@WebParam(name = "Version", targetNamespace = "") BigInteger version)
			throws ResumeFault, ServiceUnavailable_Exception {
		// TODO Auto-generated method stub
		
	}









	@Override
	@WebMethod(operationName = "KillJob")
	@RequestWrapper(localName = "KillJob", targetNamespace = "http://www.example.org/PJSMaster/", className = "it.polito.dp2.PJS.sol6.service.gen.jaxws.KillJob")
	@ResponseWrapper(localName = "KillJobResponse", targetNamespace = "http://www.example.org/PJSMaster/", className = "it.polito.dp2.PJS.sol6.service.gen.jaxws.KillJobResp")
	public void killJob(
			@WebParam(name = "JobId", targetNamespace = "") BigInteger jobId,
			@WebParam(name = "Client", targetNamespace = "") String client,
			@WebParam(name = "TransactionID", targetNamespace = "") BigInteger transactionID,
			@WebParam(name = "Version", targetNamespace = "") BigInteger version)
			throws KillFault, ServiceUnavailable_Exception {
		// TODO Auto-generated method stub
		
	}









	@Override
	@WebMethod(operationName = "KillJobGroup")
	@RequestWrapper(localName = "KillJobGroup", targetNamespace = "http://www.example.org/PJSMaster/", className = "it.polito.dp2.PJS.sol6.service.gen.jaxws.KillJobGroup")
	@ResponseWrapper(localName = "KillJobGroupResponse", targetNamespace = "http://www.example.org/PJSMaster/", className = "it.polito.dp2.PJS.sol6.service.gen.jaxws.KillJobGroupResp")
	public void killJobGroup(
			@WebParam(name = "JobGroupName", targetNamespace = "") String jobGroupName,
			@WebParam(name = "Client", targetNamespace = "") String client,
			@WebParam(name = "TransactionID", targetNamespace = "") BigInteger transactionID,
			@WebParam(name = "Version", targetNamespace = "") BigInteger version)
			throws KillGroupFault, ServiceUnavailable_Exception {
		// TODO Auto-generated method stub
		
	}









	@Override
	@WebMethod(operationName = "SuspendJobGroup")
	@RequestWrapper(localName = "SuspendJobGroup", targetNamespace = "http://www.example.org/PJSMaster/", className = "it.polito.dp2.PJS.sol6.service.gen.jaxws.SuspendJobGroup")
	@ResponseWrapper(localName = "SuspendJobGroupResponse", targetNamespace = "http://www.example.org/PJSMaster/", className = "it.polito.dp2.PJS.sol6.service.gen.jaxws.SuspendJobGroupResp")
	public void suspendJobGroup(
			@WebParam(name = "JobGroupName", targetNamespace = "") String jobGroupName,
			@WebParam(name = "Client", targetNamespace = "") String client,
			@WebParam(name = "TransactionID", targetNamespace = "") BigInteger transactionID,
			@WebParam(name = "Version", targetNamespace = "") BigInteger version)
			throws ServiceUnavailable_Exception, SuspendFault {
		// TODO Auto-generated method stub
		
	}









	@Override
	@WebMethod(operationName = "ResumeJobGroup")
	@RequestWrapper(localName = "ResumeJobGroup", targetNamespace = "http://www.example.org/PJSMaster/", className = "it.polito.dp2.PJS.sol6.service.gen.jaxws.ResumeJobGroup")
	@ResponseWrapper(localName = "ResumeJobGroupResponse", targetNamespace = "http://www.example.org/PJSMaster/", className = "it.polito.dp2.PJS.sol6.service.gen.jaxws.ResumeJobGroupResp")
	public void resumeJobGroup(
			@WebParam(name = "JobGroupName", targetNamespace = "") String jobGroupName,
			@WebParam(name = "Client", targetNamespace = "") String client,
			@WebParam(name = "TransactionID", targetNamespace = "") BigInteger transactionID,
			@WebParam(name = "Version", targetNamespace = "") BigInteger version)
			throws ResumeFault, ServiceUnavailable_Exception {
		// TODO Auto-generated method stub
		
	}









	@Override
	@WebMethod(operationName = "ResultJob")
	@RequestWrapper(localName = "ResultJob", targetNamespace = "http://www.example.org/PJSMaster/", className = "it.polito.dp2.PJS.sol6.service.gen.jaxws.ResultJob")
	@ResponseWrapper(localName = "ResultJobResponse", targetNamespace = "http://www.example.org/PJSMaster/", className = "it.polito.dp2.PJS.sol6.service.gen.jaxws.ResultJobResp")
	public void resultJob(
			@WebParam(name = "JobId", targetNamespace = "") BigInteger jobId,
			@WebParam(name = "Client", targetNamespace = "") String client,
			@WebParam(name = "TransactionID", targetNamespace = "") BigInteger transactionID,
			@WebParam(name = "Version", targetNamespace = "") BigInteger version)
			throws ResultFault, ServiceUnavailable_Exception {
		// TODO Auto-generated method stub
		
	}

	
	
	
	
	





	
}
