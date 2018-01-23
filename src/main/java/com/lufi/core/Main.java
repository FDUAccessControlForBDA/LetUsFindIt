package com.lufi.core;

import com.lufi.matching.MatchInfo;
import com.lufi.matching.Matchers;
import com.lufi.matching.matchers.*;
import com.lufi.preproccess.Converter;
import com.lufi.preproccess.ConverterFactory;
import com.lufi.snapshot.SnapShot;
import com.lufi.utils.FilenameUtils;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.SparkConf;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main implements Serializable {
    private static Matchers matchers = new Matchers();
    private ArrayList<MatchInfo> matchInfoList;
    private static ArrayList<String> filePathList = new ArrayList<>();
    private static String filePath = null;
    private String convertedFilePath = null;
    private String outputPath = null;
    private Converter converter = null;

    private static SparkConf conf = null;
    private static JavaSparkContext sc = null;
    private SnapShot snapShot = null;
    private static int index = 0;


    public static void main(String[] args) {
        Main find = new Main();
        find.init();
        find.input();
        for (int i = 0; i < filePathList.size(); i++) {
            filePath = filePathList.get(i);
            if (find.convert()) {
                find.process();
                find.snapshot();
                find.output();
            }
        }
    }

    private void init() {
        matchInfoList = new ArrayList<>();
        snapShot = new SnapShot();
        conf = new SparkConf().setAppName("spark").setMaster("local[*]").set("spark.driver.maxResultSize", "20g");
        sc = new JavaSparkContext(conf);
        matchers = new Matchers();
        matchers.addMatcher(IdMatcher.getInstance());
        matchers.addMatcher(PhoneMatcher.getInstance());
        matchers.addMatcher(BankMatcher.getInstance());
        //matchers.addMatcher(AddressMatcher.getInstance());
        matchers.addMatcher(MailMatcher.getInstance());
    }


    private void input() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String str = null;
        boolean flag = true;
        try {
            while (flag) {
                System.out.print("请输入文件地址:");
                String tmpPath = "C:\\Users\\symsimmy\\Documents\\content\\demo.article_12071234.csv";
                //String tmpPath = "C:\\Users\\symsimmy\\Documents\\content\\chens.article_100.csv";
                //String tmpPath = br.readLine();

                File file = new File(tmpPath);
                if (file.exists()) {
                    if (file.isDirectory()) {
                        find(tmpPath);
                    } else {
                        filePathList.add(tmpPath);
                    }
                    flag = false;
                } else {
                    System.out.println("该文件不存在，请重新输入!");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("------输入文件成功！------");
        }
    }

    private void find(String pathName) throws IOException {
        //获取pathName的File对象
        File dirFile = new File(pathName);
        //判断该文件或目录是否存在，不存在时在控制台输出提醒
        if (!dirFile.exists()) {
            System.out.println("do not exit");
            return;
        }
        //判断如果不是一个目录，就判断是不是一个文件，是文件则输出文件路径
        if (!dirFile.isDirectory()) {
            if (dirFile.isFile()) {
                System.out.println(dirFile.getCanonicalFile());
            }
            return;
        } else {
            //获取此目录下的所有文件名与目录名
            String[] fileList = dirFile.list();
            for (int i = 0; i < fileList.length; i++) {
                String path = pathName + "\\" + fileList[i];
                filePathList.add(path);
            }
        }

    }


    private boolean convert() {
        ConverterFactory factory = new ConverterFactory();
        String extension = FilenameUtils.getExtension(filePath);
        converter = factory.getConverter(extension);
        if (converter == null) {
            return false;
        }
        converter.convert(filePath);
        convertedFilePath = converter.getNewFileName();
        System.out.println("------文件预处理成功！------");
        return true;
    }

    private void process() {
        matchInfoList.clear();
        JavaRDD<String> dataFile = sc.textFile(convertedFilePath);
        JavaRDD<String> lines = dataFile.flatMap(line -> Arrays.asList(line.split(System.getProperty("line.separator"))).iterator());
        List<String> output = lines.collect();
        for (String x : output) {
            matchx(x);
        }
        System.out.println("------隐私数据匹配成功！------");
    }

    private String matchx(final String x) {
        String[] splitx = x.split(",");
        int count = splitx.length;
        String ret = "";
        String detail = "";
        boolean flag = false;
        for (int i = 1; i < count; i++) {
            String element = splitx[i];
            String returnFlag = matchers.match(element);
            if (returnFlag != null) {
                ret += returnFlag + ",";
                flag = true;
                detail += splitx[i] + ",";
            }
        }
        if (ret.endsWith(",")) {
            ret = ret.substring(0, ret.length() - 1);
        }

        if (detail.endsWith(",")) {
            detail = detail.substring(0, detail.length() - 1);
        }

        if (flag) {
            MatchInfo info = new MatchInfo();
            info.setType(ret);
            info.setDetail(detail);
            info.setLocation(splitx[0]);
            matchInfoList.add(info);
        }


        return ret;
    }

    private void snapshot() {
        try {
            snapShot.getReport(matchInfoList, converter);
            outputPath = snapShot.getReportName();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("------快照处理成功！------");
        }
    }

    private void output() {
        System.out.println("处理文件类型：" + FilenameUtils.getExtension(filePath));
        System.out.println("输入文件地址：" + filePath);
        System.out.println("转换后文件地址：" + convertedFilePath);
        System.out.println("数据快照地址：" + outputPath);
    }

}

