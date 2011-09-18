package isnork.g4.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ClusteringStrategy {

	private static final ClusteringStrategy CLUSTERING_STATERGY = new ClusteringStrategy();
	
	Map<Integer, Cluster> clusters = new HashMap<Integer, Cluster>();
	int numberOfClusters = -1;
	int maxSizeOfCluster = -1;
	int numIndependentDivers = -1;
	
	public Map<Integer, Cluster> getClusters() {
		return clusters;
	}
	public void setClusters(Map<Integer, Cluster> clusters) {
		this.clusters = clusters;
	}
	public int getNumberOfClusters() {
		return numberOfClusters;
	}
	public void setNumberOfClusters(int numberOfClusters) {
		this.numberOfClusters = numberOfClusters;
	}
	public int getMaxSizeOfCluster() {
		return maxSizeOfCluster;
	}
	public void setMaxSizeOfCluster(int maxSizeOfCluster) {
		this.maxSizeOfCluster = maxSizeOfCluster;
	}

	private ClusteringStrategy(){
	}
	
	public static ClusteringStrategy getInstance(){
		return CLUSTERING_STATERGY;
	}
	
	public void initialize(int d, int n, int playerId){
		numberOfClusters = n;
		maxSizeOfCluster = 1;
		numIndependentDivers = n;
		addDiverToCluster(playerId);
	}
	
	public void addDiverToCluster(int diverId){
		int clusterId = Math.abs(diverId)%numberOfClusters;
		Cluster cluster = clusters.get(clusterId);
		if(cluster != null){
			cluster.addDiver(diverId, clusterId);
		} else {
			System.out.println("creating new custer with " + clusterId + " - " + diverId);
			clusters.put(clusterId, new Cluster(diverId, Math.abs(diverId)%numberOfClusters));
		}
	}
	
	@Override
	public String toString() {
		System.out.println(" mmmm " + clusters.size());
		StringBuilder sb = new StringBuilder();
		sb.append("CLUSTERING STATERGY - toString ------ \n");
		int refClusterId;
		Cluster refCluster = null;
		Iterator<Integer> iteratorClusterId = clusters.keySet().iterator();
		while(iteratorClusterId.hasNext()){
			refClusterId = iteratorClusterId.next();
			refCluster = clusters.get(refClusterId);
			sb.append(refCluster.toString());
		}
		sb.append("\n");
		return sb.toString();
	}
	
}
