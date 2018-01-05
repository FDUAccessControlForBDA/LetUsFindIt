package com.lufi.preprocess;

<<<<<<< HEAD:src/main/javat/com/lufi/preprocess/ConvertUtil.java
=======
import com.lufi.regex.RegexUtil;
>>>>>>> 735f7d5c563ba1934676e8e4be87cc65cc533303:src/main/java/com/lufi/preprocess/ConvertUtil.java
import com.lufi.utils.Constants;
import com.lufi.utils.FilenameUtils;

import com.lufi.utils.PDFLayoutTextStripper;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConvertUtil {

    private static String targetFilePath;

    private static File targetFile = null;

    private static BufferedWriter writer = null;


    private static void doInit(final String filepath, final String suffix) {
        targetFilePath = FilenameUtils.getFullPath(filepath) + FilenameUtils.getBaseName(filepath) + "."+suffix;
        targetFile = new File(targetFilePath);
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


    public static boolean convertPdfToTxt(String filepath) {
        String buffer = null;

        if (filepath.length() <= 0 || !FilenameUtils.getExtension(filepath).toLowerCase().equals(Constants.SUFFIX_PDF)) {
            return false;
        }

        try {

            PDFParser pdfParser = new PDFParser(new RandomAccessFile(new File(filepath), "r"));
            pdfParser.parse();
            PDDocument pdDocument = new PDDocument(pdfParser.getDocument());
            PDFTextStripper pdfTextStripper = new PDFLayoutTextStripper();
            buffer = pdfTextStripper.getText(pdDocument);

            doInit(filepath, Constants.SUFFIX_TXT);

            writer.write(buffer);
            writer.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            return true;
        }
    }


    public static boolean convertExcelToCsv(String filepath) {
        String buffer = null;
        if (filepath == null) {
            return false;
        }

        if (!FilenameUtils.getExtension(filepath).toLowerCase().contains(Constants.SUFFIX_XLS)) {
            return false;
        }
            InputStream in = null;

            try {
                doInit(filepath, Constants.SUFFIX_CSV);

                in = new FileInputStream(filepath);
                Workbook wb = WorkbookFactory.create(in);

                for (int i = 0; i < wb.getNumberOfSheets(); i++) {
                    System.out.println(wb.getSheetAt(i).getSheetName());
                    Sheet sheet = wb.getSheetAt(i);

                    Row row = null;

                    for (int j = 0; j < sheet.getLastRowNum(); j++) {
                        row = sheet.getRow(j);
                        for (int k = 0; k < row.getLastCellNum()-1; k++) {
                            buffer += row.getCell(k) + ",";
                        }
                        buffer += row.getCell(row.getLastCellNum()-1)+"\n";
                    }
                    writer.write(buffer);
                }
                writer.close();
            } catch (InvalidFormatException ex) {
                Logger.getLogger(ConvertUtil.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ConvertUtil.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            } catch (IOException ex) {
                Logger.getLogger(ConvertUtil.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            } finally {
                try {
                    in.close();
                } catch (IOException ex) {
                    Logger.getLogger(ConvertUtil.class.getName()).log(Level.SEVERE, null, ex);
                    return false;
                }
                return true;
            }
        }


    public static boolean convertTxtToCsv(String filepath) {
        String buffer = null;
        String line = null;
        int i = 1;
        if (filepath.length() <= 0 || !FilenameUtils.getExtension(filepath).toLowerCase().equals(Constants.SUFFIX_TXT)) {
            return false;
        }

        Scanner in = null;
        try {
            doInit(filepath, Constants.SUFFIX_CSV);

            in = new Scanner(new BufferedReader(new FileReader(filepath)));
            if (in.hasNextLine()) {
                line = in.nextLine();
            } else {
                return false;
            }

            while (in.hasNextLine()) {
                buffer = i + " " + regexNonePrintChar(line) + "\n";
                writer.write(buffer);

                if (in.hasNextLine()) {
                    line = in.nextLine();
                    i++;
                } else {
                    return false;
                }
            }
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            in.close();
            System.out.println("success");
            return true;
        }
    }

    public  static String regexNonePrintChar(String content){
        Pattern pattern =Pattern.compile("\\s+");
        Matcher matcher = pattern.matcher(content);
        Logger.getAnonymousLogger().info("Before: " + content);
        String result = matcher.replaceAll(",");
        Logger.getAnonymousLogger().info("After: " + result);
        return result;
    }
}


