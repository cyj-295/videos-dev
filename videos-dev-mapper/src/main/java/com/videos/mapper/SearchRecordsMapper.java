package com.videos.mapper;

import java.util.List;

import com.videos.pojo.SearchRecords;
import com.videos.utils.MyMapper;

public interface SearchRecordsMapper extends MyMapper<SearchRecords> {
	
	public List<String> getHotwords();
}