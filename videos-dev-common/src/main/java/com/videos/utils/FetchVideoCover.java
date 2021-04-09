package com.videos.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FetchVideoCover {

    private String ffmpegEXE;

    public FetchVideoCover(String ffmpegEXE) {
        this.ffmpegEXE = ffmpegEXE;
    }

    public void convertor(String videoInputPath, String coverOutputPath) throws Exception{

        List<String> command = new ArrayList<>();
        // ffmpeg.exe -ss 00:00:01 -y -i 新的视频.mp4 -vframes 1  new.jpg
        command.add(ffmpegEXE);
        command.add("-ss");
        command.add("00:00:01");
        command.add("-y");
        command.add("-i");
        command.add(videoInputPath);
        command.add("-vframes");
        command.add("1");
        command.add(coverOutputPath);

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
    public String getFfmpegEXE() {
        return ffmpegEXE;
    }

    public void setFfmpegEXE(String ffmpegEXE) {
        this.ffmpegEXE = ffmpegEXE;
    }

    public FetchVideoCover() {
        super();
    }


    public static void main(String[] agrs){
        FetchVideoCover ffmpeg = new FetchVideoCover("E:\\software\\ffmpeg\\ffmpeg\\bin\\ffmpeg.exe");
        try {
            ffmpeg.convertor("D:\\aaaa.mp4","D:\\new.jpg");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
