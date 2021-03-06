package com.videos.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FFMpegTest {

    private String ffmpegEXE;

    public FFMpegTest(String ffmpegEXE) {
        this.ffmpegEXE = ffmpegEXE;
    }

    public void convertor(String videoInputPath, String videoOutPath) throws Exception{

        List<String> command = new ArrayList<>();
        // ffmpeg -i jisoo.mp4 lisa.avi
        command.add(ffmpegEXE);
        command.add("-i");
        command.add(videoInputPath);
        command.add(videoOutPath);
        for(String c : command){
            System.out.print(c);
        }
        ProcessBuilder builder = new ProcessBuilder(command);
        Process process = builder.start();
        InputStream errorStream = process.getErrorStream();
        InputStreamReader inputStreamReader = new InputStreamReader(errorStream);
        BufferedReader br = new BufferedReader(inputStreamReader);

        String line="";
        while ((line = br.readLine()) != null){

        }
        if(br != null){
            br.close();
        }
        if(inputStreamReader != null){
            inputStreamReader.close();
        }
        if(errorStream != null){
            errorStream.close();
        }

    }

    public static void main(String[] agrs){
        FFMpegTest ffmpeg = new FFMpegTest("E:\\software\\ffmpeg\\ffmpeg\\bin\\ffmpeg.exe");
        try {
            ffmpeg.convertor("D:\\develop\\workspace\\a.mp4","D:\\develop\\workspace\\b.avi");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
