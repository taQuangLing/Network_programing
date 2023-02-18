package com.example.front_end.model;

import com.example.front_end.controller.AppUtils;
import lombok.NoArgsConstructor;

import java.util.List;
@lombok.Data
@NoArgsConstructor
public class Data {
    private AppUtils.MessageCode messageCode;
    private List<Object> data;
    private int size;
}
