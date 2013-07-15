package it.polito.dp2.PJS.sol6.client1;

import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.xml.datatype.XMLGregorianCalendar;

import it.polito.dp2.PJS.ClusterException;
import it.polito.dp2.PJS.Host;
import it.polito.dp2.PJS.Job;
import it.polito.dp2.PJS.JobGroup;
import it.polito.dp2.PJS.sol6.client1.gen.jaxws.JobType;

public class MyJob implements Job{

	int jobId;
	JobState state;
	GregorianCalendar subTime;
	String subHost;
	String execHost;
	MyCluster cluster;
	MyJobGroup jobGroup;
	
	public MyJob(JobType jt, MyCluster cluster,MyJobGroup jg) throws MyClusterException{
		
		this.cluster=cluster;
		
		
		jobId=jt.getJobID().intValue();
		
		String stateAsString=jt.getState().toString();
		if(stateAsString.equals("DONE")==true){
			state=JobState.DONE;
		}
		else{
			if(stateAsString.equals("EXIT")==true){
				state=JobState.EXIT;
			}
			else{
				if(stateAsString.equals("PENDING")==true){
					state=JobState.PENDING;
				}
				else{
					if(stateAsString.equals("RUNNING")==true){
						state=JobState.RUNNING;
					}
					else{
						if(stateAsString.equals("SUSPENDED")==true){
							state=JobState.SUSPENDED;
						}
						else{
							System.out.println("Job with wrong state");
							throw new MyClusterException("Job with wrong state");
						}
					}
				}
			}
		}
		
		
		
		execHost=jt.getExecHost();
		subHost=jt.getSubHost();
		
		
		XMLGregorianCalendar gc;
		gc=jt.getSubTime();
		
		if(gc!=null){
			subTime=gc.toGregorianCalendar();
		}
		
		jobGroup=jg;
		
	}
	
	
	
	
	
	
	@Override
	public Host getExecutionHost() {
		Host h;
		if(execHost!=null){
			h=cluster.getHostByName(execHost);
			return(h);
		}
		return(null);
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
		return null;
	}

	@Override
	public Calendar getSubmissionTime() {
		return(subTime);
	}

}
