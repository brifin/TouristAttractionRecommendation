package com.example.tourapp.manager;

public class User {
    private String username;
    private String password;
    private Integer userId;


    public User() {
    }

    public User(String username, String password, Integer userId) {
        this.username = username;
        this.password = password;
        this.userId = userId;
    }

    /**
     * 获取
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 获取
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取
     * @return userId
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置
     * @param userId
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String toString() {
        return "User{username = " + username + ", password = " + password + ", userId = " + userId + "}";
    }
}
