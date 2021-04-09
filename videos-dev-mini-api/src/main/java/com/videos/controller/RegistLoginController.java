package com.videos.controller;

import com.videos.pojo.Users;
import com.videos.pojo.vo.UsersVO;
import com.videos.utils.IMoocJSONResult;
import com.videos.utils.MD5Utils;
import com.videos.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@Api(value = "用户注册登录的接口", tags = {"注册和登录的controller"})
public class RegistLoginController extends BasicController {

	@Autowired
	private UserService userService;

	@PostMapping("/regist")
	@ApiOperation(value = "用户注册", notes = "用户注册的接口")
	public IMoocJSONResult regist(@RequestBody Users user) throws Exception {

		//1.判断用户名和密码不为空
		if(StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword())){
			return IMoocJSONResult.errorMsg("用户名和密码不能为空");
		}

		//2.判断用户名是否存在
		boolean usernameIsExist = userService.queryUsernameIsExist(user.getUsername());
		//3.保存用户，注册信息
		if(!usernameIsExist){
			user.setNickname(user.getUsername());
			user.setPassword(MD5Utils.getMD5Str(user.getPassword()));
			user.setFollowCounts(0);
			user.setReceiveLikeCounts(0);
			user.setFansCounts(0);
			userService.SaveUser(user);
		}else {
			return IMoocJSONResult.errorMsg("用户名已存在，请换一个试试");
		}
		user.setPassword("");
//		String uniqueToken = UUID.randomUUID().toString();
//		redis.set("USER_REDIS_SESSION"+":"+user.getId(),uniqueToken,1000 *60 * 30);
//
//		UsersVO usersVO = new UsersVO();
//		BeanUtils.copyProperties(user,usersVO);
//		usersVO.setUserToken(uniqueToken);

		UsersVO usersVO = setUserRedisSessionToken(user);
		return IMoocJSONResult.ok(usersVO);
	}

	public UsersVO setUserRedisSessionToken(Users userModel){
		String uniqueToken = UUID.randomUUID().toString();
		redis.set("USER_REDIS_SESSION"+":"+userModel.getId(),uniqueToken,1000 *60 * 30);

		UsersVO userVO = new UsersVO();
		BeanUtils.copyProperties(userModel,userVO);
		userVO.setUserToken(uniqueToken);
		return userVO;
	}

	@PostMapping("/login")
	@ApiOperation(value = "用户登录", notes = "用户登录接口")
	public IMoocJSONResult login(@RequestBody Users users) throws Exception {
		String username = users.getUsername();
		String password = users.getPassword();

		//1.判断用户名和密码不为空
		if(StringUtils.isBlank(username) || StringUtils.isBlank(password)){
			return IMoocJSONResult.errorMsg("用户名和密码不能为空");
		}

		//2.判断用户是否存在
		Users userResult = userService.queryUserForLogin(username,MD5Utils.getMD5Str(users.getPassword()));
		//3.返回
		if(userResult !=null){
			userResult.setPassword("");
			UsersVO userVo = setUserRedisSessionToken(userResult);
			return IMoocJSONResult.ok(userVo);
		}else {
			return IMoocJSONResult.errorMsg("用户名或密码不正确，请重试...");
		}

	}

	@PostMapping("/logout")
	@ApiImplicitParam(name = "userId",value = "用户id",dataType = "String",paramType = "query",required = true)
	@ApiOperation(value = "用户注销", notes = "用户注销接口")
	public IMoocJSONResult logout(String userId) throws Exception {
		redis.del("USER_REDIS_SESSION"+":"+userId);
			return IMoocJSONResult.ok("注销成功！");
		}

	}


