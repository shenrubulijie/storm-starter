package storm.scheduler;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import backtype.storm.scheduler.Cluster;
import backtype.storm.scheduler.EvenScheduler;
import backtype.storm.scheduler.ExecutorDetails;
import backtype.storm.scheduler.IScheduler;
import backtype.storm.scheduler.SchedulerAssignment;
import backtype.storm.scheduler.SupervisorDetails;
import backtype.storm.scheduler.Topologies;
import backtype.storm.scheduler.TopologyDetails;
import backtype.storm.scheduler.WorkerSlot;


public class DemoScheduler implements IScheduler {
    public void prepare(Map conf) {}

    private int flag=0;
    private void Myschedule(Topologies topologies, Cluster cluster)
    {
    	
    	 SchedulerAssignment currentAssignment = cluster.getAssignmentById(topologies.getByName("special-topology").getId());
         if (currentAssignment != null) {
                 System.out.println("MY:current assignments: " + currentAssignment.getExecutorToSlot());
         } else {
                 System.out.println("My:current assignments: {}");
         }
         
         SupervisorDetails specialSupervisor= GetSupervisorDetailsByName(cluster,"special-slave3");
         if(specialSupervisor!=null)
         {
         List<WorkerSlot>  availableSlots = cluster.getAvailableSlots(specialSupervisor);

         System.out.println("availableSlotsNum:"+availableSlots.size());
         System.out.println("availableSlotsNum List:"+availableSlots);
       
         TopologyDetails topology = topologies.getByName("special-topology");
        
         Map<String, List<ExecutorDetails>> componentToExecutors = cluster.getNeedsSchedulingComponentToExecutors(topology);
         List<ExecutorDetails> executors = componentToExecutors.get("1");
         List<ExecutorDetails> executors2 = componentToExecutors.get("2");
        
         Map<String, SupervisorDetails> AllSupervisors= cluster.getSupervisors();
         Collection<SupervisorDetails>AllSuperVaule= AllSupervisors.values();
        
         SupervisorDetails[] superArray=new SupervisorDetails[AllSuperVaule.size()];
         AllSuperVaule.toArray(superArray);
         
         ArrayList<ExecutorDetails> AllExecutor=new ArrayList<ExecutorDetails>();
         
         for(int i=0;i<executors.size();i++)
         {
        	 AllExecutor.add(executors.get(i));
        	 AllExecutor.add(executors2.get(i));
         }
         
         System.out.println("AllExecutor size:"+AllExecutor.size()+" "+superArray.length);
        for(int i=0;i<superArray.length;i++)
         {
        	 List<ExecutorDetails> temp=AllExecutor.subList(i*5, i*5+5);
        	 
        	 List<WorkerSlot>  availableSlotsInner = cluster.getAvailableSlots(superArray[i]);
        	 cluster.assign(availableSlotsInner .get(0), topology.getId(), temp);
        	 System.out.println("Assiment:"+temp+"to"+i);
        	 
         }
         
       //  cluster.assign(availableSlots.get(1), topology.getId(), executors);
        // cluster.assign(availableSlots.get(2), topology.getId(), executors2);
        
         }
         else
         {
        	 System.out.println("special-slave3 is not exits!!!");
         }
    }
    private SupervisorDetails  GetSupervisorDetailsByName(Cluster cluster,String SupervisorName)
    {
        // find out the our "special-supervisor" from the supervisor metadata
        Collection<SupervisorDetails> supervisors = cluster.getSupervisors().values();
        SupervisorDetails specialSupervisor = null;
        for (SupervisorDetails supervisor : supervisors) {
            Map meta = (Map) supervisor.getSchedulerMeta();

            if (meta.get("name").equals(SupervisorName)) {
                specialSupervisor = supervisor;
                break;
            }
        }
        
        return specialSupervisor;
    }
    public void schedule(Topologies topologies, Cluster cluster) {
    	
 
            System.out.println("DemoScheduler: begin scheduling");
        // Gets the topology which we want to schedule
            TopologyDetails topology = topologies.getByName("special-topology");

        // make sure the special topology is submitted,
        if (topology != null) {
        	System.out.println("special-topology is  not null!!!");
        	
if(flag==0)
{
      
            boolean needsScheduling = cluster.needsScheduling(topology);
           // cluster.n
            if (needsScheduling) {
                    System.out.println("Our special topology DOES NOT NEED scheduling.");
            } else {
                    System.out.println("Our special topology needs scheduling.");
                // find out all the needs-scheduling components of this topology
                    
                    
                    
                    Collection<SupervisorDetails> Tempsupervisors = cluster.getSupervisors().values();//d
                    
                    
                    for (SupervisorDetails supervisor : Tempsupervisors) {

                    
                    	
                    	
                    	   List<WorkerSlot> availableSlots = cluster.getAvailableSlots(supervisor);
                    	   //
                    	  int Availablenum =availableSlots.size();
                    	  String suName=supervisor.getHost();
                    	  
                    	  System.out.println("before:HostName:"+suName+" AvailableNum:"+Availablenum);
                    	  
                    	   if(!availableSlots.isEmpty())
                    	   {
                    		   for (Integer port : cluster.getUsedPorts(supervisor)) {
                                   cluster.freeSlot(new WorkerSlot(supervisor.getId(), port));
                               }
                    	   }
                    	   List<WorkerSlot> availableSlots2 = cluster.getAvailableSlots(supervisor);
                    	   
                     	  int Availablenum2 =availableSlots2.size();
                     	 
                     	  
                     	  System.out.println("after:HostName:"+suName+" AvailableNum:"+Availablenum2);
                         
                        }
                    
                    
                    
                    
                    
                Map<String, List<ExecutorDetails>> componentToExecutors = cluster.getNeedsSchedulingComponentToExecutors(topology);
                
                System.out.println("needs scheduling(component->executor): " + componentToExecutors);
                System.out.println("needs scheduling(executor->compoenents): " + cluster.getNeedsSchedulingExecutorToComponents(topology));
                SchedulerAssignment currentAssignment = cluster.getAssignmentById(topologies.getByName("special-topology").getId());
                if (currentAssignment != null) {
                        System.out.println("current assignments: " + currentAssignment.getExecutorToSlot());
                } else {
                        System.out.println("current assignments: {}");
                }
                
              
                
                
                if (!componentToExecutors.containsKey("spout")) {
                        System.out.println("Our special-spout DOES NOT NEED scheduling.");
                } else {
                    System.out.println("Our special-spout needs scheduling.");
                    List<ExecutorDetails> executors = componentToExecutors.get("spout");
 
                    
                    
                    
                    
                    // find out the our "special-supervisor" from the supervisor metadata
                    Collection<SupervisorDetails> supervisors = cluster.getSupervisors().values();
                    SupervisorDetails specialSupervisor = null;
                    for (SupervisorDetails supervisor : supervisors) {
                        Map meta = (Map) supervisor.getSchedulerMeta();

                        if (meta.get("name").equals("special-slave2")) {
                            specialSupervisor = supervisor;
                            break;
                        }
                    }

                    // found the special supervisor
                    if (specialSupervisor != null) {
                            System.out.println("Found the special-supervisor");
                        List<WorkerSlot> availableSlots = cluster.getAvailableSlots(specialSupervisor);
                        
                        // if there is no available slots on this supervisor, free some.
                        // TODO for simplicity, we free all the used slots on the supervisor.
                        if (availableSlots.isEmpty() && !executors.isEmpty()) {
                            for (Integer port : cluster.getUsedPorts(specialSupervisor)) {
                                cluster.freeSlot(new WorkerSlot(specialSupervisor.getId(), port));
                            }
                        }

                        // re-get the aviableSlots
                        availableSlots = cluster.getAvailableSlots(specialSupervisor);

                        // since it is just a demo, to keep things simple, we assign all the
                        // executors into one slot.
                        cluster.assign(availableSlots.get(0), topology.getId(), executors);
                        Myschedule(topologies, cluster);
                        flag=1;
                        System.out.println("We assigned executors:" + executors + " to slot: [" + availableSlots.get(0).getNodeId() + ", " + availableSlots.get(0).getPort() + "]");
                    } else {
                            System.out.println("There is no supervisor named special-supervisor!!!");
                    }
                }
            }
            
            
        }//end flag==0
else
{
	System.out.println(" only do once :"+flag);
	}
    }//end special=null
       
        // let system's even scheduler handle the rest scheduling work
        // you can also use your own other scheduler here, this is what
        // makes storm's scheduler composable.
        	System.out.println("using the default system Schedule!!!");
        new EvenScheduler().schedule(topologies, cluster);
        
       
      
    }
    	
    }//end class
