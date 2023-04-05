package com.example.tourapp.data;

public class RouteRespose {
    private int code;
    private String msg;
    private int total;

    private RouteListData data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public RouteListData getData() {
        return data;
    }

    public void setData(RouteListData data) {
        this.data = data;
    }


}
