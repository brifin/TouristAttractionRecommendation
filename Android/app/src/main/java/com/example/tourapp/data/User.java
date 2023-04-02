package com.example.tourapp.data;

public class User {
    //private Integer id;
    private String nickname;
    private String account;
    private String password;


    public User() {
    }

    public User(Integer id, String nickname, String account, String password) {
      //  this.id = id;
        this.nickname = nickname;
        this.account = account;
        this.password = password;
    }

    /**
     * 获取
     * @return id
     */
//    public Integer getId() {
//        return id;
//    }

    /**
     * 设置
     * @param id
     */
//    public void setId(Integer id) {
//        this.id = id;
//    }

    /**
     * 获取
     * @return nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * 设置
     * @param nickname
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * 获取
     * @return account
     */
    public String getAccount() {
        return account;
    }

    /**
     * 设置
     * @param account
     */
    public void setAccount(String account) {
        this.account = account;
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

//    public String toString() {
//        return "User{id = " + id + ", nickname = " + nickname + ", account = " + account + ", password = " + password + "}";
//    }
}
