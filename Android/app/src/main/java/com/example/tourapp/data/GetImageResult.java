package com.example.tourapp.data;


public class GetImageResult {
    Integer code;
    String msg;
    Integer total;
    String data;

    public GetImageResult() {
    }

    public GetImageResult(Integer code, String msg, Integer total, String data) {
        this.code = code;
        this.msg = msg;
        this.total = total;
        this.data = data;
    }

    /**
     * 获取
     * @return code
     */
    public Integer getCode() {
        return code;
    }

    /**
     * 设置
     * @param code
     */
    public void setCode(Integer code) {
        this.code = code;
    }

    /**
     * 获取
     * @return msg
     */
    public String getMsg() {
        return msg;
    }

    /**
     * 设置
     * @param msg
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * 获取
     * @return total
     */
    public Integer getTotal() {
        return total;
    }

    /**
     * 设置
     * @param total
     */
    public void setTotal(Integer total) {
        this.total = total;
    }

    /**
     * 获取
     * @return data
     */
    public String getData() {
        return data;
    }

    /**
     * 设置
     * @param data
     */
    public void setData(String data) {
        this.data = data;
    }

    public String toString() {
        return "GetImageResult{code = " + code + ", msg = " + msg + ", total = " + total + ", data = " + data + "}";
    }
}
