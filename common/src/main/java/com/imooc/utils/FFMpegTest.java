package com.imooc.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author daile.sun
 * @date 2018/10/9
 */
public class FFMpegTest {

    private String ffmpegEXE;

    public FFMpegTest(String ffmpegEXE) {
        this.ffmpegEXE = ffmpegEXE;
    }

    public void convertor(String videoInputPath, String videoOutputPath) {

        List<String> command=new ArrayList<>();
        command.add(ffmpegEXE);
        command.add("-i");
        command.add(videoInputPath);
        command.add(videoOutputPath);
        ProcessBuilder builder = new ProcessBuilder(command);
        BufferedReader buffer =null;
        InputStreamReader inputStreamReader=null;
        InputStream errorStream =null;
        try {
            Process process=builder.start();
            errorStream = process.getErrorStream();
            inputStreamReader = new InputStreamReader(errorStream);
            buffer = new BufferedReader(inputStreamReader);
            String line="";
            while((line=buffer.readLine())!=null){

            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(buffer!=null){
                    buffer.close();
                }
                if(inputStreamReader!=null){
                    inputStreamReader.close();
                }
                if(errorStream!=null){
                    errorStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



    }

    public static void main(String[] args) {
        FFMpegTest ffMpeg = new FFMpegTest("D:/小程序/ffmpeg-4.0.2-win64-static/bin/ffmpeg.exe");
        ffMpeg.convertor("D:\\imooc.mp4", "D:\\le.avi");

    }
}
