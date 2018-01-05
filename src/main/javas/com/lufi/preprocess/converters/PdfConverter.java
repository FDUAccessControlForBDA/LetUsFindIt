package com.lufi.preprocess.converters;

import com.lufi.preprocess.Converter;

import java.util.Map;

/**
 * Created by Sunny on 2018/1/4.
 */
public class PdfConverter implements Converter {

    private String fileName;
    private String newFileName;
    private Map<String, Object> map;

    public PdfConverter(){}

    @Override
    public String convert() {
        // TODO
        return null;
    }

    public String getFileName(){
        return fileName;
    }

    public String getNewFileName(){
        return newFileName;
    }

    public Map<String, Object> getMap(){
        return map;
    }
}
