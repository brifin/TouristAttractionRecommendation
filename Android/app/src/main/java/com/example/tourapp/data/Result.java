package com.example.tourapp.data;

public class Result {
    private Integer code;
    private Integer total;
    private String msg;
    private Data data;

    public Result() {
    }

    public Result(Integer code, Integer total, String msg, Data data) {
        this.code = code;
        this.total = total;
        this.msg = msg;
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
     * @return data
     */
    public Data getData() {
        return data;
    }

    /**
     * 设置
     * @param data
     */
    public void setData(Data data) {
        this.data = data;
    }

    public String toString() {
        return "Result{code = " + code + ", total = " + total + ", msg = " + msg + ", data = " + data + "}";
    }

    public class Data {
        public Integer id;
        public String nickname;
        public String account;
        public String password;
        public String stars;
        public String gone;
    }

}
