package it.polito.dp2.PJS.sol6.client1;

import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.namespace.QName;

import it.polito.dp2.PJS.Cluster;
import it.polito.dp2.PJS.ClusterException;
import it.polito.dp2.PJS.Host;
import it.polito.dp2.PJS.Job;
import it.polito.dp2.PJS.JobGroup;
import it.polito.dp2.PJS.sol6.client1.gen.jaxws.GetHostsFault_Exception;
import it.polito.dp2.PJS.sol6.client1.gen.jaxws.GetHostsNameFault_Exception;
import it.polito.dp2.PJS.sol6.client1.gen.jaxws.GetJobGroupsFault_Exception;
import it.polito.dp2.PJS.sol6.client1.gen.jaxws.GetJobGroupsNameFault_Exception;
import it.polito.dp2.PJS.sol6.client1.gen.jaxws.GetJobIdFault_Exception;
import it.polito.dp2.PJS.sol6.client1.gen.jaxws.GetJobsFault_Exception;
import it.polito.dp2.PJS.sol6.client1.gen.jaxws.HostType;
import it.polito.dp2.PJS.sol6.client1.gen.jaxws.JobGroupType;
import it.polito.dp2.PJS.sol6.client1.gen.jaxws.JobType;
import it.polito.dp2.PJS.sol6.client1.gen.jaxws.PJSMaster;
import it.polito.dp2.PJS.sol6.client1.gen.jaxws.PJSMasterPort;
import it.polito.dp2.PJS.sol6.client1.gen.jaxws.UnknownHost_Exception;
import it.polito.dp2.PJS.sol6.client1.gen.jaxws.UnknownJobGroup_Exception;
import it.polito.dp2.PJS.sol6.client1.gen.jaxws.UnknownJob_Exception;

public class MyCluster implements Cluster {

	Set<Host>cacheHosts;
	Set<JobGroup>cacheJobGroups;
	Set<Job>cacheJobs;
	
	MyJobGroup defaultJobGroup;
	Host masterHost;
	
	URL urlC;
	QName qn;
	
	ClusterStatus clusterS;
	
	
	public MyCluster(String url) throws MyClusterException{
		
		//INIT PHASE
		
		cacheHosts=new HashSet<Host>();
		cacheJobGroups=new HashSet<JobGroup>();
		cacheJobs=new HashSet<Job>();
		
		
		
		
		//url.concat("/PJSMaster?wsdl");
		
		QName qname = new QName("http://www.example.org/PJSMaster/", "PJSMaster");
		
		try {
			urlC=new URL(url);
			System.out.println("Client 2 verify: "+url);
		} catch (MalformedURLException e) {
			System.out.println("Malformed URL");
			throw new MyClusterException("Malformed URL");
		}
		
		
		qn=qname;
		
		
		PJSMaster service=new PJSMaster(urlC,qname);
		
		PJSMasterPort port=service.getPJSMasterPort();
		
		
		
		
		
		//HOST
		
		List<String> hostName=null;
		
		try {
			hostName=port.getHostsName();
		} catch (GetHostsNameFault_Exception e) {
			System.out.println("Error in contacting the server");
			throw new MyClusterException("Error in contacting the server");
		}
		
		List<HostType> hostRvc=null;
		
		System.out.println("string is: "+hostName);
		
		
		if(hostName==null){
			System.out.println("There are no hosts at the moment");
		}
		else{
			
			try {
				hostRvc=port.getHosts(hostName);
			} catch (GetHostsFault_Exception e) {
				System.out.println("Error in contacting the server");
				throw new MyClusterException("Error in contacting the server");
			} catch (UnknownHost_Exception e) {
				System.out.println("Unknown host requested");
				throw new MyClusterException("Unknown host requested");
			}
			
			
			if(hostRvc==null){
				System.out.println("Hosts are empty");
				throw new MyClusterException("Empty host");
			}
			
			
			//parsing the host
			
			boolean found=false;
			
			for(HostType ht : hostRvc){
				
				
				MyHost mh=null;
				
				try{
					mh=new MyHost(ht);
				}
				catch(ClusterException ce){
					System.out.println("Exception: "+ce.getMessage());
					throw(ce);
				}
				
				if(cacheHosts.add(mh)==false){
					System.out.println("There are 2 equal hosts");
					throw new MyClusterException("Equal hosts");
				}
				
				if(mh.isMaster()==true){
					if(found==true){
						System.out.println("Too many master hosts");
						throw new MyClusterException("Too many master hosts");
					}
					found=true;
					masterHost=mh;
				}
			}
			
			
		}
		
		
		
		//JOB
		
			List<BigInteger> jobsName=null;
			
			
			try {
				jobsName=port.getJobId();
			} catch (GetJobIdFault_Exception e1) {
				System.out.println("Error in contacting the server");
				throw new MyClusterException("Error in contacting the server");
			}
		
			
			List<JobType> jobRvc=null;
			
			if(jobsName==null){
				System.out.println("There are no jobs at the moment");
			}
			else{
				
				
				try {
					jobRvc=port.getJobs(jobsName);
				} catch (GetJobsFault_Exception e) {
					System.out.println("Error in contacting the server");
					throw new MyClusterException("Error in contacting the server");
				} catch (UnknownJob_Exception e) {
					System.out.println("Unknown job requested");
					throw new MyClusterException("Unknown job requested");
				}
			

				if(jobRvc==null){
					System.out.println("Job are empty");
					throw new MyClusterException("Empty job");
				}
				
				
				
				//parsing the job
				for(JobType jt : jobRvc){
					
					
					MyJob mj=null;
					
					try{
						mj=new MyJob(jt,this,defaultJobGroup);
					}
					catch(ClusterException ce){
						System.out.println("Exception: "+ce.getMessage());
						throw(ce);
					}
					
					if(cacheJobs.add(mj)==false){
						System.out.println("Error: jobId collision");
						throw new MyClusterException("JobId collision");
					}
				}
				
			}
		
		
				
			
			//JOB GROUP
			
			List<String> jobGroupsName=null;
			

			try {
				jobGroupsName=port.getJobGroupsName();
			} catch (GetJobGroupsNameFault_Exception e1) {
				System.out.println("Error in contacting the server");
				throw new MyClusterException("Error in contacting the server");
			}

			
			List<JobGroupType> jobGroupRvc=null;
			
			if(jobGroupsName==null){
				System.out.println("There are no job groups at the moment");
				throw new MyClusterException("No job groups!");
			}
			
				

			try {
				jobGroupRvc=port.getJobGroups(jobGroupsName);
			} catch (GetJobGroupsFault_Exception e) {
				System.out.println("Error in contacting the server");
				throw new MyClusterException("Error in contacting the server");
			} catch (UnknownJobGroup_Exception e) {
				System.out.println("Unknown job group requested");
				throw new MyClusterException("Unknown job group requested");
			}

			
			
			
			if(jobGroupRvc==null){
				System.out.println("JobGroups are empty");
				throw new MyClusterException("Empty jobGroups");
			}
			Host h=null;
			
			
			//parsing the jobGroup
			boolean defFounf=false;
			
			for(JobGroupType jgt : jobGroupRvc){
				
				
				
				MyJobGroup mjg=null;
				
				try{
					mjg=new MyJobGroup(jgt);
				}
				catch(Exception ce){
					System.out.println("Exception: "+ce.getMessage());
					throw new MyClusterException(ce.getMessage());
				}
				
				
				if(mjg.getName().equals("default")==true){
					if(defFounf==false){
						defaultJobGroup=mjg;
						mjg.setJobs(cacheJobs);
						defFounf=true;
					}
				}
				cacheJobGroups.add(mjg);
			}
			
		
			clusterS=ClusterStatus.OK;
		
	}
	
	
	
	
	public MyJobGroup updateJobGroup(){
		MyJobGroup tmp=null;
		
		
		PJSMaster service=null;
		PJSMasterPort port=null;
		
		
		
		
		try{
			service=new PJSMaster(urlC,qn);
			port=service.getPJSMasterPort();
		}
		catch(Exception e){
			System.out.println("Error in contacting the server while updating");
			return(null);
		}
		
		
		
		List<String> jobGroupsName=null;
		

		try {
			jobGroupsName=port.getJobGroupsName();
		} catch (GetJobGroupsNameFault_Exception e1) {
			System.out.println("Error in contacting the server while updating");
			return(null);
		}

		
		List<JobGroupType> jobGroupRvc=null;
		
		if(jobGroupsName==null){
			System.out.println("There are no job groups at the moment while updating");
			return(null);
		}
		
			

		try {
			jobGroupRvc=port.getJobGroups(jobGroupsName);
		} catch (GetJobGroupsFault_Exception e) {
			System.out.println("Error in contacting the server while updating");
			return(null);
		} catch (UnknownJobGroup_Exception e) {
			System.out.println("Unknown job group requested while updating");
			return(null);
		}

		
		
		
		if(jobGroupRvc==null){
			System.out.println("JobGroups are empty while updating");
			return(null);
		}
		Host h=null;
		
		
		//parsing the jobGroup
		boolean defFounf=false;
		MyJobGroup newGroupDefault=null;
		Set<JobGroup> updateJobGroups=new HashSet<JobGroup>(); 
		
		for(JobGroupType jgt : jobGroupRvc){
			
			MyJobGroup mjg=null;
			
			try{
				mjg=new MyJobGroup(jgt);
			}
			catch(ClusterException ce){
				System.out.println("Exception IGNORED: "+ce.getMessage());
				return(null);
			}
			
			
			if(mjg.getName().equals("default")==true){
				if(defFounf==false){
					newGroupDefault=mjg;
					mjg.setJobs(cacheJobs);
					defFounf=true;
				}
			}
			updateJobGroups.add(mjg);
		}
		
		newGroupDefault.setJobs(defaultJobGroup.getJobs());
		
		cacheJobGroups=updateJobGroups;
		
		return(newGroupDefault);
	}
	
	
	
	
	
	public Set<Host> updateHosts(){
		Set<Host> tmp=null;
		
		PJSMaster service=null;
		PJSMasterPort port=null;
		
		
		
		
		try{
			service=new PJSMaster(urlC,qn);
			port=service.getPJSMasterPort();
		}
		catch(Exception e){
			System.out.println("Error in contacting the server while updating");
			return(null);
		}
		
		
		
		
		
		List<String> hostName=null;
		
		try {
			hostName=port.getHostsName();
		} catch (GetHostsNameFault_Exception e) {
			System.out.println("Error in contacting the server while updating");
			return(null);
		}
		
		List<HostType> hostRvc=null;
		if(hostName==null){
			System.out.println("There are no hosts at the moment");
		}
		else{
			
			try {
				hostRvc=port.getHosts(hostName);
			} catch (GetHostsFault_Exception e) {
				System.out.println("Error in contacting the server while updating");
				return(null);
			} catch (UnknownHost_Exception e) {
				System.out.println("Unknown host requested while updating");
				return(null);
			}
			
			
			if(hostRvc==null){
				System.out.println("Hosts are empty while updating");
				return(null);
			}
			
			
			//parsing the host
			
			boolean found=false;
			
			tmp=new HashSet<Host>();
			for(HostType ht : hostRvc){
				
				MyHost mh=null;
				try{
					mh=new MyHost(ht);
				}
				catch(MyClusterException ce){
					System.out.println("Exception IGNORED: "+ce.getMessage());
					return(null);
				}
				
				if(tmp.add(mh)==false){
					System.out.println("There are 2 equal hosts while updating");
					return(null);
				}
				
				if(mh.isMaster()==true){
					if(found==true){
						System.out.println("Too many master hosts while updating");
						return(null);
					}
					found=true;
					masterHost=mh;
				}
			}
			
			
		}
		return(tmp);
	}
	
	
	@Override
	public JobGroup getDefaultJobGroup() {
		MyJobGroup updJobGroup;
		
		updJobGroup=updateJobGroup();
		
		if(updJobGroup!=null){
			if(updJobGroup.getName().equals("default")==true){
				defaultJobGroup=updJobGroup;
			}
		}
		
		return(defaultJobGroup);
	}

	@Override
	public Set<Host> getHosts() {
		Set<Host> updHosts;
		
		updHosts=updateHosts();
		
		if(updHosts!=null){
			cacheHosts=updHosts;
		}
		
		return(cacheHosts);
	}

	@Override
	public Set<JobGroup> getJobGroups() {
		MyJobGroup updJobGroup;
		
		updJobGroup=updateJobGroup();
		
		if(updJobGroup!=null){
			if(updJobGroup.getName().equals("default")==true){
				defaultJobGroup=updJobGroup;
			}
		}
		Set<JobGroup>tmp=new HashSet<JobGroup>();
		tmp.add(defaultJobGroup);
		
		return(tmp);
	}
	
	
	
	public Set<Job> updateJobs(){
		Set<Job>tmp=null;
		
		
		PJSMaster service=null;
		PJSMasterPort port=null;
		
		
		
		
		try{
			service=new PJSMaster(urlC,qn);
			port=service.getPJSMasterPort();
		}
		catch(Exception e){
			System.out.println("Error in contacting the server while updating");
			return(null);
		}
		
		
		List<BigInteger> jobsName=null;
		
		
		try {
			jobsName=port.getJobId();
		} catch (GetJobIdFault_Exception e1) {
			System.out.println("Error in contacting the server while updating");
			return(null);
		}
	
		
		List<JobType> jobRvc=null;
		
		if(jobsName==null){
			System.out.println("There are no jobs at the moment");
		}
		else{
			
			
			try {
				jobRvc=port.getJobs(jobsName);
			} catch (GetJobsFault_Exception e) {
				System.out.println("Error in contacting the server while updating");
				return(null);
			} catch (UnknownJob_Exception e) {
				System.out.println("Unknown job requested whilre updating");
				return(null);
			}
		

			if(jobRvc==null){
				System.out.println("Job are empty");
				return(null);
			}
			
			
			tmp=new HashSet<Job>();
			
			//parsing the job
			for(JobType jt : jobRvc){
				
				
				MyJob mj=null;
				try{
					mj=new MyJob(jt,this,defaultJobGroup);
				}catch(MyClusterException ce){
					System.out.println("Exception IGNORED: "+ce.getMessage());
					return(null);
				}
				
				if(tmp.add(mj)==false){
					System.out.println("Error: jobId collision while updating");
					return(null);
				}
			}
			
		}
		
		
		return(tmp);
	}
	
	

	@Override
	public Set<Job> getJobs(boolean arg0) {
		Set<Job> updJobs;
		
		updJobs=updateJobs();
		
		if(updJobs!=null){
			cacheJobs=updJobs;
		}
		
		if(arg0==false){
			Set<Job> tmp=new HashSet<Job>();
			
			for(Job j : cacheJobs){
				if(j.getState().equals("RUNNING")==true || j.getState().equals("PENDING")==true || j.getState().equals("SUSPENDED")==true){
					tmp.add(j);
				}
			}
			return(tmp);
		}
		
		return(cacheJobs);
	}
	
	
	

	@Override
	public Host getMasterHost() {
		Set<Host> updHosts;
		
		updHosts=updateHosts();
		
		if(updHosts!=null){
			cacheHosts=updHosts;
		}
		
		return(masterHost);
	}
	
	
	

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	
	
	
	@Override
	public int getNumberOfHosts() {
		Set<Host> updHosts;
		
		updHosts=updateHosts();
		
		if(updHosts!=null){
			cacheHosts=updHosts;
		}
		
		return(cacheHosts.size());
	}
	
	
	

	@Override
	public int getNumberOfServers() {
		Set<Host> updHosts;
		
		updHosts=updateHosts();
		
		if(updHosts!=null){
			cacheHosts=updHosts;
		}
		
		return(cacheHosts.size());
	}
	
	
	

	@Override
	public Set<Host> getServers() {
		Set<Host> updHosts;
		
		updHosts=updateHosts();
		
		if(updHosts!=null){
			cacheHosts=updHosts;
		}
		
		return(cacheHosts);
	}
	
	
	

	@Override
	public ClusterStatus getStatus() {
		return(clusterS);
	}
	
	
	
	
	
	public Host getHostByName(String n){
		
		for(Host hh : cacheHosts){
			if(hh.getName().equals(n)==true){
				return(hh);
			}
		}
		
		return(null);
	}

}
