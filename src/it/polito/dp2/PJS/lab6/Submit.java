package it.polito.dp2.PJS.lab6;

public interface Submit {
	
	/* Submit a new job 
	 * 
	 * Arguments:
	 * host		the submission host name
	 * cmdline	the command-line string to run the job
	 * stdin	the standard-input string to be passed to the job
	 * Returns:
	 * on success: a unique id (positive integer) assigned to the submitted job by the system
	 * on failure: -1
	 * 
	 */
	int submit(String host, String cmdline, String stdin);

}
