package com.videos.controller;
import com.videos.pojo.Users;
import com.videos.pojo.vo.UsersVO;
import com.videos.service.UserService;
import com.videos.utils.IMoocJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@RestController
@Api(value = "用户相关业务的接口", tags = {"用户相关业务的controller"})
@RequestMapping("/user")
public class UserController extends BasicController {

	@Autowired
	private UserService userService;

	@PostMapping("/uploadFace")
	@ApiImplicitParam(name = "userId",value = "用户id",dataType = "String",paramType = "query",required = true)
	@ApiOperation(value = "用户上传头像", notes = "用户上传头像接口")
	public IMoocJSONResult uploadFace(String userId, @RequestParam("file") MultipartFile[] files) throws Exception {

		if(StringUtils.isBlank(userId)){
			return IMoocJSONResult.errorMsg("用户id不能为空。。。");
		}

		//文件保存的命名空间
		String fileSpace = "D:/develop/workspace/videos-img";

		//保存到数据库相对路径
		String uploadPathDb ="/"+userId+"/face";
		FileOutputStream fileOutputStream = null;
		InputStream inputStream = null;

		try {
			if(files !=null && files.length>0){

				String fileName = files[0].getOriginalFilename();
				if(StringUtils.isNoneBlank(fileName)){
					//文件上传的最终保存路径
					String finalFacePath = fileSpace + uploadPathDb + "/" +fileName;
					//设置数据库保存的路径
					uploadPathDb += ("/"+fileName);
					File outfile = new File(finalFacePath);
					if(outfile.getParentFile() != null || !outfile.getParentFile().isDirectory()){
						//创建父文件夹
						outfile.getParentFile().mkdirs();
					}
					fileOutputStream =new FileOutputStream(outfile);
					inputStream= files[0].getInputStream();
					IOUtils.copy(inputStream,fileOutputStream);

				}

			}else {
				return IMoocJSONResult.errorMsg("上传出错...");
			}
		} catch (IOException e) {
			e.printStackTrace();
			return IMoocJSONResult.errorMsg("上传出错...");
		}finally {
			if(fileOutputStream != null){
				fileOutputStream.flush();
				fileOutputStream.close();
			}
		}

		Users user = new Users();
		user.setId(userId);
		user.setFaceImage(uploadPathDb);
		userService.updateUserInfo(user);

		return IMoocJSONResult.ok(uploadPathDb);

		}

	@ApiImplicitParam(name = "userId",value = "用户id",dataType = "String",paramType = "query",required = true)
	@ApiOperation(value = "查询用户信息", notes = "查询用户信息接口")
	@PostMapping("/query")
	public IMoocJSONResult query(String userId) throws Exception {
		if(StringUtils.isBlank(userId)){
			IMoocJSONResult.errorMsg("用户id不能为空...");
		}
		Users userInfo = userService.queryUserInfo(userId);
		UsersVO userVO = new UsersVO();
		BeanUtils.copyProperties(userInfo,userVO);
			return IMoocJSONResult.ok(userVO);
		}

	}


