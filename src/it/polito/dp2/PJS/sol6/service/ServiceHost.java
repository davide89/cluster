package it.polito.dp2.PJS.sol6.service;

import it.polito.dp2.PJS.Host;
import it.polito.dp2.PJS.lab6.tests.gen.jaxb.THost;

public class ServiceHost implements Host {

	int load,memory;
	String name;
	HostStatus status;
	String type;
	String uri;
	
	
	
	//Constructor for XML document
	public ServiceHost(THost h){
		
		name=h.getName();
		
		if(h.getLoad()==null || h.getMemory()==null || h.getUri()==null || h.getType()==null ){
			System.exit(1);
		}
		
		
		load=h.getLoad().intValue();
		memory=h.getMemory().intValue();
		
		
		String state=h.getStatus().toString();
		if(state.equals("OK")==true){
			status=HostStatus.OK;
		}
		else{
			if(state.equals("UNAVAIL")==true){
				status=HostStatus.UNAVAIL;
			}
			else{
				if(state.equals("UNLICENSED")==true){
					status=HostStatus.UNLICENSED;
				}
				else{
					if(state.equals("CLOSED")==true){
						status=HostStatus.CLOSED;
					}
					else{
						System.exit(1);
					}
				}
			}
		}
		uri=h.getUri();
		
		
		
		if(uri==null || uri.trim().isEmpty()==true){
			status=HostStatus.CLOSED;
		}
		
		
		String typeP;
		typeP=h.getType().toString();
		
		if(typeP.equals("MASTER")==true){
			type="Master";
		}
		else{
			if(typeP.equals("SERVER")==true){
				type="Server";
			}
			else{
				if(typeP.equals("CLIENT")==true){
					type="Client";
				}
			}
		}
		
		System.out.println("this host is: "+type);

	}
	
	
	
	//Constructor for client host
	public ServiceHost(String n){
		name=n;
		status=HostStatus.OK;
		type="Client";		
	}
	
	
	
	
	
	
	
	
	
	
	@Override
	public int getLoad() {
		if(isServer()==true || isMaster()==true){
			return(load);
		}
		return(0);
	}

	@Override
	public String getName() {
		return(name);
	}

	@Override
	public int getPhysicalMemory() {
		if(isServer()==true || isMaster()==true){
			return(memory);
		}
		return(0);
	}

	@Override
	public HostStatus getStatus() {
		return(status);
	}

	@Override
	public boolean isMaster() {
		if(type.equals("Master")==true){
			return(true);
		}
		return(false);
	}

	@Override
	public boolean isServer() {
		if(type.equals("Server")==true || isMaster()==true){
			return(true);
		}
		return(false);
	}
	
	
	
	public String getURI(){
		return(uri);
	}
	
	public void incrementLoad(){
		this.load++;
	}
	
	
	
	@Override
	public boolean equals(Object obj){
		if( !(obj instanceof ServiceHost) )
			return false;
		
		ServiceHost otherHost = (ServiceHost)obj;
		return otherHost.name.equals(name);	
	}
	

}
