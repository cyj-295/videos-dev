package com.videos.service.impl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.videos.mapper.BgmMapper;
import com.videos.mapper.VideosMapper;
import com.videos.mapper.VideosMapperCustom;
import com.videos.pojo.Bgm;
import com.videos.pojo.Videos;
import com.videos.pojo.vo.VideosVO;
import com.videos.service.BgmService;
import com.videos.service.VideoService;
import com.videos.utils.PagedResult;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    private VideosMapper videosMapper ;
    @Autowired
    private Sid sid;
    @Autowired
    private VideosMapperCustom videosMapperCustom;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public String saveVideo(Videos video) {
        String id = sid.nextShort();
        video.setId(id);
        videosMapper.insertSelective(video);
        return id;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updataVideo(String videoId, String coverPath) {
        Videos video = new Videos();
        video.setId(videoId);
        video.setCoverPath(coverPath);
        videosMapper.updateByPrimaryKeySelective(video);

    }

    @Override
    public PagedResult getAllVideos(Integer page, Integer pageSize) {

        PageHelper.startPage(page,pageSize);
        List<VideosVO> list = videosMapperCustom.queryAllVideos();

        PageInfo<VideosVO> pageList = new PageInfo<>(list);

        PagedResult pagedResult = new PagedResult();
        pagedResult.setPage(page);
        pagedResult.setRows(list);
        pagedResult.setTotal(pageList.getPages());
        pagedResult.setRecords(pageList.getTotal());

        return pagedResult;
    }
}
