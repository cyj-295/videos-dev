package com.videos.service.impl;

import com.videos.mapper.UsersMapper;
import com.videos.pojo.Users;
import com.videos.service.UserService;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UsersMapper userMapper;

    @Autowired
    private Sid sid;

    @Transactional(propagation = Propagation.SUPPORTS)//事务开启。（查询）
    @Override
    public boolean queryUsernameIsExist(String username) {
        Users users = new Users();
        users.setUsername(username);
        Users result = userMapper.selectOne(users);
        return result == null? false : true;
    }

    @Transactional(propagation = Propagation.REQUIRED)//事务开启。（新增修改删除）
    @Override
    public void SaveUser(Users user) {
        String userId = sid.nextShort();
        user.setId(userId);
        userMapper.insert(user);
    }

    @Transactional(propagation = Propagation.SUPPORTS)//事务开启。（查询）
    @Override
    public Users queryUserForLogin(String username, String password) {

        Example userExample = new Example(Users.class);
        Criteria criteria = userExample.createCriteria();
        criteria.andEqualTo("username", username);
        criteria.andEqualTo("password", password);
        Users result = userMapper.selectOneByExample(userExample);
        return result;
    }

    @Override
    public void updateUserInfo(Users user) {
        Example userExample = new Example(Users.class);
        Criteria criteria = userExample.createCriteria();
        criteria.andEqualTo("id",user.getId());
        userMapper.updateByExampleSelective(user,userExample);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users queryUserInfo(String userId) {
        Example userExample = new Example(Users.class);
        Criteria criteria = userExample.createCriteria();
        criteria.andEqualTo("id",userId);
        Users user = userMapper.selectOneByExample(userExample);
        return user;
    }
}
