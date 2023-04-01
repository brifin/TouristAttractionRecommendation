package com.example.tourapp.data;

import java.util.List;

public class RouteRecommend {
    private long code;
    private Data  data;
    private String msg;
    private long total;

    public long getCode() { return code; }
    public void setCode(long value) { this.code = value; }

    public Data getData() { return data; }
    public void setData(Data value) { this.data = value; }

    public String getMsg() { return msg; }
    public void setMsg(String value) { this.msg = value; }

    public long getTotal() { return total; }
    public void setTotal(long value) { this.total = value; }
}
