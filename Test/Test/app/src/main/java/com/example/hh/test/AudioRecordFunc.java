/**
 * Created by hh on 2015/6/4.
 */
package com.example.hh.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.annotation.TargetApi;
import android.media.AudioFormat;

import android.media.AudioRecord;
import android.os.Build;

public class AudioRecordFunc {
    private int bufferSizeInBytes = 0;
    private String AudioName = "";
    private String NewAudioName = "";
    private AudioRecord audioRecord;
    private boolean isRecord = false;
    private static AudioRecordFunc Instance;
    private AudioRecordFunc(){  }
    public synchronized static AudioRecordFunc getInstance(){

        if (Instance == null)
            Instance = new AudioRecordFunc();
        System.out.println("Get instance finish\n");
        return Instance;
    }

    public int startRecordAndFile(){
        //if (AudioFileFunc.)
        System.out.println("Start Record and File\n");
        if (isRecord)
            return ErrorCode.E_STATE_RECODING;
        //    System.out.println("Have already been recorded.");
        else {
            if (audioRecord == null) {
                System.out.println("audioRecord is null. We are here.");
                createAudioRecord();
            }

            audioRecord.startRecording();
            isRecord = true;
            new Thread(new AudioRecordThread()).start();
            System.out.println("Start Record and File Finish\n");
            return ErrorCode.SUCCESS;
        }

    }
    public void stopRecordAndFile(){
        close();
    }

    public long getRecordFileSize(){
        return AudioFileFunc.getFileSize(NewAudioName);
    }

    private void close(){
        if (audioRecord != null){
            System.out.println("stop record");
            isRecord = false;
            audioRecord.stop();
            audioRecord.release();
            audioRecord = null;
        }
    }

    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    private void createAudioRecord(){
        System.out.println("Create AudioRecording");
        AudioName = AudioFileFunc.getRawPath();
        NewAudioName = AudioFileFunc.getWavePath();

        bufferSizeInBytes = AudioRecord.getMinBufferSize(AudioFileFunc.AUDIO_SAMPLE_RATE,
                AudioFormat.CHANNEL_IN_STEREO, AudioFormat.ENCODING_PCM_16BIT);

        audioRecord = new AudioRecord(AudioFileFunc.AUDIO_INPUT, AudioFileFunc.AUDIO_SAMPLE_RATE,
                AudioFormat.CHANNEL_IN_STEREO, AudioFormat.ENCODING_PCM_16BIT, bufferSizeInBytes);
        System.out.println("Create AudioRecording finish");
    }

    class AudioRecordThread implements Runnable {
        @Override
        public void run() {
            writeDataToFile();
            copyWaveFile(AudioName,NewAudioName);
        }
    }
    //Processing Here

    private void processRecord(){

    }
    //Processing Here

    private void writeDataToFile(){
        byte[] audioData = new byte[bufferSizeInBytes];
        FileOutputStream fOut = null;
        int readsize = 0;
        try{
            File file = new File(AudioName);
            if (file.exists()){
                file.delete();
            }
            fOut = new FileOutputStream(file);
        }catch (Exception err){
            err.printStackTrace();
        }
        while (isRecord==true){
            readsize = audioRecord.read(audioData,0,bufferSizeInBytes);
            if (AudioRecord.ERROR_INVALID_OPERATION != readsize && fOut!=null) {
                try {
                    fOut.write(audioData);
                } catch (IOException err) {
                    err.printStackTrace();
                }
            }
        }
    }

    private void copyWaveFile(String inFilename, String outFilename) {
        FileInputStream in = null;
        FileOutputStream out = null;
        long totalAudioLength = 0;
        long totalDataLength = totalAudioLength + 36;
        long longSampleRate = AudioFileFunc.AUDIO_SAMPLE_RATE;
        int channels = 2;
        long byteRate = 16 * AudioFileFunc.AUDIO_SAMPLE_RATE * channels / 8;
        byte[] data = new byte[bufferSizeInBytes];
        try {
            in = new FileInputStream(inFilename);
            out = new FileOutputStream(outFilename);
            totalAudioLength = in.getChannel().size();
            totalDataLength = totalAudioLength + 36;
            WriteWaveFileHeader(out, totalAudioLength, totalDataLength,
                    longSampleRate, channels, byteRate);
            while (in.read(data) != -1) {
                out.write(data);
            }
            in.close();
            out.close();
        } catch (FileNotFoundException err) {
            err.printStackTrace();
        } catch (IOException err) {
            err.printStackTrace();
        }
    }

    private void WriteWaveFileHeader(FileOutputStream out, long totalAudioLen,
                                     long totalDataLen, long longSampleRate, int channels,
                                     long byteRate)
            throws IOException {
        byte[] header = new byte[44];
        header[0] = 'R'; // RIFF/WAVE header
        header[1] = 'I';
        header[2] = 'F';
        header[3] = 'F';
        header[4] = (byte) (totalDataLen & 0xff);
        header[5] = (byte) ((totalDataLen >> 8) & 0xff);
        header[6] = (byte) ((totalDataLen >> 16) & 0xff);
        header[7] = (byte) ((totalDataLen >> 24) & 0xff);
        header[8] = 'W';
        header[9] = 'A';
        header[10] = 'V';
        header[11] = 'E';
        header[12] = 'f'; // 'fmt ' chunk
        header[13] = 'm';
        header[14] = 't';
        header[15] = ' ';
        header[16] = 16; // 4 bytes: size of 'fmt ' chunk
        header[17] = 0;
        header[18] = 0;
        header[19] = 0;
        header[20] = 1; // format = 1
        header[21] = 0;
        header[22] = (byte) channels;
        header[23] = 0;
        header[24] = (byte) (longSampleRate & 0xff);
        header[25] = (byte) ((longSampleRate >> 8) & 0xff);
        header[26] = (byte) ((longSampleRate >> 16) & 0xff);
        header[27] = (byte) ((longSampleRate >> 24) & 0xff);
        header[28] = (byte) (byteRate & 0xff);
        header[29] = (byte) ((byteRate >> 8) & 0xff);
        header[30] = (byte) ((byteRate >> 16) & 0xff);
        header[31] = (byte) ((byteRate >> 24) & 0xff);
        header[32] = (byte) (2 * 16 / 8); // block align
        header[33] = 0;
        header[34] = 16; // bits per sample
        header[35] = 0;
        header[36] = 'd';
        header[37] = 'a';
        header[38] = 't';
        header[39] = 'a';
        header[40] = (byte) (totalAudioLen & 0xff);
        header[41] = (byte) ((totalAudioLen >> 8) & 0xff);
        header[42] = (byte) ((totalAudioLen >> 16) & 0xff);
        header[43] = (byte) ((totalAudioLen >> 24) & 0xff);
        out.write(header, 0, 44);
    }

}
