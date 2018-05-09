package com.king.modul;

/**
 * Created by KingLee on 2018/5/4.
 */

public class UserAccount {
    private String qq;
    private String pwd;
    private Boolean rember;

    public UserAccount(String qq, String pwd, Boolean rember) {
        this.qq = qq;
        this.pwd = pwd;
        this.rember = rember;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public Boolean getRember() {
        return rember;
    }

    public void setRember(Boolean rember) {
        this.rember = rember;
    }
}
