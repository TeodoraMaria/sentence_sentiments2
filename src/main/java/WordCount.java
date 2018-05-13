import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class WordCount extends Configured implements Tool {
    public int run(String[] strings) throws Exception {
        if(strings.length<2) {
            System.out.println("Wrong input or output path..");
            return -1;
        }
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf,"word count");
        job.setJarByClass(WordCount.class);
        job.setMapperClass(WordMapper.class);
        job.setCombinerClass(WordReducer.class);
        job.setReducerClass(WordReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        FileInputFormat.addInputPath(job,new Path(strings[0]));
        FileOutputFormat.setOutputPath(job,new Path(strings[1]));


        return job.waitForCompletion(true)?0:1;
    }

    public static void main(String[] args) {
        int exitCode=0;
        try {
            exitCode = ToolRunner.run(new WordCount(),args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(exitCode);
    }
}
