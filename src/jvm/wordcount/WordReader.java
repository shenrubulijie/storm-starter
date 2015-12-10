package wordcount;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.hyperic.sigar.Sigar;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

public class WordReader extends BaseRichSpout implements Serializable{
    private static final long serialVersionUID = 2197521792014017918L;
    private String inputPath;
    private SpoutOutputCollector collector;
    transient InfoMetric _countMetric;
    private long port;
    @Override
    @SuppressWarnings("rawtypes")
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        this.collector = collector;
        inputPath = (String) conf.get("INPUT_PATH");
        initMetrics(context);
    }

    @Override
    public void nextTuple() {
    	updateMetrics();
        Collection<File> files = FileUtils.listFiles(new File(inputPath),
                FileFilterUtils.notFileFilter(FileFilterUtils.suffixFileFilter(".bak")), null);
        for (File f : files) {
            try {
                List<String> lines = FileUtils.readLines(f, "UTF-8");
                for (String line : lines) {
                    collector.emit(new Values(line));
                }
                FileUtils.moveFile(f, new File(f.getPath() + System.currentTimeMillis() + ".bak"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("line"));
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