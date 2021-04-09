package com.videos.service;

import com.videos.pojo.Users;

public interface UserService {

    /**
     * 判断用户名是否存在
     * @param name
     * @return
     */
    public boolean queryUsernameIsExist(String name);

    /**
     * 保存用户名
     */
    public void SaveUser(Users user);

    /**
     * 用户登录，根据用户名和密码查询
     * @return
     */
    public Users queryUserForLogin(String username, String password) throws Exception;

    /**
     *用户修改信息
     */
    public void  updateUserInfo(Users user);

    /**
     * 查询用户信息
     * @param userId
     * @return
     */
    public Users queryUserInfo(String userId);

}
