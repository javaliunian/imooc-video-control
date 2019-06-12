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
public class MergeVideoMp3 {

    private String ffmpegEXE;

    public MergeVideoMp3(String ffmpegEXE) {
        this.ffmpegEXE = ffmpegEXE;
    }

    public void convertor(String videoInputPath,String mp3InputPath,double seconds, String videoOutputPath) {

        List<String> command=new ArrayList<>();
        command.add(ffmpegEXE);

        command.add("-i");
        command.add(videoInputPath);

        command.add("-i");
        command.add(mp3InputPath);

        command.add("-t");
        command.add(String.valueOf(seconds));

        command.add("-y");
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
        MergeVideoMp3 ffMpeg = new MergeVideoMp3("D:/小程序/ffmpeg-4.0.2-win64-static/bin/ffmpeg.exe");
        ffMpeg.convertor("D:\\imooc.mp4", "C:\\Users\\Admin\\Music\\Two Steps From Hell - Victory.mp3",12.0,"D:\\新视频.mp4");

    }
}
