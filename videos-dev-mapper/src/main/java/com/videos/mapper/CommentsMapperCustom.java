package com.videos.mapper;

//import com.imooc.pojo.vo.CommentsVO;
import com.videos.pojo.Comments;
import com.videos.utils.MyMapper;
import com.videos.pojo.vo.CommentsVO;

import java.util.List;

public interface CommentsMapperCustom extends MyMapper<Comments> {
	
	public List<CommentsVO> queryComments(String videoId);
}