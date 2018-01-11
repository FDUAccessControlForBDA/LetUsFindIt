import com.lufi.matching.Matchers;
import com.lufi.matching.matchers.IdMatcher;
import com.lufi.matching.matchers.PhoneMatcher;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.SparkConf;

import java.util.Arrays;

public class FindIts {
    private static Matchers matchers;

    public static void main(String[] args) {
        String filepath =  "src/main/files/sample.csv";
        String outputpath = "src/main/files/output";

        long startTime = System.currentTimeMillis();
        SparkConf conf = new SparkConf().setAppName("spark").setMaster("local[*]");
        JavaSparkContext sc = new JavaSparkContext(conf);
        JavaRDD<String> dataFile = sc.textFile(filepath);
        JavaRDD<String> lines = dataFile.flatMap(line -> Arrays.asList(line.split(System.getProperty("line.separator"))).iterator());
        matchers = new Matchers();
        matchers.addMatcher(IdMatcher.getInstance());
        matchers.addMatcher(PhoneMatcher.getInstance());


    }

    private String matchx(final String x){
        String[] splitx = x.split(",");
         int count = splitx.length;
         String ret = "";
         for(int i=0;i<count;i++){
             String element = splitx[i];
             String returnFlag = matchers.match(element);
             if(returnFlag!=null){
                 ret+=returnFlag+",";
             }
         }
         return ret;
    }

}
