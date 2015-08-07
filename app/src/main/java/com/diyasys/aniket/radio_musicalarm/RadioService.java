package com.diyasys.aniket.radio_musicalarm;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import android.media.MediaPlayer;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Aniket on 25-07-2015.
 */

public class RadioService extends Activity  {

    //Store all songs
    private ArrayList<Song> songList;
    //Button for playing radio
    private Button buttonRadio;
    //Button for playing music
    private Button buttonMusic;

    AlarmManager alarmManager;

    private TextView text;

    String choice;

    String selected;

    private PendingIntent pendingIntent;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.radio_layout);

        text = (TextView) findViewById(R.id.warning);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        //Button to play music
        buttonMusic = (Button) findViewById(R.id.buttonMusic);
        buttonMusic.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

                //Initialise arraylist and fetch all songs from disk
                songList = new ArrayList<Song>();
                getSongList();

                Random r = new Random();
                //Pick a random index
                int randomId = r.nextInt(songList.size()+1);
                Song playSong = songList.get(randomId);
                long currSong = playSong.getID();

                //Generate Uri for random song
                Uri trackUri = ContentUris.withAppendedId(
                        android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        currSong);
                String uriString = trackUri.toString();
                choice = new String(uriString);

                //Notify that Alarm is set
                text.setText("Alarm Set! Go to bed.");
                Log.d("choiceM", "" + choice);

                alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                //Fetch time selected in TimePicker
                Bundle alarmTime  = getIntent().getExtras();
                int hour = alarmTime.getInt("hour");
                int minute = alarmTime.getInt("minute");

                //Get the calendar instance of the selected time
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);

                //Create an intent which branches to AlarmReceiver when triggered
                Intent myIntent = new Intent(RadioService.this, AlarmReceiver.class);
                Log.d("choice","" + choice);

                //Bundle the Uri of the song
                Bundle chce = new Bundle();
                chce.putString("choice",choice);
                myIntent.putExtras(chce);

                //Book a pendingintent to be triggered at the time specified in calendar object
                pendingIntent = PendingIntent.getBroadcast(RadioService.this, 0, myIntent, 0);
                alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
            }
        });

        //Radiogroup for radio buttons on the varios radio stations
        final RadioGroup  group= (RadioGroup) findViewById(R.id.radiogrp);
        buttonRadio = (Button) findViewById(R.id.buttonRadio);

        buttonRadio.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

                int selectedID = group.getCheckedRadioButtonId();
                View radioButton = group.findViewById(selectedID);
                int index = group.indexOfChild(radioButton);
                //Get a the clicked radiobutton and the value associated with it
                switch (index)
                {
                    case 0:
                        selected = new String("http://radio.flex.ru:8000/radionami");
                        break;

                    case 1:
                        selected = new String("http://50.7.77.115:8174/");
                        break;

                    case 2:
                        selected = new String("http://s5.nexuscast.com:8070/");
                        break;

                    case 3:
                        selected =  new String("http://198.105.220.12:9125");
                        break;

                    case 4:
                        selected = new String("http://prclive1.listenon.in:8870/");
                        break;
                }
                //If Radio is selected

                choice = new String(selected);
                Context context = getApplicationContext();

                //Warn the user to keep the data/wifi on
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, "Warning: You need to leave your Wi-Fi/Data on", duration);
                toast.show();

                //Say that alarm has been set
                text.setText("Alarm Set! Go to bed.");
                Log.d("choiceR", "" + choice);
                alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                //Fetch the time set in TimePicker
                Bundle alarmTime  = getIntent().getExtras();
                int hour = alarmTime.getInt("hour");
                int minute = alarmTime.getInt("minute");

                //Initialise calendar object for the set time
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);

                Intent myIntent = new Intent(RadioService.this, AlarmReceiver.class);
                Log.d("choice","" + choice);

                //Bundle the datasource
                Bundle chce = new Bundle();
                chce.putString("choice",choice);
                myIntent.putExtras(chce);

                //Book a pendingintent to be triggered at the time specified in calendar object
                pendingIntent = PendingIntent.getBroadcast(RadioService.this, 0, myIntent, 0);
                alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
            }
        });
    }



    //method to retrieve song info from device
    public void getSongList(){
        //query external audio
        ContentResolver musicResolver = getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);
        //iterate over results if valid
        if(musicCursor!=null && musicCursor.moveToFirst()){
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);
            //add songs to list
            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                songList.add(new Song(thisId, thisTitle, thisArtist));
            }
            while (musicCursor.moveToNext());
        }
    }
}