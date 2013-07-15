package it.polito.dp2.PJS.lab6.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import it.polito.dp2.PJS.Cluster;
import it.polito.dp2.PJS.ClusterFactory;
import it.polito.dp2.PJS.Host;
import it.polito.dp2.PJS.Job;
import it.polito.dp2.PJS.JobGroup;
import it.polito.dp2.PJS.lab6.Submit;
import it.polito.dp2.PJS.lab6.tests.gen.jaxws.PJSDispatch;
import it.polito.dp2.PJS.lab6.tests.gen.jaxws.PJSDispatchService;
import it.polito.dp2.PJS.lab6.tests.gen.jaxws.ServiceUnavailable_Exception;
import it.polito.dp2.PJS.lab6.tests.gen.jaxws.UnknownJob_Exception;

import java.math.BigInteger;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.ws.BindingProvider;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class MasterTests {

    class HostComparator implements Comparator<Host> {
        public int compare(Host h0, Host h1) {
            return h0.getName().compareTo(h1.getName());
        }

    }

    class JobGroupComparator implements Comparator<JobGroup> {
        public int compare(JobGroup g0, JobGroup g1) {
            return g0.getName().compareTo(g1.getName());
        }

    }

    class JobComparator implements Comparator<Job> {
        public int compare(Job j0, Job j1) {

            return j0.getID() - j1.getID();
        }
    }

    private static Cluster controlCluster;
    private static Cluster testCluster;
    private static Submit testSubmit;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        System.setProperty("it.polito.dp2.PJS.ClusterFactory", "it.polito.dp2.PJS.lab6.tests.ClusterFactory");
        controlCluster = ClusterFactory.newInstance().newCluster();

        System.setProperty("it.polito.dp2.PJS.ClusterFactory", "it.polito.dp2.PJS.sol6.client1.ClusterFactory");
        testCluster = ClusterFactory.newInstance().newCluster();
        
        testSubmit = new it.polito.dp2.PJS.sol6.client2.Client2();
    }

    @Before
    public void setUp() throws Exception {
        assertNotNull(controlCluster);
        assertNotNull(testCluster);
        assertNotNull(testSubmit);
    }

    private void compareHost(Host host1, Host host2) {
        assertNotNull(host1);
        assertNotNull(host2);

        assertNotNull("NULL Host Name", host2.getName());
        assertEquals("Wrong Host Name", host1.getName(), host2.getName());
        assertEquals("Wrong Status for Host " + host2.getName(), host1.getStatus(), host2.getStatus());
        assertEquals("Wrong Master Flag for Host " + host2.getName(), host1.isMaster(), host2.isMaster());
        assertEquals("Wrong Server Flag for Host " + host2.getName(), host1.isServer(), host2.isServer());
        assertEquals("Wrong Physical Memory for Host " + host2.getName(), host1.getPhysicalMemory(), host2.getPhysicalMemory());
        assertEquals("Wrong Load for Host " + host2.getName(), host1.getLoad(), host2.getLoad());
    }
    
    @Test
    public final void testGetHosts() {
        Set<Host> s1 = controlCluster.getHosts();
        Set<Host> s2 = testCluster.getHosts();

        if ((s1 == null) && (s2 != null)) {
            fail();
            return;
        }

        if ((s1 == null) && (s2 == null)) {
            assertTrue("There are no hosts!", true);
            return;
        }

        assertEquals("Wrong Number of Hosts", s1.size(), s2.size());
        assertEquals("Wrong Number of Hosts", testCluster.getNumberOfHosts(), s1.size());
        assertEquals("Wrong Number of Hosts", testCluster.getNumberOfHosts(), s2.size());

        TreeSet<Host> ts1 = new TreeSet<Host>(new HostComparator());
        TreeSet<Host> ts2 = new TreeSet<Host>(new HostComparator());

        ts1.addAll(s1);
        ts2.addAll(s2);

        Iterator<Host> i1 = ts1.iterator();
        Iterator<Host> i2 = ts2.iterator();

        while (i1.hasNext() && i2.hasNext()) {
            compareHost(i1.next(), i2.next());
        }
    }

    @Test
    public final void testGetNumberOfHosts() {
        assertEquals("Wrong Number of Hosts", controlCluster.getNumberOfHosts(), testCluster.getNumberOfHosts());
    }
    
    @Test
    public final void testSubmit() {
    	Set<Job> jset = testCluster.getJobs(true);
    	
    	// check that initially the set of jobs is empty
    	assertEquals("Wrong initial size for the set of jobs",0,jset.size());
    	
    	// check that there is at least one host
    	if (controlCluster.getNumberOfHosts()==0)
    		fail("This test can run only if there is at least one host");
    	String subHost = controlCluster.getHosts().iterator().next().getName();
    	
    	testOneSubmit(subHost, "cmdline1", "stdin1", false);
    	testOneSubmit(subHost, "cmdline2", "stdin2", false);
    	testOneSubmit(subHost, "cmdline3", "stdin3", false);
    	testOneSubmit(subHost, "cmdline4", "stdin4", false);
    	testOneSubmit(subHost, "cmdline5", "stdin5", true);
    	
    }
    
    private void testOneSubmit(String subHost, String cmdline, String stdin, boolean expectedFail) {
    	// get current number of jobs
    	Set<Job> jset = testCluster.getJobs(true);
    	int jnum = jset.size();
    	
    	// try to submit the job
    	int id = testSubmit.submit(subHost, cmdline, stdin);

    	if (expectedFail) {
    		// check submit return value equals the expected value
    		assertEquals("Wrong submit return value", -1, id);
    	}
    	else {
    		// check submit return value equals the expected value
    		assertTrue("Submit return value should be positive", id>0);
        	
    		// get all jobs after operation
        	jset = testCluster.getJobs(true);
        	
        	// check the number of jobs has been incremented
        	assertEquals("Wrong number of jobs after submission",jnum+1,jset.size());
        	
        	// check if a job with id equal to the returned value is in the set of jobs
        	Job job = findJob(jset,id);
        	assertNotNull("No job found after submission",job);
        	
        	// get execution host and check it is non null
        	Host eh = job.getExecutionHost();
        	assertNotNull("Null execution host",eh);
        	
        	// get name of execution host and check it is non null
        	String name = eh.getName();
        	assertNotNull("Null name of execution host",name);
        	
        	// get URI of PJSDispatch (and check execution host name is right)
        	String uri = getPJSDispatchURI(name) + "/PJSDispatchService";
        	assertNotNull("Wrong name of execution host",uri);
        	
        	// contact execution host web service and check job has been correctly dispatched
        	PJSDispatchService s = new PJSDispatchService();
    		PJSDispatch proxy = s.getPJSDispatchPort();
    		BindingProvider bp = (BindingProvider)proxy;
        	bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, uri);
        	try {
				proxy.suspend(BigInteger.valueOf(id));
			} catch (ServiceUnavailable_Exception e) {
				fail("Internal error");
			} catch (UnknownJob_Exception e) {
				fail("Job "+id +" not correctly dispatched to host "+name);
			}
    	}
    }

    // get URI of execution host from host name
	private String getPJSDispatchURI(String name) {
		Set<Host> s = controlCluster.getHosts();
		for (Host h:s)
			if(h.getName().equals(name))
				return ((HostImpl)h).getURI();
		return null;
	}

	// get Job with given id in given job set
	private Job findJob(Set<Job> jset, int id) {
		for (Job j: jset)
			if ((j.getID())==id)
				return j;
		return null;
	}

}
