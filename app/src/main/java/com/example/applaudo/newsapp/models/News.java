package com.example.applaudo.newsapp.models;

public class News {

    private String mHeadline;
    private String mBodyText;
    private String mSection;
    private String mThumbnail;
    private String mWebSite;

    public String getmHeadline() {
        return mHeadline;
    }

    public void setmHeadline(String mHeadline) {
        this.mHeadline = mHeadline;
    }

    public String getmBodyText() {
        return mBodyText;
    }

    public void setmBodyText(String mBodyText) {
        this.mBodyText = mBodyText;
    }

    public String getmSection() {
        return mSection;
    }

    public void setmSection(String mSection) {
        this.mSection = mSection;
    }

    public String getmThumbnail() {
        return mThumbnail;
    }

    public void setmThumbnail(String mThumbnail) {
        this.mThumbnail = mThumbnail;
    }

    public String getmWebSite() {
        return mWebSite;
    }

    public void setmWebSite(String mWebSite) {
        this.mWebSite = mWebSite;
    }

    public News(String mHeadline, String mBodyText, String mSection, String mThumbnail, String mWebSite) {
        this.mHeadline = mHeadline;
        this.mBodyText = mBodyText;
        this.mSection = mSection;
        this.mThumbnail = mThumbnail;
        this.mWebSite = mWebSite;
    }
}
