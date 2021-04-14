package com.videos.mapper;

//import com.imooc.pojo.vo.VideosVO;
import com.videos.pojo.Videos;
import com.videos.pojo.vo.VideosVO;
import com.videos.utils.MyMapper;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VideosMapperCustom extends MyMapper<Videos> {
	
	/**
	 * @Description: 条件查询所有视频列表
//	 */
	public List<VideosVO> queryAllVideos( @Param("videoDesc") String videoDesc,@Param("userId") String userId);


}