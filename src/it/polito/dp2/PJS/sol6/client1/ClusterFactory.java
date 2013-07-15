package it.polito.dp2.PJS.sol6.client1;

import it.polito.dp2.PJS.Cluster;
import it.polito.dp2.PJS.ClusterException;


public class ClusterFactory extends it.polito.dp2.PJS.ClusterFactory{

	@Override
	public Cluster newCluster() throws ClusterException {
		
		String url;
		MyCluster tmp=null;
		
		
		//check on url
			url=System.getProperty("it.polito.dp2.PJS.sol6.URL1");
			
			System.out.println("The system property URL1 is: "+url);

	
			String uurl=new String("http://localhost:8182");
			String finalUrl=null;
			
			
			if(url!=null && url.isEmpty()==false){
				finalUrl=url.concat("/PJSMaster?wsdl");
				System.out.println("Client 2 trying: "+finalUrl);
			}
			else{
				finalUrl=uurl.concat("/PJSMaster?wsdl");
				System.out.println("Client 2 trying: "+finalUrl);
			}
				
			//System.out.println("THE URL is: "+url);
			
			try{
				tmp=new MyCluster(finalUrl);
			}
			catch(MyClusterException ce){
				System.out.println("Exception: "+ce.getMessage());
				throw new ClusterException(ce.getMessage());
			}
	
	
		return(tmp);
	}
	
	
	
	
	
	
//	public static void main(String[] args) {	
//		
//		
//		String url;
//		url=System.getProperty("it.polito.dp2.PJS.sol6.URL1");
//		
//		System.out.println("The system property URL1 is: "+url);
//		
//		//if(url==null || url.trim().isEmpty()==true){
//			//url=new String("http://localhost:8182");
//			
//			//url=new String("http://localhost:8182/PJSMaster?wsdl");
//		//}
//
//		String uurl=new String("http://localhost:8182");
//		String finalUrl=null;
//		if(url!=null && url.isEmpty()==false){
//			finalUrl=url.concat("/PJSMaster?wsdl");
//			System.out.println("Client 2 trying: "+finalUrl);
//		}
//		else{
//			finalUrl=uurl.concat("/PJSJobSubmission?wsdl");
//			System.out.println("Client 2 trying: "+finalUrl);
//		}
//		
//		
//		Cluster tmp=new MyCluster(finalUrl);
//		
//		try {
//			Thread.sleep(5000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		
//		System.out.println("Try to contact");
//		System.out.println("Servers are: "+tmp.getNumberOfServers());
//		System.out.println("JOBS are: "+tmp.getJobs(true).size());
//		
//		
//		return;
//	}

}
