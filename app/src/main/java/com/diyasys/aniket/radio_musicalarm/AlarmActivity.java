package com.diyasys.aniket.radio_musicalarm;

import android.app.Activity;
import android.app.AlarmManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.TimePicker;
import android.widget.Button;
import android.view.View.OnClickListener;



/**
 * Created by Aniket on 24-07-2015.
 */
public class AlarmActivity extends Activity {
    AlarmManager alarmManager;
    private TimePicker alarmTimepicker;
    private static AlarmActivity inst;
    Button alarmSet;

    public static AlarmActivity instance() {
        return inst;
    }
    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        //Initialize UI
        alarmTimepicker = (TimePicker) findViewById(R.id.alarmTimePicker);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        addListenerOnButton();
    }

    public void addListenerOnButton() {

        alarmSet = (Button) findViewById(R.id.alarmSet);

        alarmSet.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Log.d("MyActivity", "Alarm On");

                //Get time from TimePicker
                int hour = alarmTimepicker.getCurrentHour();
                int minute = alarmTimepicker.getCurrentMinute();

                //Bundle the time to send to another activity
                Bundle alarmTime = new Bundle();
                alarmTime.putInt("hour",hour);
                alarmTime.putInt("minute",minute);


                Intent intent = new Intent(AlarmActivity.this, RadioService.class);
                intent.putExtras(alarmTime);

                //Start RadioService Activity
                startActivity(intent);

            }

        });

    }

}
