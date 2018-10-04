package com.example.applaudo.newsapp.query;

public class Query {

    public static final String BASE_URL = "https://content.guardianapis.com/search?show-fields=headline%2Cid%2CbodyText%2Cthumbnail%2CshortUrl&page-size=30&q=";
    public static final String API_KEY = "&api-key=3f05b3d1-6a19-44ff-a6fa-6c0e825e314b";

    public static final String QUERY_POLITICS = BASE_URL + "politics" + API_KEY;
    public static final String QUERY_SPORTS = BASE_URL + "sport" + API_KEY;
    public static final String QUERY_MUSIC = BASE_URL + "music" + API_KEY;
    public static final String QUERY_SOCIAL = BASE_URL + "social" + API_KEY;

}