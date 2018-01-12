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

public class Main implements Serializable{
    private Matchers matchers = new Matchers();
    private ArrayList<MatchInfo> mathchInfoList;
    private String filePath = null;
    private String convertedFilePath = null;
    private String outputPath = null;
    private Converter converter = null;
    private SnapShot snapShot = null;


    public static void main(String[] args) {
        Main find = new Main();
        find.input();
        find.convert();
        find.init();
        find.process();
        find.snapshot();
        find.output();
    }

    private void init() {
        matchers = new Matchers();
        matchers.addMatcher(IdMatcher.getInstance());
        matchers.addMatcher(PhoneMatcher.getInstance());
        matchers.addMatcher(BankMatcher.getInstance());
        matchers.addMatcher(AddressMatcher.getInstance());
        matchers.addMatcher(MailMatcher.getInstance());
        mathchInfoList = new ArrayList<>();
        snapShot = new SnapShot();
    }

    private void input(){
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String str = null;
        boolean flag = true;
        try {
            while(flag){
                System.out.print("请输入文件地址:");
                filePath = br.readLine();

                File file = new File(filePath);
                if(file.exists()){
                    if(file.isDirectory()){
                        System.out.println("这是一个目录，请重新输入!");
                    }else {
                        flag = false;
                    }
                }else{
                    System.out.println("该文件不存在，请重新输入!");
                }
            }
        }  catch (IOException e) {
            e.printStackTrace();
        }finally {
            System.out.println("------输入文件成功！------");
        }
    }


    private void convert() {
        ConverterFactory factory = new ConverterFactory();
        String extension = FilenameUtils.getExtension(filePath);
        converter =  factory.getConverter(extension);
        converter.convert(filePath);
        convertedFilePath = converter.getNewFileName();
        System.out.println("------文件预处理成功！------");
    }

    private void process() {
        SparkConf conf = new SparkConf().setAppName("spark").setMaster("local[*]");
        JavaSparkContext sc = new JavaSparkContext(conf);
        JavaRDD<String> dataFile = sc.textFile(convertedFilePath);
        JavaRDD<String> lines = dataFile.flatMap(line -> Arrays.asList(line.split(System.getProperty("line.separator"))).iterator());
        List<String> output = lines.collect();
        for (String x : output){
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
                flag=true;
                detail += splitx[i] + ",";
            }
        }
        if (ret.endsWith(",")) {
            ret = ret.substring(0, ret.length() - 1);
        }

        if(detail.endsWith(",")){
            detail = detail.substring(0,detail.length()-1);
        }

        if(flag){
            MatchInfo info = new MatchInfo();
            info.setType(ret);
            info.setDetail(detail);
            info.setLocation(splitx[0]);
            mathchInfoList.add(info);
        }


        return ret;
    }

    private void snapshot(){
        try{
            snapShot.getReport(mathchInfoList,converter);
            outputPath = snapShot.getReportName();
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            System.out.println("------快照处理成功！------");
        }
    }

    private void output(){
        System.out.println("处理文件类型："+FilenameUtils.getExtension(filePath));
        System.out.println("输入文件地址："+filePath);
        System.out.println("转换后文件地址："+convertedFilePath);
        System.out.println("数据快照地址："+outputPath);
    }

}

