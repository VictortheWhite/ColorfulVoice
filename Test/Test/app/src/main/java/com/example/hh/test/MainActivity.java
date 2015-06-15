package com.example.hh.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jtransforms.fft.DoubleFFT_1D;


public class MainActivity extends Activity {

    private final static int FLAG_WAV = 0;
    //private final static int FRAMES_PER_BUFFER = 2048;
    //private static final int BYTES_PER_SAMPLE = 16;
    private int State = -1;
    private Button Record;
    private Button stop;
    private TextView Text;
    private Handler uiHandler;
    private Thread uiThread;

    private DoubleFFT_1D fft;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewByIds();
        setListeners();
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    private void findViewByIds(){
        Record = (Button)this.findViewById(R.id.Record);
        stop = (Button)this.findViewById(R.id.stop);
        Text = (TextView)this.findViewById(R.id.text);
    }
    private void setListeners(){
        Record.setOnClickListener(btn_record_wav_clickListener);
        stop.setOnClickListener(btn_stop_clickListener);
    }
    private void init(){
        uiHandler = new Handler();
    }
    private Button.OnClickListener btn_record_wav_clickListener = new Button.OnClickListener(){
        public void onClick(View v){
            System.out.println("Start Record(FLAG_WAV)");
            record(FLAG_WAV);
        }
    };


    private Button.OnClickListener btn_stop_clickListener = new Button.OnClickListener(){
        public void onClick(View v){
            stop();
        }
    };
    /**
     * 开始录音
     */
    private void record(int Flag){
        System.out.println("Recording\n");
        if(State != -1){
            Message msg = new Message();
            Bundle bundle = new Bundle();// 存放数据
            bundle.putInt("cmd",CMD_RECORDFAIL);
            bundle.putInt("msg", ErrorCode.E_STATE_RECODING);
            msg.setData(bundle);

            uiHandler.sendMessage(msg); // 向Handler发送消息,更新UI
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
        }
    }
    /**
     * 停止录音
     */
    private void stop(){
        if(State != -1){
            if (State == FLAG_WAV){
                    AudioRecordFunc Record_1 = AudioRecordFunc.getInstance();
                    Record_1.stopRecordAndFile();
            }
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
            State = -1;
        }
    }
    private final static int CMD_RECORDING_TIME = 2000;
    private final static int CMD_RECORDFAIL = 2001;
    private final static int CMD_STOP = 2002;
    class UIHandler extends Handler{
        public UIHandler() {
        }
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            Log.d("MyHandler", "handleMessage......");
            System.out.println("We are handling messages!");
            super.handleMessage(msg);
            Bundle b = msg.getData();
            int vCmd = b.getInt("cmd");
            switch(vCmd)
            {
                case CMD_RECORDING_TIME:
                    int vTime = b.getInt("msg");
                    MainActivity.this.Text.setText("正在录音中，已录制："+vTime+" s");
                    break;
                case CMD_RECORDFAIL:
                    int vErrorCode = b.getInt("msg");
                    String vMsg = ErrorCode.getErrorInfo(MainActivity.this, vErrorCode);
                    MainActivity.this.Text.setText("录音失败："+vMsg);
                    break;
                case CMD_STOP:
                    int vFileType = b.getInt("msg");
                    switch(vFileType){
                        case FLAG_WAV:
                            AudioRecordFunc mRecord_1 = AudioRecordFunc.getInstance();
                            long mSize = mRecord_1.getRecordFileSize();
                            MainActivity.this.Text.setText("录音已停止.录音文件:"+AudioFileFunc.getWavePath()+"\n文件大小："+mSize);
                            break;

                    }
                    break;
                default:
                    break;
            }
        }
    };
    class UIThread implements Runnable {
        int mTimeMill = 0;
        boolean vRun = true;
        public void stopThread(){
            vRun = false;
        }
        public void run() {
            while(vRun){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                mTimeMill ++;
                Log.d("thread", "mThread........"+mTimeMill);
                Message msg = new Message();
                Bundle b = new Bundle();// 存放数据
                b.putInt("cmd",CMD_RECORDING_TIME);
                b.putInt("msg", mTimeMill);
                msg.setData(b);

                MainActivity.this.uiHandler.sendMessage(msg); // 向Handler发送消息,更新UI
            }

        }
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
}
