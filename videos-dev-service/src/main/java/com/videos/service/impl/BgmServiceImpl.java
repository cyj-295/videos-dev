package com.videos.service.impl;
import com.videos.mapper.BgmMapper;
import com.videos.pojo.Bgm;
import com.videos.service.BgmService;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BgmServiceImpl implements BgmService {

    @Autowired
    private BgmMapper bgmMapper ;
    @Autowired
    private Sid sid;

    /**
     * 查询所有bgm
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)//事务开启。（查询）
    @Override
    public List<Bgm> queryBgmList() {
        return bgmMapper.selectAll();
    }

    /**
     * 根据id查询bgm信息
     * @param bgmId
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)//事务开启。（查询）
    @Override
    public Bgm queryBgmById(String bgmId){

        return bgmMapper.selectByPrimaryKey(bgmId);
    }

}
