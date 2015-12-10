package cpuinfo;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Random;

import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

public class Test2 {
public static void main(String[] args) throws SigarException {

		

		final Random rand = new Random();

		

		new Thread() {

			

			public void run() {

				

				while(true) {

					

					if(rand.nextBoolean()) {

						

						for (int i = 0, l = 10000000; i < l; i++) {

							rand.nextDouble();

						}

						

					} else {

						

						try {

							Thread.sleep(1000L);

						} catch (InterruptedException e) {

							e.printStackTrace();

						}

						

					}

				}

			}

			

		}.start();

		

		NumberFormat format = new DecimalFormat("0");

		Sigar sigar = new Sigar();

		long pid = sigar.getPid(); //javaw的pid 或者注释掉上边的Thread，在资源管理器看pid

		int availableProcessors = Runtime.getRuntime().availableProcessors();

		

		while (true) {

			

			System.out.println(format.format(sigar.getProcCpu(9382).getPercent() / availableProcessors * 100) + '%');

			

			try {

				Thread.sleep(1000L);

			} catch (InterruptedException e) {

				e.printStackTrace();

			}

			

		}

		

	}
}
