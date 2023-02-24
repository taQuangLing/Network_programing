package com.example.front_end.model;

import com.example.front_end.appUtils.AppUtils;

import java.util.ArrayList;
import java.util.List;
@lombok.Data
public class Data {
    private AppUtils.MessageCode messageCode;
    private List<Object> data;
    private int size;
    public Data(){
        data = new ArrayList<>();
    }
    public Data(AppUtils.MessageCode messageCode){
        this.messageCode = messageCode;
        data = new ArrayList<>();
    }
}
