package com.example.applaudo.newsapp.models;

public class News {

    private String mId;
    private String mHeadline;
    private String mBodyText;
    private String mSection;
    private String mThumbnail;
    private String mWebSite;

    public String getId() {
        return mId;
    }

    public String getHeadline() {
        return mHeadline;
    }

    public String getBodyText() {
        return mBodyText;
    }

    public String getSection() {
        return mSection;
    }

    public String getThumbnail() {
        return mThumbnail;
    }

    public String getWebSite() {
        return mWebSite;
    }

    public News(String mHeadline, String mBodyText, String mSection, String mThumbnail, String mWebSite,String mId) {
        this.mId=mId;
        this.mHeadline = mHeadline;
        this.mBodyText = mBodyText;
        this.mSection = mSection;
        this.mThumbnail = mThumbnail;
        this.mWebSite = mWebSite;
    }
}
