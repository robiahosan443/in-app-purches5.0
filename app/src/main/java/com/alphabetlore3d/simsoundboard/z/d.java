package com.alphabetlore3d.simsoundboard.z;



public class d {
    String appstatus;
    String image1;
    String pname1;
    String title1;

    public d() {
    }

    public d(String str, String str2, String str3, String str4) {
        this.appstatus = str;
        this.title1 = str2;
        this.image1 = str3;
        this.pname1 = str4;
    }

    public String getAppStatus() {
        return this.appstatus;
    }

    public void setAppStatus(String str) {
        this.appstatus = str;
    }

    public String getTitle1() {
        return this.title1;
    }

    public void setTitle1(String str) {
        this.title1 = str;
    }

    public String getImage1() {
        return this.image1;
    }

    public void setImage1(String str) {
        this.image1 = str;
    }

    public String getPname1() {
        return this.pname1;
    }

    public void setPname1(String str) {
        this.pname1 = str;
    }
}