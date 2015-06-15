/**
 * Created by hh on 2015/6/4.
 */
package com.example.victor.colorfulwithoutvoice;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.media.AudioFormat;

import android.media.AudioRecord;

import org.jtransforms.fft.DoubleFFT_1D;


public class AudioRecordFunc {
//    static {
//        System.loadLibrary("FFT");
 //   }
    //public native double processSampleData(byte[] sample,int sampleRate);
    private static int FRAMES_PER_BUFFER;
    private int bufferSizeInBytes = 0;
    private String AudioName = "";
    private String NewAudioName = "";
    private AudioRecord audioRecord;
    private boolean isRecord = false;
    private static AudioRecordFunc Instance;
    private AudioRecordFunc(){  }
    public double [] volume;
    public double [] sampledoublebuffer;
    public static int count;
    Object mLock = new Object();
    private DoubleFFT_1D fft;

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

            //audioRecord.startRecording();
            isRecord = true;

           // new Thread(new AudioRecordThread()).start();
            new Thread(new Runnable(){
                public void run(){

                    audioRecord.startRecording();
                    processRecord();
                }

            }).start();
           // processRecord();
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

    private void createAudioRecord(){
        System.out.println("Create AudioRecording");

        AudioName = AudioFileFunc.getRawPath();
        NewAudioName = AudioFileFunc.getWavePath();

        bufferSizeInBytes = AudioRecord.getMinBufferSize(AudioFileFunc.AUDIO_SAMPLE_RATE,
                AudioFormat.CHANNEL_IN_STEREO, AudioFormat.ENCODING_PCM_16BIT);
        FRAMES_PER_BUFFER = bufferSizeInBytes;
        sampledoublebuffer = new double[FRAMES_PER_BUFFER];
        volume = new double[bufferSizeInBytes];
        fft = new DoubleFFT_1D(FRAMES_PER_BUFFER);
        audioRecord = new AudioRecord(AudioFileFunc.AUDIO_INPUT, AudioFileFunc.AUDIO_SAMPLE_RATE,
                AudioFormat.CHANNEL_IN_STEREO, AudioFormat.ENCODING_PCM_16BIT, bufferSizeInBytes);
        System.out.println("Create AudioRecording finish");
    }

  /*  class AudioRecordThread implements Runnable {
        @Override
        public void run() {
            writeDataToFile();
            copyWaveFile(AudioName,NewAudioName);
        }
    }*/
    //Processing Here

    private void processRecord(){
        int maxv=0,maxf=0;
        byte[] buffer = new byte[bufferSizeInBytes];
        short[] sampleshortbuffer = new short[bufferSizeInBytes];
        //short[] sampleshortbuffer = new short[bufferSizeInBytes/2];
        FileOutputStream fOut = null;
        try{
            File file = new File(AudioName);
            if (file.exists()){
                file.delete();
            }
            fOut = new FileOutputStream(file);
        }catch (Exception err){
            err.printStackTrace();
        }
        int cc = 0;
        while (isRecord) {
            //r是实际读取的数据长度，一般而言r会小于buffersize

            int r = audioRecord.read(buffer, 0, bufferSizeInBytes);
            //if (cc%2 == 1) {
                //System.out.print("We are in cc.....\n" + String.valueOf(buffer[cc]) + " " + String.valueOf(buffer[cc+1]));
                sampleshortbuffer[cc] = buffer[cc];
                //sampleshortbuffer[cc/2] = (short) (sampleshortbuffer[cc-1] << 8);
                //sampleshortbuffer[cc/2] = (short) (sampleshortbuffer[cc] + buffer[cc]);
                System.out.print(" " + String.valueOf(sampleshortbuffer[cc]) + "\n");
                long v = 0;
                // 将 buffer 内容取出，进行平方和运算
                for (int i = 0; i < sampleshortbuffer.length; i++) {
                    v += sampleshortbuffer[i] * sampleshortbuffer[i];
                }
                // 平方和除以数据总长度，得到音量大小。
                double mean = v / (double) r;
                convertToDouble(sampleshortbuffer, sampledoublebuffer);
                fft.realForward(sampledoublebuffer);
                if (count < bufferSizeInBytes) {
                    volume[count] = 10 * Math.log10(mean);
                    System.out.println("volume: " + String.valueOf(volume[count]) + " frequency:" + String.valueOf(sampledoublebuffer[count]) + " " + String.valueOf(count) + "\n");
                    count++;
                }
           // }

            if (AudioRecord.ERROR_INVALID_OPERATION != r && fOut!=null) {
                try {
                    fOut.write(buffer);
                } catch (IOException err) {
                    err.printStackTrace();
                }
            }
            cc++;
            //writeDataToFile(r,buffer, fOut);
            /*if (count < bufferSizeInBytes) {
                volume[count] = 10 * Math.log10(mean);
                System.out.println("volume: "+String.valueOf( volume[count]) + " frequency:" + String.valueOf(sampledoublebuffer[count])+" ");
                count++;
            }*/
            // 大概一秒十次
            /* synchronized (mLock) {
                try {
                    mLock.wait(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }*/
        }
        copyWaveFile(AudioName,NewAudioName);

    }
    //Processing Here
    private void convertToDouble(short[] input, double[] output){
        double scale = 1 / 32768.0;
        for(int i = 0; i < input.length; i++){
            output[i] = input[i] * scale;
        }
    }

    private void writeDataToFile(int readsize, byte [] audioData, FileOutputStream fOut){
            System.out.print("Write Data To File!!!!!!!!\n");
            if (AudioRecord.ERROR_INVALID_OPERATION != readsize && fOut!=null) {
                try {
                    fOut.write(audioData);
                } catch (IOException err) {
                    err.printStackTrace();
                }
            }

    }

    /*
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
*/
    private void copyWaveFile(String inFilename, String outFilename) {
        FileInputStream in = null;
        FileOutputStream out = null;
        long totalAudioLength = 0;
        long totalDataLength = totalAudioLength + 36;
        long longSampleRate = AudioFileFunc.AUDIO_SAMPLE_RATE;
        int channels = 2;
        long byteRate = 16 * AudioFileFunc.AUDIO_SAMPLE_RATE * channels / 8;
        byte[] data = new byte[bufferSizeInBytes];
        System.out.print("Copy Wave To File!!!!!\n");
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
