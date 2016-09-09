package com.funtest.analysis.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.funtest.analysis.bean.User;
import com.funtest.analysis.service.UserService;
import com.funtest.core.bean.ReturnMsg;
import com.funtest.core.bean.constant.Constants;
import com.funtest.core.bean.page.Page;
import com.google.gson.Gson;

import sun.misc.BASE64Encoder;

@Controller
@RequestMapping("/user")
public class UserController {

	private static Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	UserService service;

	@SuppressWarnings("finally")
	@RequiresPermissions("CardManager:createCard")
	@RequestMapping("/createUser")
	@ResponseBody
	public Object createUser(@RequestParam(value = "user", required = true) String user) {
		ReturnMsg rm = new ReturnMsg();
		try {
			System.out.println("user:" + user);
			User u = new Gson().fromJson(user, User.class);
			rm.setData(service.createUser(u));
			rm.setCode(Constants.RETURN_MSG_SUCCESS);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			rm.setCode(Constants.RETURN_MSG_FAILURE);
			rm.setMessage(e.getMessage());
		} finally {
			return rm;
		}
	}

	@SuppressWarnings("finally")
	@RequiresPermissions("CardManager:deleteCard")
	@RequestMapping("deleteUser")
	@ResponseBody
	public Object deleteUser(@RequestParam(value = "user", required = true) String user) {
		ReturnMsg rm = new ReturnMsg();
		try {
			User u = new Gson().fromJson(user, User.class);
			service.deleteUser(u);
			rm.setCode(Constants.RETURN_MSG_SUCCESS);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			rm.setCode(Constants.RETURN_MSG_FAILURE);
		} finally {
			return rm;
		}

	}

	@SuppressWarnings("finally")
	@RequiresPermissions("CardManager:updateCard")
	@RequestMapping("updateUser")
	@ResponseBody
	public Object updateUser(@RequestParam(value = "user", required = true) String user) {
		ReturnMsg rm = new ReturnMsg();
		try {
			User u = new Gson().fromJson(user, User.class);
			service.updateUser(u);
			rm.setCode(Constants.RETURN_MSG_SUCCESS);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			rm.setCode(Constants.RETURN_MSG_FAILURE);
		} finally {
			return rm;
		}
	}

	@SuppressWarnings("finally")
	@RequestMapping("queryUser/{id}")
	@ResponseBody
	public Object queryUser(@PathVariable("id") Integer id) {
		ReturnMsg rm = new ReturnMsg();
		try {
			rm.setData(service.queryUser(id));
			rm.setCode(Constants.RETURN_MSG_SUCCESS);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			rm.setCode(Constants.RETURN_MSG_FAILURE);
		} finally {
			return rm;
		}

	}

	@SuppressWarnings("finally")
	@RequestMapping("queryUserByName/{name}")
	@ResponseBody
	public Object queryUserByName(@PathVariable("name") String name) {
		ReturnMsg rm = new ReturnMsg();
		try {
			rm.setData(service.queryUserByName(name));
			rm.setCode(Constants.RETURN_MSG_SUCCESS);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			rm.setCode(Constants.RETURN_MSG_FAILURE);
		} finally {
			return rm;
		}

	}

	@SuppressWarnings("finally")
	@RequestMapping("queryUserByEId/{eId}")
	@ResponseBody
	public Object queryUserByEmployeeId(@PathVariable("eId") String employeeId) {
		ReturnMsg rm = new ReturnMsg();
		try {
			rm.setData(service.queryUserByEmployeeId(employeeId));
			rm.setCode(Constants.RETURN_MSG_SUCCESS);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			rm.setCode(Constants.RETURN_MSG_FAILURE);
		} finally {
			return rm;
		}

	}

	@SuppressWarnings("finally")
	@RequestMapping("queryUserByPhoneNo/{phoneNo}")
	@ResponseBody
	public Object queryUserByPhoneNo(@PathVariable("phoneNo") String phoneNo) {
		ReturnMsg rm = new ReturnMsg();
		try {
			rm.setData(service.queryUserByPhoneNo(phoneNo));
			rm.setCode(Constants.RETURN_MSG_SUCCESS);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			rm.setCode(Constants.RETURN_MSG_FAILURE);
		} finally {
			return rm;
		}

	}

	@SuppressWarnings("finally")
	@RequestMapping("queryUserRecords")
	@ResponseBody
	public Object queryUserRecords() {
		ReturnMsg rm = new ReturnMsg();
		try {
			rm.setData(service.queryUserRecords());
			rm.setCode(Constants.RETURN_MSG_SUCCESS);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			rm.setCode(Constants.RETURN_MSG_FAILURE);
		} finally {
			return rm;
		}

	}

	@SuppressWarnings("finally")
	@RequestMapping("queryPage/{curPage}")
	@ResponseBody
	public Object queryPage(@PathVariable("curPage") Integer curPage) {
		ReturnMsg rm = new ReturnMsg();
		try {
			Page<User> page = new Page<User>();
			page.setCurPage(curPage);
			rm.setData(service.queryPage(page, null));
			rm.setCode(Constants.RETURN_MSG_SUCCESS);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			rm.setCode(Constants.RETURN_MSG_FAILURE);
		} finally {
			return rm;
		}

	}

	@SuppressWarnings("finally")
	@RequestMapping("queryCanRegister")
	@ResponseBody
	public Object queryCanRegister(@RequestParam(value = "user", required = true) String user) {
		ReturnMsg rm = new ReturnMsg();
		try {
			User u = new Gson().fromJson(user, User.class);

			String data = service.queryCanRegister(u);
			rm.setData(data);
			rm.setCode(Constants.RETURN_MSG_SUCCESS);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			rm.setCode(Constants.RETURN_MSG_FAILURE);
		} finally {
			return rm;
		}
	}

	@SuppressWarnings("finally")
	@RequestMapping("addStar/{id}")
	@ResponseBody
	public Object addStar(@PathVariable("id") Integer id, HttpSession session) {
		ReturnMsg rm = new ReturnMsg();
		try {
			if(session.getAttribute(id.toString()) != null){
				rm.setCode(Constants.RETURN_MSG_FAILURE);
				return rm;
			}
			rm.setData(service.updateAddStar(id));
			rm.setCode(Constants.RETURN_MSG_SUCCESS);
			session.setAttribute(id.toString(), true);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			rm.setCode(Constants.RETURN_MSG_FAILURE);
		} finally {
			return rm;
		}

	}
	
	@SuppressWarnings("finally")
	@RequestMapping(value="avatarUpload",produces = "text/html; charset=UTF-8")
	@ResponseBody
	public Object avatarUpload(@RequestParam("avatar") MultipartFile file) throws IOException {
		ReturnMsg rm = new ReturnMsg();
		rm.setCode(Constants.RETURN_MSG_FAILURE);
		InputStream is=null;
		try {
			StringBuilder sb;
			String fileName=file.getOriginalFilename();
			String suffix=fileName.substring(fileName.lastIndexOf(".")+1);
			Pattern pattern = Pattern.compile("jpg|gif|png|bmp");
			Matcher matcher=pattern.matcher(suffix);  
			if(matcher.matches()){
				byte[] bytes= file.getBytes();
				if(bytes.length > 500*1024){
					throw new RuntimeException("图片大小不能超过500k");
				}
				is=file.getInputStream();
				BufferedImage img=  ImageIO.read(is);
				//像素与比例
				if(img.getHeight()> Math.sqrt(5e5)|| img.getWidth()> Math.sqrt(5e5)  
						|| (img.getWidth()/img.getHeight() != 1)){
					throw new RuntimeException("图片宽高比例必须是1:1");
				}
				
				sb=new StringBuilder();
				sb.append("data:image/png;base64,".replace("png", suffix));
				sb.append(new BASE64Encoder().encode(file.getBytes()).replaceAll("\r\n", ""));
				rm.setData(sb.toString()) ;
				System.out.println(rm.getData());
				rm.setCode(Constants.RETURN_MSG_SUCCESS);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			rm.setMessage(e.getMessage());
			rm.setCode(Constants.RETURN_MSG_FAILURE);
		} finally {
			if(is !=null){
				is.close();
			}
			return "<script>var rm="+new Gson().toJson(rm)+"; parent.uploadCallBack(rm);</script>";  
		}

	}
	
	@SuppressWarnings("finally")
	@RequestMapping("test")
	@ResponseBody
	public Object test(@RequestParam(value = "dataFiles", required = false) String file) {
		ReturnMsg rm = new ReturnMsg();
		try {
			//System.out.println("file"+file.getOriginalFilename());
			rm.setData("test");
			rm.setCode(Constants.RETURN_MSG_SUCCESS);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			rm.setCode(Constants.RETURN_MSG_FAILURE);
		} finally {
			return rm;
		}

	}
}
