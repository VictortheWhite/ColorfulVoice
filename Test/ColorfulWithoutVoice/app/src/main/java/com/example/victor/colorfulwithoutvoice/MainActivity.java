package com.example.victor.colorfulwithoutvoice;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import org.jtransforms.fft.DoubleFFT_1D;

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
    private static int State=-1;
    private final static int FLAG_WAV = 0;


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

        RecordButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                if (IsRecording) {
                    IsRecording = false;
                    //changeButton
                    RecordButton.setBackgroundResource(R.drawable.play);
                    stopRecording();

                    //stop timer
                    RecordTimer.cancel();
                    //go to next activity
                    StartNextActivity();
                } else {
                    IsRecording = true;
                    //changeButton
                    RecordButton.setBackgroundResource(R.drawable.pause);
                    startRecording(FLAG_WAV);
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

    private void startRecording(int Flag){
        System.out.println("Recording\n");
        if(State != -1){
            return;
        }
        int Result = -1;
        if (Flag == FLAG_WAV)
        {
            System.out.println("FLAG_WAVing!!!");
            AudioRecordFunc Record_1 = AudioRecordFunc.getInstance();
            Result = Record_1.startRecordAndFile();
        }
        System.out.println("Now out of FLAG_WAVing!!!");
        /*
        if(Result == ErrorCode.SUCCESS){
            uiThread = new Thread();
            new Thread(uiThread).start();
            State = Flag;
        }else{
            Message msg = new Message();
            Bundle b = new Bundle();// 存放数据
            b.putInt("cmd",CMD_RECORDFAIL);
            b.putInt("msg", Result);
            msg.setData(b);
            uiHandler.sendMessage(msg); // 向Handler发送消息,更新UI
        }*/
    }

    private void stopRecording(){
        if(State != -1){
            if (State == FLAG_WAV){
                AudioRecordFunc Record_1 = AudioRecordFunc.getInstance();
                Record_1.stopRecordAndFile();
            }
            /*
            if(uiThread != null){
                //uiThread.stopThread();
                uiThread = null;
            }
            if(uiHandler != null)
                uiHandler.removeCallbacks(uiThread);
            Message msg = new Message();
            Bundle b = new Bundle();// 存放数据
            b.putInt("cmd",CMD_STOP);
            b.putInt("msg", State);
            msg.setData(b);
            uiHandler.sendMessageDelayed(msg,1000); // 向Handler发送消息,更新UI
            State = -1;*/
        }
    }



}
