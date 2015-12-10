package wordcount;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;

import org.apache.log4j.Logger;
import org.hyperic.sigar.Sigar;

import backtype.storm.metric.api.CountMetric;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;

public class WordCounter extends BaseBasicBolt implements Serializable{
    private static final long serialVersionUID = 5683648523524179434L;
    private HashMap<String, Integer> counters = new HashMap<String, Integer>();
    private volatile boolean edit = false;
    transient InfoMetric _countMetric;
    private long port;
    //private Logger logger = Logger.getLogger(WordCounter.class);
    @Override
    @SuppressWarnings("rawtypes")
    public void prepare(Map stormConf, TopologyContext context) {
    	System.setProperty("java.library.path", "/home/miller/research/storm/storm-starter-3/storm-starter/hyperic-sigar-1.6.3/sigar-bin/lib");
        
    	System.out.println("prepare called");
    	 initMetrics(context);
        final long timeOffset = Long.parseLong(stormConf.get("TIME_OFFSET").toString());
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (edit) {
                        for (Entry<String, Integer> entry : counters.entrySet()) {
                            System.out.println(entry.getKey() + " : " + entry.getValue());
                        }
                        System.out.println("WordCounter---------------------------------------");
                        edit = false;
                    }
                    try {
                        Thread.sleep(timeOffset * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        String str = input.getString(0);
        updateMetrics();
        if (!counters.containsKey(str)) {
            counters.put(str, 1);
        } else {
            Integer c = counters.get(str) + 1;
            counters.put(str, c);
        }
        edit = true;
        //logger.info("Ok, EvenScheduler succesfully called");
        System.out.println("WordCounter+++++++++++++++++++++++++++++++++++++++++++");
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }
    void initMetrics(TopologyContext context)
    {
    	System.setProperty("java.library.path", "/home/miller/research/storm/storm-starter-3/storm-starter/hyperic-sigar-1.6.3/sigar-bin/lib");
    	port = context.getThisWorkerPort();
    	Sigar sigar = new Sigar();
		long pid = sigar.getPid();
        _countMetric = new InfoMetric(""+port);
        context.registerMetric("execute_count", _countMetric, 3);
    }

  //更新计数器
    void updateMetrics()
    {
    	System.setProperty("java.library.path", "/home/miller/research/storm/storm-starter-3/storm-starter/hyperic-sigar-1.6.3/sigar-bin/lib");
    	
    	Sigar sigar = new Sigar();
		long pid = sigar.getPid();
        _countMetric.update(""+port);
    }
}