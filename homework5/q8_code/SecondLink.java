import java.io.IOException;
import java.util.StringTokenizer;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class SecondLink {

  public static class LinkMapper
       extends Mapper<Object, Text, Text, Text>{

    private Text startText = new Text();
    private Text endText = new Text();
    private Text combineText = new Text();

    public void map(Object key, Text value, Context context
                    ) throws IOException, InterruptedException {

      StringTokenizer itr = new StringTokenizer(value.toString());

      String startNode = itr.nextToken();
      String endNode = itr.nextToken();
      String combineLink = startNode + "-" + endNode;

      startText.set(startNode);
      endText.set(endNode);
      combineText.set(combineLink);

      context.write(startText, endText);
      context.write(endText, combineText);
    }
  }

  public static class LinkCombiner
       extends Reducer<Text,Text,Text,Text> {

    private Text combineText = new Text();
    private Text secondText = new Text();
    public void reduce(Text key, Iterable<Text> endNodes, Context context
                       ) throws IOException, InterruptedException {
      String startStr = key.toString();

      for(Text endNode : endNodes){
        String endStr = endNode.toString();
        String combineStr = startStr + "-" + endStr;
        secondText.set(endStr);

        context.write(key, endNode);
        combineText.set(combineStr);
        context.write(secondText, combineText);
      }
  }
}

  public static class LinkReducer
       extends Reducer<Text,Text,Text,Text> {

    private Text firstNode = new Text();
    private Text secondNode = new Text();
    private Text thirdNode = new Text();
    private Log log = LogFactory.getLog(LinkReducer.class);

    public void reduce(Text key, Iterable<Text> combineNodes, Context context
                       ) throws IOException, InterruptedException {
      ArrayList<String> arrLink = new ArrayList<String>();
      ArrayList<String> arrSingle = new ArrayList<String>();


      // log.info("Key = " + key.toString());
      for(Text combine : combineNodes){
        String comStr = combine.toString();
        // log.info(comStr);
        if(comStr.indexOf('-') != -1){
          arrLink.add(comStr);
        }
        else{
          arrSingle.add(comStr);
        }
      }

      for(String linkNode : arrLink){
        String nodes[] = linkNode.split("-");
        firstNode.set(nodes[0]);
        secondNode.set(nodes[1]);
        context.write(firstNode, secondNode);
        for(String node3 : arrSingle){
          thirdNode.set(node3);
          context.write(firstNode, thirdNode);
        }
      }
  }
}

  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    Job job = Job.getInstance(conf, "Second Link");
    job.setJarByClass(SecondLink.class);
    job.setMapperClass(LinkMapper.class);
    //job.setCombinerClass(LinkCombiner.class);
    job.setReducerClass(LinkReducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(Text.class);
    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    System.out.println("Max split " + FileInputFormat.getMaxSplitSize(job));
    System.out.println("Min split " + FileInputFormat.getMinSplitSize(job));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}
