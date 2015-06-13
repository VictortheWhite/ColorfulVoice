package com.example.hh.test;

/**
 * Created by hh on 2015/6/4.
 */

import java.io.File;

import android.media.MediaRecorder;
import android.os.Environment;


public class AudioFileFunc {
    public final static int AUDIO_INPUT = MediaRecorder.AudioSource.MIC;

    public final static int AUDIO_SAMPLE_RATE = 8000;

    private final static String AUDIO_RAW_FILENAME = "Raw.raw";
    private final static String AUDIO_WAV_FILENAME = "Final.wav";

    //public static boolean isSDcardExist();

    public static String getRawPath(){
        String RawPath = "";

        String BasePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        RawPath = BasePath+ "/" + AUDIO_RAW_FILENAME;
        return RawPath;
    }

    public static String getWavePath(){
        String WavePath = "";

        String BasePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        return (WavePath = BasePath + "/" +AUDIO_WAV_FILENAME);
    }

    public static long getFileSize(String path){
        File file = new File(path);
        if (!file.exists())
            return -1;
        else return file.length();
    }

}
