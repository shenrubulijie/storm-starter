package scheduler;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import backtype.storm.generated.Bolt;
import backtype.storm.generated.StormTopology;
import backtype.storm.scheduler.Cluster;
import backtype.storm.scheduler.EvenScheduler;
import backtype.storm.scheduler.ExecutorDetails;
import backtype.storm.scheduler.IScheduler;
import backtype.storm.scheduler.SupervisorDetails;
import backtype.storm.scheduler.Topologies;
import backtype.storm.scheduler.TopologyDetails;
import backtype.storm.scheduler.WorkerSlot;

public class TestScheduler implements IScheduler{
	private Logger logger = Logger.getLogger(TestScheduler.class);
	private Master master;

    private Map<Integer,Integer> executorToSlotCount = new Hashtable<Integer,Integer>();
    private Map<Integer,List<ExecutorDetails>> executorToSlotList = new Hashtable<Integer,List<ExecutorDetails>>();
    private List<WorkerSlot> workerSlotList = new LinkedList<WorkerSlot>();
	@Override
	public void prepare(Map arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void schedule(Topologies topologies, Cluster cluster) {
		// TODO Auto-generated method stub
		logger.info("Test Scheduler");
		logger.info("+++++++++++++++++++++++++++");
		System.out.println("++++++++++++++++++++++WorkerSlot list:+++++++++++++++++");
		for(SupervisorDetails supervisor : cluster.getSupervisors().values()){
			List<WorkerSlot> availableSlots = cluster.getAvailableSlots(supervisor);
			String s = supervisor.getHost();
			for(WorkerSlot ws :availableSlots){
				System.out.println(s+":"+ws.getPort());
			}
		}
		System.out.println("----------------------WorkerSlot end--------------------");
//		for (TopologyDetails topology : topologies.getTopologies()) {
//			StormTopology st = topology.getTopology();
//			int executorCount=topology.getExecutors().size();
//			if (cluster.needsScheduling(topology)) {
//				int numOfWorker = topology.getNumWorkers();
//				WorkerSlot[] allocatedWorkerSlot = new WorkerSlot[numOfWorker];
//				for(SupervisorDetails supervisor : cluster.getSupervisors().values()){
//					List<WorkerSlot> availableSlots = cluster.getAvailableSlots(supervisor);
//					for(WorkerSlot ws :availableSlots){
//						System.out.println(ws.getNodeId());
//					}
//				}
//			}
			
//		}
		Master server = Master.getInstance();
		new EvenScheduler().schedule(topologies, cluster);
	}

}
