package com.videos.controller;

import com.videos.enums.VideoStatusEnum;
import com.videos.mapper.SearchRecordsMapper;
import com.videos.pojo.Bgm;
import com.videos.pojo.SearchRecords;
import com.videos.pojo.Users;
import com.videos.pojo.Videos;
import com.videos.service.BgmService;
import com.videos.service.VideoService;
import com.videos.utils.*;
import io.swagger.annotations.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@Api(value = "视频相关业务的接口", tags = {"视频相关相关业务的controller"})
@RequestMapping("/video")
public class VideoController extends BasicController {

    @Autowired
    private BgmService bgmService;

    @Autowired
    private VideoService videoService;
    @Autowired
    private SearchRecordsMapper searchRecordsMapper;

    @PostMapping(value = "/upload",headers = "content-type=multipart/form-data")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId",value = "用户id",dataType = "String",paramType = "form",required = true),
            @ApiImplicitParam(name = "bgmId",value = "背景音乐id",dataType = "String",paramType = "form",required = false),
            @ApiImplicitParam(name = "videoSeconds",value = "视频播放长度",dataType = "String",paramType = "form",required = true),
            @ApiImplicitParam(name = "videoWidth",value = "视频宽度",dataType = "String",paramType = "form",required = true),
            @ApiImplicitParam(name = "videoHeight",value = "视频高度",dataType = "String",paramType = "form",required = true),
            @ApiImplicitParam(name = "desc",value = "视频描述",dataType = "String",paramType = "form",required = false)
    })
    @ApiOperation(value = "上传视频", notes = "上传视频接口")
    public IMoocJSONResult upload(String userId,String bgmId, double videoSeconds, int videoWidth, int videoHeight,
                                      String desc,@ApiParam(value = "短视频",required = true) MultipartFile file) throws Exception {

        if(StringUtils.isBlank(userId)){
            return IMoocJSONResult.errorMsg("用户id不能为空。。。");
        }
       System.out.println(desc);
        //文件保存的命名空间
//        String fileSpace = "D:/develop/workspace/videos-img";

        //保存到数据库相对路径
        String uploadPathDb ="/"+userId+"/video";
        String coverPathDb ="/" + userId + "/video";

        FileOutputStream fileOutputStream = null;
        InputStream inputStream = null;

        //文件上传的最终保存路径
        String finalVideoPath = "";

        try {
            if(file !=null ){

                String fileName = file.getOriginalFilename();
                String fileNamePrefix = fileName.split("\\.")[0];
                if(StringUtils.isNoneBlank(fileName)){

                    finalVideoPath = FILE_SPACE + uploadPathDb + "/" +fileName;
                    //设置数据库保存的路径
                    uploadPathDb += ("/"+fileName);
                    coverPathDb = coverPathDb + "/" + fileNamePrefix + ".jpg";
                    File outfile = new File(finalVideoPath);
                    if(outfile.getParentFile() != null || !outfile.getParentFile().isDirectory()){
                        //创建父文件夹
                        outfile.getParentFile().mkdirs();
                    }
                    fileOutputStream =new FileOutputStream(outfile);
                    inputStream= file.getInputStream();
                    IOUtils.copy(inputStream,fileOutputStream);

                }

            }else {
                return IMoocJSONResult.errorMsg("上传出错...");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return IMoocJSONResult.errorMsg("上传出错...");
        }finally {
            if(fileOutputStream != null){
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        }
        //判断bgmId是否为空，如果不为空
        //那就查询bgm的信息，并且合并视频，产生新的视频
        if(StringUtils.isNotBlank(bgmId)){
            Bgm bgm = bgmService.queryBgmById(bgmId);
            String mp3InputPath =FILE_SPACE + bgm.getPath();

            NoVoiceVideo toor1 = new NoVoiceVideo(FFMPEG_EXE);
            String NoVideoInputPath = finalVideoPath;
            String VideoOutputName = UUID.randomUUID().toString() + "novoice" + ".mp4";;
            uploadPathDb = "/" + userId + "/video" + "/" + VideoOutputName;
            String VideoInputPath = FILE_SPACE + uploadPathDb;
            toor1.novoice(NoVideoInputPath,VideoInputPath);

            MergeVideoMp3 toor = new MergeVideoMp3(FFMPEG_EXE);
            VideoOutputName = UUID.randomUUID().toString() + ".mp4";
            uploadPathDb = "/" + userId + "/video" + "/" + VideoOutputName;
            finalVideoPath = FILE_SPACE + uploadPathDb;
            toor.convertor(VideoInputPath,mp3InputPath,videoSeconds,finalVideoPath);

            //删除无音乐视频
            File folder = new File(VideoInputPath);
            folder.delete();
            File folder1 = new File(NoVideoInputPath);
            folder1.delete();
        }
            System.out.println("uploadPathDb:"+uploadPathDb);
            System.out.println("finalVideoPath:"+finalVideoPath);

            //对进行截图
            FetchVideoCover ffmpeg = new FetchVideoCover(FFMPEG_EXE);
            ffmpeg.convertor(finalVideoPath,FILE_SPACE + coverPathDb);

            //保存视频信息到数据库
            Videos video = new Videos();
            video.setAudioId(bgmId);
            video.setUserId(userId);
            video.setVideoDesc(desc);
            video.setVideoHeight(videoHeight);
            video.setVideoSeconds((float)videoSeconds);
            video.setVideoWidth(videoWidth);
            video.setVideoPath(uploadPathDb);
            video.setCoverPath(coverPathDb);
            video.setStatus(VideoStatusEnum.SUCCESS.value);
            video.setCreateTime(new Date());

            String videoId = videoService.saveVideo(video);
            return IMoocJSONResult.ok(videoId);

    }

    @PostMapping(value = "/uploadCover",headers = "content-type=multipart/form-data")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "videoId",value = "视频主键id",dataType = "String",paramType = "form",required = true),
            @ApiImplicitParam(name = "userId",value = "用户id",dataType = "String",paramType = "form",required = true)
    })
    @ApiOperation(value = "上传封面", notes = "上传封面接口")
    public IMoocJSONResult uploadCover(String videoId,String userId, @ApiParam(value = "视频封面",required = true) MultipartFile file) throws Exception {

        if(StringUtils.isBlank(videoId) || StringUtils.isBlank(userId)){
            return IMoocJSONResult.errorMsg("视频主键id和用户id不能为空。。。");
        }

        //文件保存的命名空间
//        String fileSpace = "D:/develop/workspace/videos-img";

        //保存到数据库相对路径
        String uploadPathDb ="/" + userId + "/video";


        FileOutputStream fileOutputStream = null;
        InputStream inputStream = null;

        //文件上传的最终保存路径
        String finalCoverPath = "";

        try {
            if(file !=null ){

                String fileName = file.getOriginalFilename();

                if(StringUtils.isNoneBlank(fileName)){

                    finalCoverPath = FILE_SPACE + uploadPathDb + "/" +fileName;
                    //设置数据库保存的路径
                    uploadPathDb += ("/"+fileName);


                    File outfile = new File(finalCoverPath);
                    if(outfile.getParentFile() != null || !outfile.getParentFile().isDirectory()){
                        //创建父文件夹
                        outfile.getParentFile().mkdirs();
                    }
                    fileOutputStream =new FileOutputStream(outfile);
                    inputStream= file.getInputStream();
                    IOUtils.copy(inputStream,fileOutputStream);

                }

            }else {
                return IMoocJSONResult.errorMsg("上传出错...");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return IMoocJSONResult.errorMsg("上传出错...");
        }finally {
            if(fileOutputStream != null){
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        }

        videoService.updataVideo(videoId,uploadPathDb);

        return IMoocJSONResult.ok();

    }


    /**
     * 分页和搜索查询视频列表
     * isSaveRecord： 1 - 需要保存
     *               0 - 不需要保存
     *
     * @param videos
     * @param isSaveRecord
     * @param page
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/showAll")
    @ApiOperation(value = "上传封面", notes = "上传封面接口")
    public IMoocJSONResult showAll(@RequestBody Videos videos,Integer isSaveRecord, Integer page) throws Exception {

        if(page == null){
            page = 1;
        }

        PagedResult result = videoService.getAllVideos(videos,isSaveRecord,page,PAGE_SIZE);
         return IMoocJSONResult.ok(result);
        }

    @PostMapping(value = "/hot")
//    @ApiOperation(value = "上传封面", notes = "上传封面接口")
    public List<String> hot() throws Exception {
        return searchRecordsMapper.getHotwords(videoService.grtHotwords());
    }


    }
