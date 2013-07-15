package it.polito.dp2.PJS.sol6.client1;



import it.polito.dp2.PJS.ClusterException;
import it.polito.dp2.PJS.Host;
import it.polito.dp2.PJS.sol6.client1.gen.jaxws.ClientType;
import it.polito.dp2.PJS.sol6.client1.gen.jaxws.HostType;
import it.polito.dp2.PJS.sol6.client1.gen.jaxws.ServerType;

public class MyHost implements Host{

	String nameH;
	HostStatus status;
	String type;
	int memory,load;
	
	public MyHost(HostType h) throws MyClusterException{
		
		
		
		nameH=h.getNameH();

		
		//Check if host is client
		ClientType tp=null;
		tp=h.getClient();
		
		
		
		if(tp!=null){
			String sstatus=tp.getStatus().toString();
			if(sstatus.equals("OK")==false){
				System.out.println("Client with wrong status");
				throw new MyClusterException("Host with wrong status");
			}
			status=HostStatus.OK;
			type="Client";
		}
		
		
		ServerType st=null;
		st=h.getServer();
		
		
		
		if(st!=null){
			String sstatus=st.getStatus().toString();
			
			System.out.println("Is server with status ?? "+ sstatus);
			
			if(sstatus.equals("OK")==true){
				status=HostStatus.OK;
			}
			else{
				if(sstatus.equals("UNAVAIL")==true){
					status=HostStatus.UNAVAIL;
				}
				else{
					if(sstatus.equals("UNLICENSED")==true){
						status=HostStatus.UNLICENSED;
					}
					else{
						if(sstatus.equals("CLOSED")==true){
							status=HostStatus.CLOSED;
						}
						else{
							System.out.println("Server with wrong status");
							throw new MyClusterException("Server with wrong status");
						}
					}
				}
			}
			memory=st.getMemory().intValue();
			load=st.getLoad().intValue();
			
			System.out.println("THIS IS:"+st.getType().toString());
			
			if(st.getType().toString()=="MASTER"){
				type="Master";
			}
			else{
				if(st.getType().toString()=="SERVER"){
					type="Server";
				}	
			}
			
			
			if(memory<0 || load<0){
				System.out.println("Wrong value for memory/load");
				throw new MyClusterException("Host with wrong meory/load value");
			}
			
		}
		
		
		if(st==null && tp==null){
			System.out.println("Invalid host");
			throw new MyClusterException("Invalid host");
		}
		
		
	}
	
	
	
	
	
	@Override
	public int getLoad() {
		return(load);
	}

	@Override
	public String getName() {
		return(nameH);
	}

	@Override
	public int getPhysicalMemory() {
		return(memory);
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
		if(type.equals("Master")==true || type.equals("Server")==true){
			return(true);
		}
		return(false);
	}

}
