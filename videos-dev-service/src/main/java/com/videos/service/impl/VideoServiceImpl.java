package com.videos.service.impl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.videos.mapper.BgmMapper;
import com.videos.mapper.SearchRecordsMapper;
import com.videos.mapper.VideosMapper;
import com.videos.mapper.VideosMapperCustom;
import com.videos.pojo.Bgm;
import com.videos.pojo.SearchRecords;
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
    @Autowired
    private SearchRecordsMapper searchRecordsMapper;

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

    /**
     * 保存热搜次
     * @param videos
     * @param isSaveRecord
     * @param page
     * @param pageSize
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public PagedResult getAllVideos(Videos videos,Integer isSaveRecord,Integer page, Integer pageSize) {

        String desc = videos.getVideoDesc();
        if(isSaveRecord != null && isSaveRecord == 1){
            SearchRecords record = new SearchRecords();
            String recordId = sid.nextShort();
            record.setContent(desc);
            record.setId(recordId);
            searchRecordsMapper.insert(record);
        }
        String userId = videos.getUserId();
        PageHelper.startPage(page,pageSize);
        List<VideosVO> list = videosMapperCustom.queryAllVideos(desc,userId);

        PageInfo<VideosVO> pageList = new PageInfo<>(list);

        PagedResult pagedResult = new PagedResult();
        pagedResult.setPage(page);
        pagedResult.setRows(list);
        pagedResult.setTotal(pageList.getPages());
        pagedResult.setRecords(pageList.getTotal());

        return pagedResult;
    }

    @Override
    public List<String> grtHotwords() {
        return null;
    }
}
