package it.polito.dp2.PJS.lab6.tests;

import it.polito.dp2.PJS.Cluster;
import it.polito.dp2.PJS.ClusterException;

public class ClusterFactory extends it.polito.dp2.PJS.ClusterFactory {

	@Override
	public Cluster newCluster() throws ClusterException {
		try {
			return new ClusterImpl();
		} catch (Exception e) {
			throw new ClusterException(e);
		}
	}

}
