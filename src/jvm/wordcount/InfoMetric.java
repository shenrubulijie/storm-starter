package wordcount;

import java.net.InetAddress;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.hyperic.sigar.ProcCpu;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

import backtype.storm.metric.api.IMetric;

public class InfoMetric implements IMetric{
	private String port;
	@Override
	public Object getValueAndReset() {
		// TODO Auto-generated method stub
		System.setProperty("java.library.path", "/home/miller/research/storm/storm-starter-3/storm-starter/hyperic-sigar-1.6.3/sigar-bin/lib");
	      
		Sigar sigar = new Sigar();
		long pid = sigar.getPid();
		System.out.println(pid);
		int i=0;
		double ret = 0;
		String s="";
		ProcCpu procCpu = new ProcCpu();
		try {
			procCpu=sigar.getProcCpu(pid);
			Thread.sleep(1000L);
			ret=sigar.getProcCpu(pid).getPercent();
			s+=ret+" ";
			Thread.sleep(1000L);
			ret=sigar.getProcCpu(pid).getPercent();
			s+=ret+" ";
//			Thread.sleep(1000L);
//			ret=sigar.getProcCpu(pid).getPercent();
//			s+=ret+" ";
//			Thread.sleep(1000L);
//			ret=sigar.getProcCpu(pid).getPercent();
//			s+=ret+" ";
//			Thread.sleep(1000L);
//			ret=sigar.getProcCpu(pid).getPercent();
//			s+=ret+" ";
			Thread.sleep(1000L);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		NumberFormat format = new DecimalFormat("0");
		 s += ret;
		 InetAddress ia=null;
		 String localip="";
		try {
			ia=ia.getLocalHost();
			localip=ia.getHostAddress();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 String retS =localip+":"+port+":"+format.format(ret * 100);
		//String retS = s;
		return retS+" ";
	}

	public InfoMetric(String port){
		this.port=port;
	}
	public void update(String port) {
		// TODO Auto-generated method stub
		this.port=port;
	}

}
