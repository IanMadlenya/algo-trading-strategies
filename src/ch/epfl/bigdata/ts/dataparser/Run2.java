package ch.epfl.bigdata.ts.dataparser;

import java.io.IOException;
import java.util.*;


import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;

public class Run2 {

    public static class Map2 extends MapReduceBase implements Mapper<LongWritable, Text, Text, Text> {
        private final static IntWritable one = new IntWritable(1);
        private IntWritable outSums = new IntWritable();

        public void map(LongWritable key, Text value, OutputCollector<Text, Text> output, Reporter reporter) throws IOException {
            List<String> lines = new ArrayList<String>(Arrays.asList(value.toString().split("\n")));

            for (String line : lines) {
                String a[] = value.toString().split(",");
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(Long.parseLong(a[0]));
                Calendar c2 = new GregorianCalendar(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

                output.collect(new Text(a[a.length-1]), new Text(line));
            }
        }
    }


    public static class Reduce2 extends MapReduceBase implements Reducer<Text, Text, Text, NullWritable> {
        private NullWritable nothing = NullWritable.get();

        public void reduce(Text key, Iterator<Text> values, OutputCollector<Text, NullWritable> output, Reporter reporter) throws IOException {
            long cnt = 0;
            while (values.hasNext()) {
                cnt++;
                output.collect(new Text(cnt+","+values.next().toString()), nothing);
            }
        }
    }
}	