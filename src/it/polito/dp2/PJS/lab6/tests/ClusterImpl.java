package it.polito.dp2.PJS.lab6.tests;

import java.io.FileInputStream;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import it.polito.dp2.PJS.Cluster;
import it.polito.dp2.PJS.Host;
import it.polito.dp2.PJS.Job;
import it.polito.dp2.PJS.JobGroup;
import it.polito.dp2.PJS.lab6.tests.gen.jaxb.Hosts;
import it.polito.dp2.PJS.lab6.tests.gen.jaxb.THost;

public class ClusterImpl implements Cluster {
	private Set<Host> hosts = new HashSet<Host>();
	
	public ClusterImpl() throws Exception {
		
        JAXBContext jc = JAXBContext.newInstance( "it.polito.dp2.PJS.lab6.tests.gen.jaxb" );
        Unmarshaller u = jc.createUnmarshaller();
        Hosts hlist = (Hosts)u.unmarshal( new FileInputStream( "xml/execHosts.xml" ) );
		for (THost h : hlist.getHost()) {
			hosts.add(new HostImpl(h));
		}
	}

	@Override
	public JobGroup getDefaultJobGroup() {
		return null;
	}

	@Override
	public Set<Host> getHosts() {
		return hosts;
	}

	@Override
	public Set<JobGroup> getJobGroups() {
		return null;
	}

	@Override
	public Set<Job> getJobs(boolean arg0) {
		return null;
	}

	@Override
	public Host getMasterHost() {
		return null;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public int getNumberOfHosts() {
		return hosts.size();
	}

	@Override
	public int getNumberOfServers() {
		return 0;
	}

	@Override
	public Set<Host> getServers() {
		return null;
	}

	@Override
	public ClusterStatus getStatus() {
		return ClusterStatus.OK;
	}
}
