/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package storm.starter;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.ShellBolt;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import storm.scheduler.OnlineScheduler;
import storm.starter.spout.RandomSentenceSpout;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * This topology demonstrates Storm's stream groupings and multilang capabilities.
 */
public class WordCountTopology {
  public static class SplitSentence extends ShellBolt implements IRichBolt {

    public SplitSentence() {
      super("python", "/home/miller/research/storm/apache-storm-0.9.3/examples/storm-starter/multilang/resources/splitsentence.py");
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
      declarer.declare(new Fields("word"));
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
      return null;
    }
  }

  public static class WordCount extends BaseBasicBolt {
    Map<String, Integer> counts = new HashMap<String, Integer>();

    @Override
    public void execute(Tuple tuple, BasicOutputCollector collector) {
      String word = tuple.getString(0);
      Integer count = counts.get(word);
      if (count == null)
        count = 0;
      count++;
      counts.put(word, count);
      collector.emit(new Values(word, count));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
      declarer.declare(new Fields("word", "count"));
    }
  }
  
public static class ReportBolt extends BaseBasicBolt {
	  
	  Map<String, Integer> counts = new HashMap<String, Integer>();
	  private Logger logger = Logger.getLogger(ReportBolt.class);
	  @Override
		public void execute(Tuple tuple, BasicOutputCollector collector) {
			// TODO Auto-generated method stub
			String word = tuple.getStringByField("word");
			  Integer count = tuple.getIntegerByField("count");
			  this.counts.put(word, count);
			  System.out.println(word + " : " + count);
			  logger.info("word: "+word+" count: "+count);
		}

	  @Override
	  public void declareOutputFields(OutputFieldsDeclarer declarer) {
		  // this bolt does not emit anything
	  }
	  
	  
	  public void cleanup() {
		  ArrayList<String> keys = new ArrayList<String>();
		  keys.addAll(this.counts.keySet());
		  Collections.sort(keys);
		  System.out.println("------------------");
		  for (String key : keys) {
			  System.out.println(key + " : " + this.counts.get(key));
		  }
		  System.out.println("------------------");
	  }
  }

  public static void main(String[] args) throws Exception {

    TopologyBuilder builder = new TopologyBuilder();

    builder.setSpout("spout", new RandomSentenceSpout(), 2);

    builder.setBolt("split", new SplitSentence(),2).shuffleGrouping("spout");
    //builder.setBolt("count", new WordCount(),4).fieldsGrouping("split", new Fields("word"));
    builder.setBolt("count", new WordCount(),4).shuffleGrouping("split");
    builder.setBolt("report", new ReportBolt()).globalGrouping("count");
    
    Config conf = new Config();
    conf.setDebug(true);
    List<String> list = new LinkedList<String>();
    list.add("spout");
    list.add("split");
    list.add("count");
    list.add("report");
    conf.put("components", list);
    Map<String, List<String>> streamMap = new HashMap<String, List<String>>();
    List<String> list1 = new LinkedList<String>();
    list1.add("split");
    streamMap.put("spout",list1);
    List<String> list2= new LinkedList<String>();
    list2.add("count");
    streamMap.put("split",list2);
    List<String> list3= new LinkedList<String>();
    list3.add("report");
    streamMap.put("count",list3);
    conf.put("streams", streamMap);
    conf.setNumWorkers(3);

    StormSubmitter.submitTopologyWithProgressBar("WordCount1", conf, builder.createTopology());
 
    /*if (args != null && args.length > 0) {
      conf.setNumWorkers(3);

      StormSubmitter.submitTopologyWithProgressBar("WordCount1", conf, builder.createTopology());
    }
    else {
      //conf.setMaxTaskParallelism(3);
    	conf.setNumWorkers(2);
    

      LocalCluster cluster = new LocalCluster();
      cluster.submitTopology("word-count", conf, builder.createTopology());

      Thread.sleep(10000);
      cluster.killTopology("word-count");

      cluster.shutdown();
    }*/
  }
}
