package com.alphabetlore3d.simsoundboard.b;

public class More {

    private String app_name;
    private String image_url;
    private String app_url;



    public More(String jdl, String img, String ps) {
        this.app_name = jdl;
        this.image_url = img;
        this.app_url = ps;

    }

    public String getApp_name() {
        return app_name;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getApp_url() {
        return app_url;
    }



}
