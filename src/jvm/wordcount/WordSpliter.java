package wordcount;

import java.io.Serializable;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hyperic.sigar.Sigar;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class WordSpliter extends BaseBasicBolt implements Serializable{

    private static final long serialVersionUID = -5653803832498574866L;
    transient InfoMetric _countMetric;
    private long port;
    @Override
    public void prepare(Map stormConf, TopologyContext context) {
    	 initMetrics(context);
    }
    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
    	updateMetrics();
        String line = input.getString(0);
        String[] words = line.split(" ");
        for (String word : words) {
            word = word.trim();
            if (StringUtils.isNotBlank(word)) {
                word = word.toLowerCase();
                collector.emit(new Values(word));
            }
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("word"));

    }
    void initMetrics(TopologyContext context)
    {
    	System.setProperty("java.library.path", "/home/miller/research/storm/storm-starter-3/storm-starter/hyperic-sigar-1.6.3/sigar-bin/lib");
    	port = context.getThisWorkerPort();
    	Sigar sigar = new Sigar();
		long pid = sigar.getPid();
        _countMetric = new InfoMetric(""+port);
        context.registerMetric("test_cpu", _countMetric, 3);
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