package com.example.tourapp.data;

import java.util.List;
import java.util.ArrayList;

public class MyLoveDataArray {
    private List<MyLoveData> myLoveData;

    public void setLoveplace(List<MyLoveData> myLoveData1){
        this.myLoveData = myLoveData1;
    }
    public List<MyLoveData> getLoveplace(){
        return this.myLoveData;
    }
}
