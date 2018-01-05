package com.lufi.preprocess;

import com.lufi.preprocess.converters.ExcelConverter;
import com.lufi.preprocess.converters.PdfConverter;
import com.lufi.preprocess.converters.TxtConverter;
import com.lufi.utils.Constants;

/**
 * Created by Sunny on 2018/1/4.
 */
public class ConverterFactory {
    public ConverterFactory(){}

    public Converter getConverter(String fileType){
        if(fileType == null){
            return null;
        }

        if(fileType.equalsIgnoreCase(Constants.SUFFIX_XLS) || fileType.equalsIgnoreCase(Constants.SUFFIX_XLSX))
            return new ExcelConverter();
        else if(fileType.equalsIgnoreCase(Constants.SUFFIX_TXT))
            return new TxtConverter();
        else if(fileType.equalsIgnoreCase(Constants.SUFFIX_PDF))
            return new PdfConverter();
        return null;
    }

}
