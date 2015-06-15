package com.example.victor.colorfulwithoutvoice;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.TextView;
import android.os.CountDownTimer;

public class MainActivity extends Activity {

    private boolean IsRecording = false;
    private Button RecordButton;
    private TextView TimerText;
    private CountDownTimer RecordTimer;
    private final int RecordingMSecs=120000;

    static int i;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findItemByID();
        //set Timer and Count Down
        RecordTimer=new CountDownTimer(RecordingMSecs, 1000) {
            public void onTick(long millisUntilFinished) {
                long SecondsLeft=millisUntilFinished/1000;
                long minutesLeft=SecondsLeft/60;
                SecondsLeft%=60;
                String timeString="0"+minutesLeft+":";
                if(SecondsLeft<10)
                    timeString+="0"+SecondsLeft;
                else
                    timeString+=SecondsLeft;
                TimerText.setText(timeString);
            }
            public void onFinish() {
                TimerText.setText("done!");
                StartNextActivity();
            }
        };

        RecordButton.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                if(IsRecording){
                    IsRecording=false;
                    //changeButton
                    RecordButton.setBackgroundResource(R.drawable.play);
                    //stopRecording();

                    //stop timer
                    RecordTimer.cancel();
                    //go to next activity
                    StartNextActivity();
                }else{
                    IsRecording=true;
                    //changeButton
                    RecordButton.setBackgroundResource(R.drawable.pause);
                    //StartRecording;
                    RecordTimer.start();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void findItemByID() {
        RecordButton = (Button) findViewById(R.id.btn_5);
        TimerText=(TextView)findViewById(R.id.TimerText);
    }

    private void StartNextActivity(){

        Intent recordActivityIntent=new Intent();
        recordActivityIntent.setClass(MainActivity.this,ShowPictureActivity.class);
        Bundle newBundle=new Bundle();
        int[] shitArray=new int[]{1,2,3,4,5,6};
        newBundle.putIntArray("shitString",shitArray);
        newBundle.putString("shit","shitshitshitshit");
        recordActivityIntent.putExtras(newBundle);
        startActivity(recordActivityIntent);
    }


}
