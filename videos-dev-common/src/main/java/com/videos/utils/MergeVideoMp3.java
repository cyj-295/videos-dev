package com.videos.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MergeVideoMp3 {

    private String ffmpegEXE;

    public MergeVideoMp3(String ffmpegEXE) {
        this.ffmpegEXE = ffmpegEXE;
    }

    public void convertor(String videoInputPath,String mp3InputPath,double seconds, String videoOutPath) throws Exception{

        List<String> command = new ArrayList<>();
        // ffmpeg -i novice.mp4 -i bgm.mp3 -t 12 -y 新的视频.mp4
        command.add(ffmpegEXE);
        command.add("-i");
        command.add(videoInputPath);
        command.add("-i");
        command.add(mp3InputPath);
        command.add("-t");
        command.add(String.valueOf(seconds));
        command.add("-y");
        command.add(videoOutPath);

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
//    public void novoice(String videoInputPath,String videoOutPath) throws IOException {
//        List<String> command = new ArrayList<>();
//        //ffmpeg -i video.mp4 -vcodec copy -an novoice.mp4
//        command.add(ffmpegEXE);
//        command.add("-i");
//        command.add(videoInputPath);
//        command.add("-vcodec");
//        command.add("copy");
//        command.add("-an");
//        command.add(videoOutPath);
//        command.add("&");
//        for(String c1 : command){
//            System.out.print(c1);
//        }
//        ProcessBuilder builder = new ProcessBuilder(command);
//        Process process = builder.start();
//        InputStream errorStream = process.getErrorStream();
//        InputStreamReader inputStreamReader = new InputStreamReader(errorStream);
//        BufferedReader br = new BufferedReader(inputStreamReader);
//
//        String line="";
//        while ((line = br.readLine()) != null){
//
//        }
//        if(br != null){
//            br.close();
//        }
//        if(inputStreamReader != null){
//            inputStreamReader.close();
//        }
//        if(errorStream != null){
//            errorStream.close();
//        }

//    }


    public static void main(){
        MergeVideoMp3 ffmpeg = new MergeVideoMp3("E:\\software\\ffmpeg\\ffmpeg\\bin\\ffmpeg.exe");
        try {
            ffmpeg.convertor("D:\\novoice.mp4","D:\\bgm.mp3",12,"D:\\aaaa.mp4");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
