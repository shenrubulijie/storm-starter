package wordcount;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.generated.StormTopology;
import backtype.storm.topology.TopologyBuilder;
import storm.scheduler.OfflineScheduler;
import storm.scheduler.OnlineScheduler;
public class WordCountTopo {
    public static void main(String[] args) throws Exception {
        /*if (args.length != 2) {
            System.err.println("Usage: inputPaht timeOffset");
            System.err.println("such as : java -jar WordCount.jar D://input/ 2");
            System.exit(2);
        }*/
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("word-reader", new WordReader());
        builder.setBolt("word-spilter", new WordSpliter()).shuffleGrouping("word-reader");
        builder.setBolt("word-counter", new WordCounter()).shuffleGrouping("word-spilter");
     
        String inputPaht = "/home/miller/research/storm/apache-storm-0.9.3/logs/wc_logs";
        String timeOffset = "2";
        Config conf = new Config();
        conf.put("INPUT_PATH", inputPaht);
        conf.put("TIME_OFFSET", timeOffset);
        conf.put(Config.STORM_SCHEDULER, "storm.scheduler.OfflineScheduler");
        //conf.put(Config.SUPERVISOR_ENABLE, false);
        conf.put("reschedule.timeout", "100");
        List<String> list = new LinkedList<String>();
        list.add("word-reader");
        list.add("word-spilter");
        list.add("word-counter");
        conf.put("components", list);
        Map<String, List<String>> streamMap = new HashMap<String, List<String>>();
        List<String> list1 = new LinkedList<String>();
        list1.add("word-spilter");
        streamMap.put("word-reader",list1);
        List<String> list2= new LinkedList<String>();
        list2.add("word-counter");
        streamMap.put("word-spilter",list2);
        conf.put("streams", streamMap);
        conf.setDebug(false);
        conf.setNumWorkers(3);
        StormSubmitter.submitTopologyWithProgressBar("WordCount1", conf, builder.createTopology());
      //System.out.println("why+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        if (args != null && args.length > 0) {
            //conf.setNumWorkers(3);
            //StormTopology st = builder.createTopology();
            //StormSubmitter.submitTopologyWithProgressBar("WordCount1", conf, builder.createTopology());
          }else{
        	 //LocalCluster cluster = new LocalCluster();
        	 //cluster.submitTopology("WordCount1", conf, builder.createTopology());
          }
    }
}