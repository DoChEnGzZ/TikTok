package com.example.myapplication.mytiktok.gson_internet;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class VideoMessage implements Serializable{

    @SerializedName("_id")
    private String userid;
    @SerializedName("feedurl")
    private String VideoUrl;
    @SerializedName("nickname")
    private String NickName;
    @SerializedName("description")
    private String DesCripTion;
    @SerializedName("likecount")
    private int LikeCount;
    @SerializedName("avatar")
    private String avatorUrl;


    public VideoMessage(String userid, String videoUrl, String nickName, String desCripTion, Integer likeCount, String avatorUrl) {
        this.userid = userid;
        VideoUrl = videoUrl;
        NickName = nickName;
        DesCripTion = desCripTion;
        LikeCount = likeCount;
        this.avatorUrl = avatorUrl;
    }


    public String getUserid() {
        return userid;
    }

    public String getVideoUrl() {
        return VideoUrl;
    }


    public String getNickName() {
        return NickName;
    }

    public String getDesCripTion() {
        return DesCripTion;
    }

    public Integer getLikeCount() {
        return LikeCount;
    }

    public String getAvatorUrl() {
        return avatorUrl;
    }

    public String getVideoName(){
        return  getVideoUrl().substring(28);
    }
    public String getPicName(){
        return getAvatorUrl().substring(31);
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setVideoUrl(String videoUrl) {
        VideoUrl = videoUrl;
    }

    public void setNickName(String nickName) {
        NickName = nickName;
    }

    public void setDesCripTion(String desCripTion) {
        DesCripTion = desCripTion;
    }

    public void setLikeCount(int likeCount) {
        LikeCount = likeCount;
    }

    public void setAvatorUrl(String avatorUrl) {
        this.avatorUrl = avatorUrl;
    }

    @Override
    public String toString() {
        return "VideoMessage{" +
                "userid='" + userid + '\'' +
                ", VideoUrl='" + VideoUrl + '\'' +
                ", NickName='" + NickName + '\'' +
                ", DesCripTion='" + DesCripTion + '\'' +
                ", LikeCount=" + LikeCount +
                ", avatorUrl='" + avatorUrl + '\'' +
                '}';
    }
}
