package com.example.mad.tennisnews;

public class TennisNews {


    // title of news
    private String mTitle ;

    // section name of news
    private String mSectionName ;

    //time published
    private String mTime ;

    // url of news
    private String mUrl ;


    /**
     * Constructs a new {@link TennisNews} object.
     *
     * @param title is the title of the news
     * @param sectionName is the section where the news is categorized
     * @param time is the time which this news published
     * @param url is the website URL to find more details about the news
     */


    public TennisNews(String title , String sectionName , String time , String url){
        mTitle = title;
        mSectionName = sectionName ;
        mTime = time ;
        mUrl = url;
    }


    /**
     * Returns the title of the news.
     */

    public String getTitle() {
        return mTitle;
    }

    /**
     * Returns the section of the news.
     */

    public String getSectionName() {
        return mSectionName;
    }

    /**
     * Returns the time of the news.
     */

    public String getTime() {
        return mTime;
    }

    /**
     * Returns the url of the news.
     */

    public String getUrl() {
        return mUrl;
    }
}
