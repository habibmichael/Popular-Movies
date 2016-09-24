package com.l2l.androided.mh122354.popularmovies;

/**
 * Created by mh122354 on 9/24/2016.
 */

public class Movie {

    private String overview;
    private int rating;
    private String releaseDate;
    private String imagePath;
    private String title;


    public Movie(){

    }

    public Movie(String overview,String releaseDate, String imagePath,String title,int rating){

        this.rating=rating;
        this.title=title;
        this.releaseDate=releaseDate;
        this.imagePath=imagePath;
        this.overview=overview;


    }

    public void setOverview(String overview){
        this.overview=overview;
    }

    public void setReleaseDate(String releaseDate){
        this.releaseDate=releaseDate;
    }

    public void setImagePath(String imagePath){
        this.imagePath=imagePath;
    }

    public void setTitle(String title){
        this.title=title;
    }

    public void setRating(int rating){
        this.rating=rating;
    }



    public String getOverview(){return overview;}

    public String getReleaseDate(){return releaseDate;}

    public String getImagePath(){return imagePath;}

    public String getTitle(){return title;}

    public int getRating(){return rating;}
}
