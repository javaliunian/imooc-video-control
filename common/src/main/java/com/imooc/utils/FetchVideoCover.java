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
public class FetchVideoCover {

    private String ffmpegEXE;

    public FetchVideoCover(String ffmpegEXE) {
        this.ffmpegEXE = ffmpegEXE;
    }

    public void convertor(String videoInputPath, String coverOutputPath) throws IOException{
        //		ffmpeg.exe -ss 00:00:01 -i spring.mp4 -vframes 1 bb.jpg

        List<String> command=new ArrayList<>();
        command.add(ffmpegEXE);

        command.add("-ss");
        command.add("00:00:01");

        command.add("-i");
        command.add(videoInputPath);

        command.add("-vframes");
        command.add("1");

        command.add(coverOutputPath);

        for(String c:command){
            System.out.print(c+" ");
        }

        ProcessBuilder builder = new ProcessBuilder(command);
        BufferedReader buffer =null;
        InputStreamReader inputStreamReader=null;
        InputStream errorStream =null;
        try {
            Process process=builder.start();
            errorStream = process.getErrorStream();
            inputStreamReader = new InputStreamReader(errorStream);
            buffer = new BufferedReader(inputStreamReader);
            while((buffer.readLine())!=null){

            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {

                if(buffer!=null){
                    buffer.close();
                }
                if(inputStreamReader!=null){
                    inputStreamReader.close();
                }
                if(errorStream!=null){
                    errorStream.close();
                }

        }



    }

    public static void main(String[] args) throws IOException{
        FetchVideoCover ffMpeg = new FetchVideoCover("D:/小程序/ffmpeg-4.0.2-win64-static/bin/ffmpeg.exe");
        ffMpeg.convertor("D:\\imooc.mp4", "D:\\封面.jpg");

    }
}
