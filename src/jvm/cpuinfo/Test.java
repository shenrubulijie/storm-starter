package cpuinfo;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.hyperic.sigar.ProcCpu;
import org.hyperic.sigar.Sigar;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		
//		System.out.println("Number of cores: " + CPUInfo.getInstance().getNumberOfCores());
//		System.out.println("Totale speed: " + CPUInfo.getInstance().getTotalSpeed() + " Hz");
//		System.out.println("Cores:");
//		for (int i = 0; i < CPUInfo.getInstance().getNumberOfCores(); i++)
//			System.out.println(CPUInfo.getInstance().getCoreInfo(i));
		Sigar sigar = new Sigar();
		long pid = sigar.getPid();
		System.out.println(pid);
		int i=0;
		while(i<10000){
		//ProcCpu procCpu = sigar.getProcCpu(pid);new ProcCpu();
		ProcCpu procCpu = new ProcCpu();
		procCpu.gather(sigar, pid);
		Thread.sleep(1000); 
		System.out.println(procCpu.getPercent());
		for(int m=0;m<i;m++){
		int j = i*i*i*i*i*i*i*i;
		}
		}
//		Map map = sigar.getProcEnv(pid);
//		Set s=map.entrySet();
//		Iterator it = s.iterator();
//		while(it.hasNext()){
//			System.out.println(it.next()+" "+map.get(it.next()));
//		}
	}

}
