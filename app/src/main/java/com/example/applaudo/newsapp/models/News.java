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

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getHeadline() {
        return mHeadline;
    }

    public void setmHeadline(String mHeadline) {
        this.mHeadline = mHeadline;
    }

    public String getBodyText() {
        return mBodyText;
    }

    public void setmBodyText(String mBodyText) {
        this.mBodyText = mBodyText;
    }

    public String getSection() {
        return mSection;
    }

    public void setmSection(String mSection) {
        this.mSection = mSection;
    }

    public String getThumbnail() {
        return mThumbnail;
    }

    public void setmThumbnail(String mThumbnail) {
        this.mThumbnail = mThumbnail;
    }

    public String getWebSite() {
        return mWebSite;
    }

    public void setmWebSite(String mWebSite) {
        this.mWebSite = mWebSite;
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
