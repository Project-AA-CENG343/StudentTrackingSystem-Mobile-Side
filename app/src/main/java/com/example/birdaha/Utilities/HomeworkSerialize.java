package com.example.birdaha.Utilities;

import com.example.birdaha.General.HwModel;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;

public class HomeworkSerialize implements Serializable {
    public ArrayList<HwModel> arr;
    public static HomeworkSerialize fromJson(String json)
    {
        return new Gson().fromJson(json, HomeworkSerialize.class);
    }
}
