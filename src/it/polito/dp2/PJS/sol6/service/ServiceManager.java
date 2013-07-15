package it.polito.dp2.PJS.sol6.service;



import it.polito.dp2.PJS.Cluster;
import it.polito.dp2.PJS.Host.HostStatus;
import it.polito.dp2.PJS.Job;
import it.polito.dp2.PJS.lab6.tests.gen.jaxb.THost;
import it.polito.dp2.PJS.lab6.tests.gen.jaxb.Hosts;

import java.io.File;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import it.polito.dp2.PJS.sol6.service.gen.jaxws.ClientType;
import it.polito.dp2.PJS.sol6.service.gen.jaxws.GetHostsFault_Exception;
import it.polito.dp2.PJS.sol6.service.gen.jaxws.GetJobGroupsFault_Exception;
import it.polito.dp2.PJS.sol6.service.gen.jaxws.HostType;
import it.polito.dp2.PJS.sol6.service.gen.jaxws.JobGroupType;
import it.polito.dp2.PJS.sol6.service.gen.jaxws.JobType;
import it.polito.dp2.PJS.sol6.service.gen.jaxws.ServerType;
import it.polito.dp2.PJS.sol6.service.gen.jaxws.ServerTypeType;
import it.polito.dp2.PJS.sol6.service.gen.jaxws.StateJobType;
import it.polito.dp2.PJS.sol6.service.gen.jaxws.StatusServerType;
import it.polito.dp2.PJS.sol6.service.gen.jaxws.UnknownHost_Exception;
import it.polito.dp2.PJS.sol6.service.gen.jaxws.UnknownJobGroup_Exception;
//import it.polito.dp2.PJS.sol6.service.gen.jaxws.*;
import it.polito.dp2.PJS.sol6.service.gen.jaxws.UnknownJob_Exception;
import it.polito.dp2.PJS.lab6.tests.gen.jaxws.AlreadyDispatchedJob_Exception;
import it.polito.dp2.PJS.lab6.tests.gen.jaxws.PJSDispatch;
import it.polito.dp2.PJS.lab6.tests.gen.jaxws.PJSDispatchService;
import it.polito.dp2.PJS.lab6.tests.gen.jaxws.TooManyJobs_Exception;
//import it.polito.dp2.PJS.lab6.tests.gen.jaxws.*;
import it.polito.dp2.PJS.lab6.tests.gen.jaxws.ServiceUnavailable_Exception;





public class ServiceManager{

	
	

	private volatile Set<ServiceHost> executionHost;
	private volatile Set<ServiceHost> submissionHost;
	
	
	private ServiceHost master;
	
	private volatile ServiceJobGroup jobGroup;
	private volatile int jobIds;
	
	/*
     * ServiceManagerHolder is loaded on the first execution of
     * ServiceManager.getInstance() or the first access to
     * ServiceManager.INSTANCE, not before.
     */
    private static class ServiceManagerHolder {
        private final static ServiceManager INSTANCE = new ServiceManager();
    }
    
    
    public static ServiceManager getInstance() {
        return ServiceManagerHolder.INSTANCE;
    }
	

    // Protected constructor is sufficient to suppress unauthorized calls to the
    // constructor
    private ServiceManager() {        
        System.out.println("ServiceManager created.");
        
        
        //Synchronized Set: this set is accessed in concurrency
        submissionHost=Collections.synchronizedSet(new HashSet<ServiceHost>());
        
        
        
        //it is no sync: loaded at startup
        executionHost=new HashSet<ServiceHost>();
        
        
        //counter to give an id to a job
        jobIds=1;
        jobGroup=new ServiceJobGroup("default","this is the default job group");

        
        
        //load the XML document
        executionHost=loadExecutionHost();
  
        
    }
    
    
  
//*******************************FUNCTIONES*******************************//    
    
    
    
    
    
    //Function to load execution host from XML document
    public Set<ServiceHost> loadExecutionHost(){
    	Set<ServiceHost> hosts=new HashSet<ServiceHost>();
    	
    	
    	
    	JAXBContext jxc= null;
		Unmarshaller unmarshaller = null;
		
		try {
			jxc = JAXBContext.newInstance("it.polito.dp2.PJS.lab6.tests.gen.jaxb");
			unmarshaller = jxc.createUnmarshaller();
		} catch (JAXBException jaxbe) {
			System.out.println("Error creating the unmarshaller: " + jaxbe.getMessage());
			//throw new MyClusterException(jaxbe.getMessage());
		}

		
		Schema mySchema = null;
		File schemaFile = new File("xsd/PJSHostsInfo.xsd");
		if(schemaFile.exists() && schemaFile.canRead()){	
			try {
				SchemaFactory sf = SchemaFactory.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
				mySchema = sf.newSchema(new File("xsd/PJSHostsInfo.xsd"));
				unmarshaller.setSchema(mySchema);
			} catch (SAXException saxe) {
				System.out.println("Error, cannot parse the schema file: " + saxe.getMessage() );
				System.exit(1);
			}
		}else{
			System.out.println("Warning, cannot locate the Schema file.");
			System.exit(1);
		}
		

		
		Hosts myHost=null;
		try{
			Object o = unmarshaller.unmarshal( new File("xml/execHosts.xml"));
			if( o instanceof Hosts )
				myHost=(Hosts) o;
			else{
				System.out.println("Error, unknown JAXB element");
				System.exit(1);
			}
		}catch (JAXBException jaxbe) {
			System.out.println("Error, cannot parse the XML file");
			System.exit(1);
		}

		
		List<THost> hostsRead=myHost.getHost();
		
	
		
		for(THost h : hostsRead){	
			ServiceHost sh=new ServiceHost(h);
			if(hosts.add(sh)==false){
				System.out.println("Error: two hosts with the same name");
				System.exit(1);
			}
			
			
			if(sh.isMaster()==true){
				master=sh;
			}
			
			System.out.println("Name: "+sh.getName());
			System.out.println("URI: "+sh.getURI());
			System.out.println("Memory: "+sh.getPhysicalMemory());
		}
		return(hosts);
    }
    
    
    
    
    
    
    
    
    //Return hosts name
    public synchronized List<String> getHostsName(){
    	List<String> l=new ArrayList<String>();
    	
		for(ServiceHost sh : executionHost){
			l.add(sh.getName());
		}
	
//		for(ServiceHost sh : submissionHost){
//			l.add(sh.getName());
//		}

    	return(l);
    }
    
    
    

    //Return hosts by name ONLY FOR SERVERS!!!!!
    public synchronized List<HostType> getHostsByName(List<String> hReq) throws UnknownHost_Exception, GetHostsFault_Exception{
    	
    	List<HostType> hosts=new ArrayList<HostType>();
    	
		
    	if(master.getStatus().toString().equals("OK")==false){
    		System.out.println("Master host is not ok: cluster UNAVAIL");
    		throw new  GetHostsFault_Exception("Cluster unavail",null);
    	}
    	
    		for(String s : hReq){
    			boolean found=false;
    			for( ServiceHost sh : executionHost){
    				if(sh.getName().equals(s)==true){
    					
    					//convert the host
    					HostType tmp=new HostType();
    					ServerType st=null;;
    					tmp.setNameH(s);
    					
    					//setting host type
    					if(sh.isMaster()==true){
    						st=new ServerType();
    						st.setType(ServerTypeType.MASTER);
    						
    						System.out.println("This host is: Master");
    					}
    					else{
    						if(sh.isServer()==true){
    							st=new ServerType();
    							st.setType(ServerTypeType.SERVER);
    							System.out.println("This host is: SERVER");
    						}
    					}
    					
    					if(st!=null){
    						//host is master/server
    						st.setMemory(BigInteger.valueOf(sh.getPhysicalMemory()));
    						st.setLoad(BigInteger.valueOf(sh.getLoad()));
    						
    						//status
    						String state=sh.getStatus().toString();
    						
    						if(state.equals("OK")==true){
    							st.setStatus(StatusServerType.OK);
    						}
    						else{
    							if(state.equals("UNAVAIL")==true){
    								st.setStatus(StatusServerType.UNAVAIL);
    							}
    							else{
    								if(state.equals("UNLICENSED")==true){
    									st.setStatus(StatusServerType.UNLICENSED);
    								}
    								else{
    									if(state.equals("CLOSED")==true){
    										st.setStatus(StatusServerType.CLOSED);
    									}
    								}
    							}
    						}
    						tmp.setServer(st);
    						
    						System.out.println("I AM HERE: is a server");
    					}
    					
    					if(st==null){
    						//host is client
    						
    						ClientType ct=new ClientType();
    						ct.setStatus("OK");
    						tmp.setClient(ct);
    					}

    					found=true;

        	   			//add host to list
            			hosts.add(tmp);
    				}
    			}

    			if(found==false){
    				//host not found
    				throw new UnknownHost_Exception(null, null);
    			}
		}
	
    	return(hosts);
    }
    
    
    
    
    
    //Return job IDs
    public synchronized List<BigInteger> getJobIds(){
    	List<BigInteger> id=new ArrayList<BigInteger>();
    	
    	
    	synchronized (jobGroup) {
    		//Set<Job> jobs=jobGroup.getJobs();
    		Set<ServiceJob> jobs=jobGroup.getJobsB();
    		
    		
    		
			for(Job j : jobs){
				id.add(BigInteger.valueOf(j.getID()));
			}
		}
    	
    	return(id);
    }
    
    
    
    
    //Return a list of JobType
    public synchronized List<JobType> getJobsById(List<BigInteger>ids) throws UnknownJob_Exception{
    	List<JobType> jobs=new ArrayList<JobType>();
    	//Set<Job> jj=jobGroup.getJobs();
    	
    	System.out.println("I am here: getJobsById 01");
    	
    	
    	Set<ServiceJob> jj=jobGroup.getJobsB();
    	
    	
    	System.out.println("I am here: getJobsById 02");
    	
    	for(BigInteger bi : ids){
    		boolean found=false;
    		
    		for(Job j : jj ){
    			
    			System.out.println("I am here: getJobsById 03");
    			
    			
    			System.out.println("Are equals: "+j.getID()+" and "+bi.intValue());
    			
    			if(j.getID()==bi.intValue()){
    				
    				System.out.println("I am here: getJobsById 04 -> YES ARE EQUAL ");
    				
    				
    				//convert the job
    				JobType tmpJob=new JobType();
    			
    				
    				System.out.println("I am here: getJobsById 05");
    				
    				tmpJob.setJobID(bi);
    				
    				System.out.println("I am here: getJobsById 06");
    				
    				tmpJob.setSubHost(j.getSubmissionHost().getName());
    				
    				System.out.println("I am here: getJobsById 07");
    				
    				if(j.getExecutionHost()!=null){
    					tmpJob.setExecHost(j.getExecutionHost().getName());
    					System.out.println("I am here: getJobsById 08");
    				}
    				
    				
    				if(j.getSubmissionTime()!=null){
    					System.out.println("I am here: getJobsById 09");
    					XMLGregorianCalendar d=null;
						try {
							d = DatatypeFactory.newInstance().newXMLGregorianCalendar((GregorianCalendar) j.getSubmissionTime());
						} catch (DatatypeConfigurationException e) {
							System.out.println("Error in data conversion");
							//throw
						}
    					tmpJob.setSubTime(d);
    					System.out.println("I am here: getJobsById 10");
    				}
    				
    				
    				System.out.println("I am here: getJobsById 11");
    				
    				String st=j.getState().toString();
    				if(st.equals("PENDING")==true){
    					tmpJob.setState(StateJobType.PENDING);
    				}
    				else{
    					if(st.equals("RUNNING")==true){
        					tmpJob.setState(StateJobType.RUNNING);
        				}
    					else{
        					if(st.equals("DONE")==true){
            					tmpJob.setState(StateJobType.DONE);
            				}
        					else{
            					if(st.equals("EXIT")==true){
                					tmpJob.setState(StateJobType.EXIT);
                				}
            					else{
                					if(st.equals("SUSPENDED")==true){
                    					tmpJob.setState(StateJobType.SUSPENDED);
                    				}
                				}
            				}
        				}
    				}
    				
    				System.out.println("I am here: getJobsById 12");
    				found=true;
    				
    				//add job to the list
    				jobs.add(tmpJob);
    				
    				System.out.println("I am here: getJobsById 13");
    			}
    			
    		}
    		if(found==false){
				//host not found
    			System.out.println("I am here: getJobsById 14: WHY HERE??");
				throw new UnknownJob_Exception(null, null);
				
				
			}
    		
    	}
    	return(jobs);
    }
    
    
    
    //Return jobGroups name
    public List<String> getJobGroupsName(){
    	List<String> jg=new ArrayList<String>();
    	
    	jg.add(jobGroup.getName());
    	    	
    	return(jg);
    }
    
    
    
    //Return jobGroups by name
    public List<JobGroupType> getjobGroups(List<String> groups) throws UnknownJobGroup_Exception, GetJobGroupsFault_Exception{
    	List<JobGroupType> g=new ArrayList<JobGroupType>();
    	
    	for(String s : groups){
    		if(jobGroup.getName().equals(s)==false){
    			throw new UnknownJobGroup_Exception(null, null);
    		}

    		JobGroupType jg=new JobGroupType();
    		jg.setNameJG(jobGroup.getName());
    		
    		if(jobGroup.getDescription()!=null){
    			jg.setDescription(jobGroup.getDescription());
    		}
    		
    		List<BigInteger> jIds=getJobIds();
    		
    		List<JobType> jobs=null;
    		try {
				jobs=getJobsById(jIds);
			}catch (UnknownJob_Exception e) {
				System.out.println("Internal error");
				throw new GetJobGroupsFault_Exception(e.getMessage(),null);
			}

    		//jg.job=jobs;
    		g.add(jg);
    	}	
    	return(g);
    }
    
    
    
    //Returns the default jobGroup
    public ServiceJobGroup getDefaultJobGroup(){
    	return(jobGroup);
    }
    
    
    
    //Returns a client host: if it does not exist it will be created
    public ServiceHost getSubmissionHost(String name){
    	ServiceHost tmp=null;
    	
    	for(ServiceHost h : submissionHost){
    		if(h.getName().equals(name)==true){
    			return(h);
    		}
    	}
    	
    	tmp=new ServiceHost(name);
    	
    	return(tmp);
    }
    
    
    //Increment the id
    public synchronized void incrementId(){
    	this.jobIds++;
    }
    
    
    
    //Function to dispatch the job
    public synchronized BigInteger tryDispatch(ServiceJob j){
    	BigInteger id=BigInteger.valueOf(-1);
    	
    	
    	//init a vector with host load (execution hosts)
    	int vSize=executionHost.size();
    	
    	int loadArray[]=new int[vSize];
    	int cnt=0;
    	
    	for(ServiceHost sh : executionHost){
    		if(sh.getStatus().toString().equals("OK")==true){
    			loadArray[cnt]=sh.getLoad();
    			System.out.println("This host has load: "+ loadArray[cnt]);
    		}
    		else{
    			loadArray[cnt]=Integer.MAX_VALUE;
    		}
    		cnt++;
    	}
    	
    	
    	boolean cont=true;
    	
    	System.out.println("I AM HERE: 01");
    	
    	while(cont==true){
    		
    		System.out.println("I AM HERE: 02");
    		
			//find the min load
//			int posMin=0;
//			boolean found=false;

//			j.setJobId(jobIds);
//			id=BigInteger.valueOf(jobIds);
			
//			for(int i=0;i<vSize;i++){
//				if(loadArray[posMin]>=loadArray[i]){
//					posMin=i;
//					found=true;
//				}
//			}
    		
    		int posMin=0;
    		int min=Integer.MAX_VALUE;
    		boolean found=false;
    		j.setJobId(jobIds);
    		id=BigInteger.valueOf(jobIds);
    		
    		for(int i=0;i<vSize;i++){
    			if(min>=loadArray[i]){
    				posMin=i;
    				min=loadArray[i];
    				found=true;
    			}
    		}
    		
    		
    		if(min==Integer.MAX_VALUE){
    			System.out.println("The job cannot be dispatched");
    			return(BigInteger.valueOf(-1));
    		}
    		
    		
    		
			
			
			System.out.println("I AM HERE: 03");
			
			//there are no available execution host!
			if(found==false){
				System.out.println("The job cannot be dispatched 01");
				return(BigInteger.valueOf(-1));
			}
			
			//catch the best execution host
			cnt=0;
			
			ServiceHost bestExec=null;
			for(ServiceHost sh : executionHost){
				if(cnt==posMin){
					bestExec=sh;
				}
				cnt++;
			}
			
			System.out.println("I AM HERE: 04");
			
			//try to dispatch to posMin host
			URL url=null;
			
			String urlAsString=bestExec.getURI();
			
			System.out.println("The uri is: "+bestExec.getURI());
			
			String finalUrl=urlAsString.concat("/PJSDispatchService?wsdl");
			
			try {
				url=new URL(finalUrl);
			} catch (MalformedURLException e1) {
				System.out.println("Error: malformed url");
				return(BigInteger.valueOf(-1));
			}

			System.out.println("I am trying to contact: "+finalUrl);
			System.out.println("I am here: before qname");
			
//CHECK:qname=new QName("http://localhost:8181/PJSDispatchService","PJSDispatch");
			
			QName qname=new QName("http://pad.polito.it/PJSDispatch","PJSDispatchService");
					
			PJSDispatchService service= new PJSDispatchService(url,qname);
			PJSDispatch port=service.getPJSDispatchPort();
		
		    
		    Boolean error=false;
		    
		    try {
				port.dispatch(BigInteger.valueOf(j.getID()), j.getCommand(), j.getStdIn(), "http://localhost:8182");
			} catch (AlreadyDispatchedJob_Exception e) {
				System.out.println("Error: already dispatched job");
				error=true;
			} catch (ServiceUnavailable_Exception e) {
				System.out.println("Error: service unavailable");
				error=true;
			} catch (TooManyJobs_Exception e) {
				System.out.println("Error: too many jobs");
				error=true;
			}
		    
		    System.out.println("I am here: after qname");
		        
			//if not possible, set loadArray[posMin]=MAX and try again
			if(error==true){
				loadArray[posMin]=Integer.MAX_VALUE;
				cont=true;
			}
			else{
				cont=false;
				j.setRunning();
				bestExec.incrementLoad();
				
				j.setExecHost(bestExec);
				
			}
    	}

    	System.out.println("The id given is: "+id);
    	return(id);
    }
    
    
    
    public ServiceHost getMasterHost(){
    	return(master);
    }
}
