package com.videos.service;

import com.videos.pojo.Bgm;
import com.videos.pojo.Videos;
import com.videos.utils.PagedResult;

import java.util.List;

public interface VideoService {

    /**
     * 保存视频
     * @param
     * @return
     */
    public String saveVideo(Videos video);

    /**
     * 修改视频封面
     * @param videoId
     * @param coverPath
     * @return
     */
    public void updataVideo(String videoId, String coverPath);

    /**
     * 分页查询视频列表
     * @param page
     * @param pageSize
     * @return
     */
    public PagedResult getAllVideos(Integer page,Integer pageSize);
}
