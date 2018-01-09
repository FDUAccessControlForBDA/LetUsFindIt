package com.lufi.preproccess.converters;

import com.lufi.preproccess.Converter;
import com.lufi.utils.Constants;
import com.lufi.utils.FilenameUtils;
import com.lufi.utils.PDFLayoutTextStripper;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Sunny on 2018/1/4.
 */
public class PdfConverter implements Converter {

    private String fileName;
    private String newFileName;
    private Map<String, String> map;

    private static File targetFile = null;
    private static BufferedWriter writer = null;

    public PdfConverter(){}

    @Override
    public String convert(String fileName) {
        if(fileName == null)
            return null;
        if (fileName.length() <= 0 || !FilenameUtils.getExtension(fileName).toLowerCase().equals(Constants.SUFFIX_PDF)) {
            return null;
        }

        String buffer;
        String tmp;
        int j = 1;
        try {

            // 初始化pdf reader
            PDFParser pdfParser = new PDFParser(new RandomAccessFile(new File(fileName), "r"));
            pdfParser.parse();
            PDDocument pdDocument = new PDDocument(pdfParser.getDocument());
            PDFTextStripper pdfTextStripper = new PDFLayoutTextStripper();

            // 初始化输出文件
            doInit(fileName, Constants.SUFFIX_CSV);

            // 获取PDF页数
            int count = pdDocument.getPages().getCount();

            for(int i = 1; i <= count; i++ ){
                // 读取第i页
                pdfTextStripper.setSortByPosition(true);
                pdfTextStripper.setStartPage(i);
                pdfTextStripper.setEndPage(i);
                tmp = pdfTextStripper.getText(pdDocument);

                // 读取被转换成txt的文本
                BufferedReader reader = new BufferedReader(new StringReader(tmp));
                String data;
                int start = j;
                while((data = reader.readLine()) != null){
                    if(!data.trim().isEmpty()) { // 判断是否一整行都是空格
                        // 将空格和中文的逗号都替换为英文的逗号
                        buffer = j + regexNonePrintChar(data) + System.getProperty("line.separator");
                        writer.write(buffer);
                        j++;
                    }
                }
                // PDF页数和csv行数的mapping
                // 例如：1-->1-29
                int end = j-1;
                if(end < start)
                    map.put(i+"", null);
                else
                    map.put(i+"", start+"-"+end);
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getFileName(){
        return fileName;
    }

    @Override
    public String getNewFileName(){
        return newFileName;
    }

    @Override
    public Map<String, String> getMap(){
        return map;
    }

    @Override
    public void doInit(String fileName, String suffix) {
        this.fileName = fileName;
        this.newFileName = FilenameUtils.getFullPath(fileName) + FilenameUtils.getBaseName(fileName) + "."+suffix;
        this.map = new HashMap<>();
        targetFile = new File(newFileName);
        writer = null;

        try {
            if (!targetFile.exists()) {
                targetFile.createNewFile();
            }
            writer = new BufferedWriter(new FileWriter(targetFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String regexNonePrintChar(String content){
        Pattern pattern =Pattern.compile("\\s+|[，]");
        Matcher matcher = pattern.matcher(content);
        Logger.getAnonymousLogger().info("Before: " + content);
        String result = matcher.replaceAll(",");
        Logger.getAnonymousLogger().info("After: " + result);
        return result;
    }
}