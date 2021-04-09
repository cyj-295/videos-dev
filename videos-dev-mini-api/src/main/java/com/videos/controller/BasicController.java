package com.videos.controller;

import com.videos.utils.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BasicController {

    @Autowired
    public RedisOperator redis;

    public static final String USER_REDIS_SESSION = "user-redis-session";

    public static final String FILE_SPACE = "D:/develop/workspace/videos-img";

    public static final String FFMPEG_EXE = "E:\\software\\ffmpeg\\ffmpeg\\bin\\ffmpeg.exe";

    //每页分页的记录数
    public static final Integer PAGE_SIZE = 5;
}
