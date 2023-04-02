package com.example.tourapp.data;

public class RouteRecommendData {
    private long code;
    private RouteRe data;
    private String msg;
    private long total;

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public RouteRe getData() {
        return data;
    }

    public void setData(RouteRe data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
