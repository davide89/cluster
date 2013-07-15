package it.polito.dp2.PJS.sol6.client1;

public class MyClusterException extends Exception{

	String msg;
	
	public MyClusterException(String msg) {
		super(msg);
		this.msg=msg;
	}
	
	
	public String getMessage(){
		return(msg);
	}
	
}
