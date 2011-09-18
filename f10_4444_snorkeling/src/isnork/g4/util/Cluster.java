package isnork.g4.util;

import java.util.HashSet;
import java.util.Set;

public class Cluster {
	
	Set<Integer> diverIds = new HashSet<Integer>();
	int clusterId = -1;
	
	public Cluster(int diverId, int clusterId) {
		this.clusterId = clusterId;
		diverIds.add(diverId);
	}
	
	public void addDiver(int diverId, int clusterId){
		if(this.clusterId == clusterId){
			diverIds.add(diverId);
		} else {
			System.err.println(" --- Wrong Cluster Id in Cluster.addDiver() --- ");
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("ClusterId = " + this.clusterId + " --- DiverIds = ");
		for(int i : diverIds){
			sb.append(i + ", ");
		}
		sb.append("\n");
		return sb.toString();
	}
}
