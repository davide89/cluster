package it.polito.dp2.PJS.lab6.tests;

import it.polito.dp2.PJS.Host;
import it.polito.dp2.PJS.lab6.tests.gen.jaxb.THost;
import it.polito.dp2.PJS.lab6.tests.gen.jaxb.THostStatus;
import it.polito.dp2.PJS.lab6.tests.gen.jaxb.THostType;

public class HostImpl implements Host {
	THost h;

	/**
	 * @param h
	 */
	public HostImpl(THost h) {
		this.h = h;
	}

	@Override
	public int getLoad() {
		return h.getLoad().intValue();
	}

	@Override
	public String getName() {
		return h.getName();
	}

	@Override
	public int getPhysicalMemory() {
		return h.getMemory().intValue();
	}

	@Override
	public HostStatus getStatus() {
		return convertStatus(h.getStatus());
	}

	private HostStatus convertStatus(THostStatus status) {
		switch(status) {
			case CLOSED : return HostStatus.CLOSED;
			case OK : return HostStatus.OK;
			case UNAVAIL : return HostStatus.UNAVAIL;
			case UNLICENSED : return HostStatus.UNLICENSED;
		}
		return null;
	}

	@Override
	public boolean isMaster() {
		return (h.getType())==THostType.MASTER;
	}

	@Override
	public boolean isServer() {
		return (h.getType())==THostType.SERVER || (h.getType()) ==THostType.MASTER;
	}
	
	public String getURI() {
		return h.getUri();
	}
}
