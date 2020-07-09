package com.imagine.myapplication.post_classes;

import com.google.firebase.Timestamp;

import java.util.Date;

public class GIFPost extends Post {
    //Attribute von GIFPosts
    public String link = "";
    // Constructor
    public GIFPost(String title,String documentID, String description, String report, String createTime,
                       Timestamp creatTimestamp, String originalPoster, long thanksCount, long wowCount, long haCount,
                       long niceCount, String type,String link) {
        super(title,documentID,description,report,createTime,creatTimestamp,originalPoster,
                thanksCount,wowCount,haCount,niceCount,type);
        this.link = link;
    }

    public GIFPost(){

    }
    //------------Setter-Functionen------------
    //------------Setter-Functionen------------
    public void setLink(String link) {
        this.link = link;
    }
    //------------Getter-Functionen------------
    //------------Getter-Functionen------------
    public String getLink() {
        return link;
    }
}