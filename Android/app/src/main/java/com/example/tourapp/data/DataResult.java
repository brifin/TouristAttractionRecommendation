package com.example.tourapp.data;

public class DataResult {
    private long code;
    private String msg;
    private long total;
    private String[] data;

    public DataResult() {
    }

    public DataResult(Integer code, String msg, Integer total, String[] data) {
        this.code = code;
        this.msg = msg;
        this.total = total;
        this.data = data;
    }

    /**
     * 获取
     * @return code
     */
    public long getCode() {
        return code;
    }

    /**
     * 设置
     * @param code
     */
    public void setCode(long code) {
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
    public long getTotal() {
        return total;
    }

    /**
     * 设置
     * @param total
     */
    public void setTotal(long total) {
        this.total = total;
    }

    /**
     * 获取
     * @return data
     */
    public String[] getData() {
        return data;
    }

    /**
     * 设置
     * @param data
     */
    public void setData(String[] data) {
        this.data = data;
    }

    public String toString() {
        return "DataResult{code = " + code + ", msg = " + msg + ", total = " + total + ", data = " + data + "}";
    }
}
