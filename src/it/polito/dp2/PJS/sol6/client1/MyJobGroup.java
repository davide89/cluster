package it.polito.dp2.PJS.sol6.client1;

import java.util.Set;

import it.polito.dp2.PJS.Job;
import it.polito.dp2.PJS.JobGroup;
import it.polito.dp2.PJS.sol6.client1.gen.jaxws.JobGroupType;

public class MyJobGroup implements JobGroup{

	String nameJobGroup;
	String description;
	Set<Job> jobs;
	
	
	public MyJobGroup(JobGroupType jgt){
		
		this.nameJobGroup=jgt.getNameJG();
		
		if(jgt.getDescription()!=null){
			this.description=jgt.getDescription();
		}
		
	}
	
	
	
	
	
	
	
	
	
	@Override
	public String getDescription() {
		return(description);
	}

	@Override
	public Set<Job> getJobs() {
		return(jobs);
	}

	@Override
	public String getName() {
		return(nameJobGroup);
	}
	
	
	public void setJobs(Set<Job> j){
		jobs=j;
	}

}
