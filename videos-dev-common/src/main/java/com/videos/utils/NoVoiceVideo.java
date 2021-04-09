package com.videos.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class NoVoiceVideo {
    private String ffmpegEXE;

    public NoVoiceVideo(String ffmpegEXE) {
        this.ffmpegEXE = ffmpegEXE;
    }
    public void novoice(String videoInputPath,String videoOutPath) throws IOException {
        List<String> command = new ArrayList<>();
        //ffmpeg -i video.mp4 -vcodec copy -an novoice.mp4
        command.add(ffmpegEXE);
        command.add("-i");
        command.add(videoInputPath);
        command.add("-vcodec");
        command.add("copy");
        command.add("-an");
        command.add(videoOutPath);
//        command.add("&");
//        for (String c1 : command) {
//            System.out.print(c1);
//        }
        ProcessBuilder builder = new ProcessBuilder(command);
        Process process = builder.start();
        InputStream errorStream = process.getErrorStream();
        InputStreamReader inputStreamReader = new InputStreamReader(errorStream);
        BufferedReader br = new BufferedReader(inputStreamReader);

        String line = "";
        while ((line = br.readLine()) != null) {

        }
        if (br != null) {
            br.close();
        }
        if (inputStreamReader != null) {
            inputStreamReader.close();
        }
        if (errorStream != null) {
            errorStream.close();
        }
    }

    public static void main(){
        NoVoiceVideo ffmpeh = new NoVoiceVideo("E:\\software\\ffmpeg\\ffmpeg\\bin\\ffmpeg.exe");
        try {
            ffmpeh.novoice("D:\\video.mp4","D:\\novoice.mp4");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    }
