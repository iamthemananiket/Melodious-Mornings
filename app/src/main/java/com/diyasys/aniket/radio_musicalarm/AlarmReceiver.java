package com.diyasys.aniket.radio_musicalarm;

import java.io.IOException;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.media.MediaPlayer;

import android.media.MediaPlayer.OnPreparedListener;
import android.util.Log;


/**
 * Created by Aniket on 24-07-2015.
 */
public class AlarmReceiver extends WakefulBroadcastReceiver {

    private MediaPlayer player;

    @Override
    public void onReceive(final Context context, Intent intent) {

        //Fetch the choice
        Bundle c = intent.getExtras();
        //Set choice as datasource
        String datasource = new String(c.getString("choice"));
        Log.d("datasource", "" + datasource);

        //Set mediaplayer's datasource as the selected choice
        initializeMediaPlayer(datasource);


        //This starts the player
        player.setOnPreparedListener(new OnPreparedListener() {

            public void onPrepared(MediaPlayer mp) {
                player.start();
            }
        });


        //This will send a notification message by initialising the AlarmService
        ComponentName comp = new ComponentName(context.getPackageName(),AlarmService.class.getName());
        startWakefulService(context,(intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);
    }

    private void initializeMediaPlayer(String dataSource) {
        player = new MediaPlayer();
        try {
            //Set datasource
            player.setDataSource(dataSource);
            player.prepareAsync();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
