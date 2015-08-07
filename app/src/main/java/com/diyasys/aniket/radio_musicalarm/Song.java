package com.diyasys.aniket.radio_musicalarm;

/**
 * Created by Aniket on 30-07-2015.
 */
public class Song {

    //Song ID
    private long id;
    //Song title
    private String title;
    //Song artist
    private String artist;

    //Construct the object
    public Song(long songID, String songTitle, String songArtist){
        id=songID;
        title=songTitle;
        artist=songArtist;
    }

    public long getID(){return id;}
    public String getTitle(){return title;}
    public String getArtist(){return artist;}

}

