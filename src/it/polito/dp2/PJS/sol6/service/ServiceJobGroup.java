package it.polito.dp2.PJS.sol6.service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import it.polito.dp2.PJS.Job;
import it.polito.dp2.PJS.JobGroup;

public class ServiceJobGroup implements JobGroup {

	String name;
	String description;
	
	private volatile Set<ServiceJob> jobs;
	
	
	public ServiceJobGroup(String name,String description){
		
        //Synchronized Set: this set is accessed in concurrency
		jobs=Collections.synchronizedSet(new HashSet<ServiceJob>());
		
		this.name=name;
		if(description!=null){
			this.description=description;
		}
	}
	
	
	
	

	
	
	
	
	
	//Add a job to the jobGroup
	public synchronized boolean addJob(ServiceJob j){		
		if(jobs.add(j)==false){
			System.out.println("Error while adding job");
			return(false);
		}
		return(true);
	}
	
	
	
	
	
	
	
	
	
	
	@Override
	public String getDescription() {
		if(description!=null){
			return(description);
		}
		return(null);
	}

	
	
	
	
	
	
	@Override
	public Set<Job> getJobs() {
		return(null);
	}
	
	
	
	
	

	public Set<ServiceJob> getJobsB() {
			return(jobs);
	}
	
	

	@Override
	public String getName() {
		return(name);
	}
	
	
	
	
	
	public boolean equals(Object obj){
		if(!(obj instanceof ServiceJobGroup)){
			return false;
		}
		
		ServiceJobGroup jobGr =(ServiceJobGroup)obj;
		return(jobGr.getName().equals(name));	
	}
	

	
	
	
	
	
	
	
	
	
	
	

}
