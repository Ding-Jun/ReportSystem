package com.funtest.analysis.controller;

import com.funtest.analysis.bean.Admin;
import com.funtest.analysis.service.AdminService;
import com.funtest.analysis.util.CustomSessionUtil;
import com.funtest.core.bean.ReturnMsg;
import com.funtest.core.bean.constant.Constants;
import com.funtest.core.bean.page.Page;
import com.google.gson.Gson;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/admin")
public class AdminController {

	private static Logger logger = LoggerFactory.getLogger(AdminController.class);
	
	@Autowired
	AdminService service;

	@SuppressWarnings("finally")
	@RequestMapping("/createAdmin")
	@ResponseBody
	public Object createAdmin(@RequestParam(value = "admin", required = true) String admin) {
		ReturnMsg rm = new ReturnMsg();
		try {
			System.out.println("admin:" + admin);
			Admin u = new Gson().fromJson(admin, Admin.class);
			rm.setData(service.createAdmin(u));
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
	@RequiresPermissions("AuthManager:deleteAuth")
	@RequestMapping("deleteAdmin/{id}")
	@ResponseBody
	public Object deleteAdmin(@PathVariable("id") Integer id) {
		ReturnMsg rm = new ReturnMsg();
		try {
			service.deleteAdmin(id);
			rm.setCode(Constants.RETURN_MSG_SUCCESS);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			rm.setCode(Constants.RETURN_MSG_FAILURE);
		} finally {
			return rm;
		}

	}

	@SuppressWarnings("finally")
	@RequiresPermissions("AuthManager:updateAuth")
	@RequestMapping("updateAdmin")
	@ResponseBody
	public Object updateAdmin(@RequestParam(value = "name", required = true) String name,
								@RequestParam(value = "id", required = false) Integer[] permissionIds,
								@RequestParam(value = "curUserId", required = false) Integer curUserId) {
		ReturnMsg rm = new ReturnMsg();
		try {
			if(curUserId != null){
				service.updateAdmin(curUserId, name,permissionIds);
			}
			else service.createAdmin(name,permissionIds);
			rm.setCode(Constants.RETURN_MSG_SUCCESS);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			rm.setCode(Constants.RETURN_MSG_FAILURE);
		} finally {
			return rm;
		}
	}

	@SuppressWarnings("finally")
	//@RequiresPermissions("AuthManager:updateAuth")
	@RequestMapping("updateAdminPassword")
	@ResponseBody
	public Object updateAdminPassword(@RequestParam(value = "newPassword", required = true) Object newPassword,
								@RequestParam(value = "oldPassword", required = false) Object oldPassword,
								@RequestParam(value = "curUserId", required = false) Integer curUserId) {
		ReturnMsg rm = new ReturnMsg();
		try {
			
			String password = service.updateAdminPassword(curUserId==null? CustomSessionUtil.getLoginAdminId():curUserId,oldPassword==null?null:oldPassword.toString(),newPassword.toString());
			rm.setData(password);
			rm.setCode(Constants.RETURN_MSG_SUCCESS);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			rm.setCode(Constants.RETURN_MSG_FAILURE);
		} finally {
			return rm;
		}
	}
	
	@SuppressWarnings("finally")
	@RequestMapping("queryAdmin/{id}")
	@ResponseBody
	public Object queryAdmin(@PathVariable("id") Integer id) {
		ReturnMsg rm = new ReturnMsg();
		try {
			rm.setData(service.queryAdmin(id));
			rm.setCode(Constants.RETURN_MSG_SUCCESS);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			rm.setCode(Constants.RETURN_MSG_FAILURE);
		} finally {
			return rm;
		}

	}

	@SuppressWarnings("finally")
	@RequiresPermissions("AuthManager:queryAuth")
	@RequestMapping("queryAdminByName/{name}")
	@ResponseBody
	public Object queryAdminByName(@PathVariable("name") String name) {
		ReturnMsg rm = new ReturnMsg();
		try {
			rm.setData(service.queryAdminByName(name));
			rm.setCode(Constants.RETURN_MSG_SUCCESS);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			rm.setCode(Constants.RETURN_MSG_FAILURE);
		} finally {
			return rm;
		}

	}

	@SuppressWarnings("finally")
	@RequiresPermissions("AuthManager:queryAuth")
	@RequestMapping("queryCanRegister/{name}")
	@ResponseBody
	public Object queryCanRegister(@PathVariable("name") String name,
									@RequestParam(value="userId",required=false) Integer userId) {
		ReturnMsg rm = new ReturnMsg();
		try {
			rm.setData(service.queryCanRegister(name,userId));
			rm.setCode(Constants.RETURN_MSG_SUCCESS);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			rm.setCode(Constants.RETURN_MSG_FAILURE);
		} finally {
			return rm;
		}

	}
	
	@SuppressWarnings("finally")
	@RequestMapping("queryAdminRecords")
	@ResponseBody
	public Object queryAdminRecords() {
		ReturnMsg rm = new ReturnMsg();
		try {
			rm.setData(service.queryAdminRecords());
			rm.setCode(Constants.RETURN_MSG_SUCCESS);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			rm.setCode(Constants.RETURN_MSG_FAILURE);
		} finally {
			return rm;
		}

	}

	@Deprecated
	@SuppressWarnings("finally")
	@RequestMapping("queryPage/{curPage}")
	@ResponseBody
	public Object queryPage(@PathVariable("curPage") Integer curPage) {
		ReturnMsg rm = new ReturnMsg();
		try {
			Page<Admin> page = new Page<Admin>();
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
	@RequestMapping("queryAdminName")
	@ResponseBody
	public Object queryAdminName() {
		ReturnMsg rm = new ReturnMsg();
		try {
			rm.setData(service.queryAdminName());
			rm.setCode(Constants.RETURN_MSG_SUCCESS);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			rm.setCode(Constants.RETURN_MSG_FAILURE);
		} finally {
			return rm;
		}

	}
	
	@SuppressWarnings("finally")
	@RequestMapping("queryAllResources")
	@ResponseBody
	public Object queryAllResources() {
		ReturnMsg rm = new ReturnMsg();
		try {
			rm.setData(service.queryAllResources());
			rm.setCode(Constants.RETURN_MSG_SUCCESS);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			rm.setCode(Constants.RETURN_MSG_FAILURE);
		} finally {
			return rm;
		}

	}
	
	@SuppressWarnings("finally")
	@RequestMapping("queryAllPermissions")
	@ResponseBody
	public Object queryAllPermissions() {
		ReturnMsg rm = new ReturnMsg();
		try {
			rm.setData(service.queryAllPermissions());
			rm.setCode(Constants.RETURN_MSG_SUCCESS);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			rm.setCode(Constants.RETURN_MSG_FAILURE);
		} finally {
			return rm;
		}

	}
	
	@SuppressWarnings("finally")
	@RequestMapping("queryResources")
	@ResponseBody
	public Object queryResources() {
		ReturnMsg rm = new ReturnMsg();
		try {
			rm.setData(service.queryResources());
			rm.setCode(Constants.RETURN_MSG_SUCCESS);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			rm.setCode(Constants.RETURN_MSG_FAILURE);
		} finally {
			return rm;
		}

	}
	/**
	 * 返回的是Permission对象
	 * @return
	 */
	@SuppressWarnings("finally")
	@RequestMapping("queryPermissions")
	@ResponseBody
	public Object queryPermissions() {
		ReturnMsg rm = new ReturnMsg();
		try {
			rm.setData(service.queryPermissions());
			rm.setCode(Constants.RETURN_MSG_SUCCESS);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			rm.setCode(Constants.RETURN_MSG_FAILURE);
		} finally {
			return rm;
		}

	}
	/**
	 * 返回的是string permission
	 * @return
	 */
	@SuppressWarnings("finally")
	@RequestMapping("queryAdminPermissionsByResource")
	@ResponseBody
	public Object queryAdminPermissionsByResource(
			@RequestParam(value="resourceName",required=true) String resourceName) {
		ReturnMsg rm = new ReturnMsg();
		try {
			rm.setData(service.queryAdminPermissionsByResource(resourceName));
			rm.setCode(Constants.RETURN_MSG_SUCCESS);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			rm.setCode(Constants.RETURN_MSG_FAILURE);
		} finally {
			return rm;
		}

	}
	
	/**
	 * 模糊查询
	 * 
	 * @param name
	 * @return
	 */
	@SuppressWarnings("finally")
	@RequestMapping("queryAdminByName/{name}/{curPage}")
	@ResponseBody
	public Object queryUserStarByNameHazily(@PathVariable("name") String name, 
											@PathVariable("curPage") Integer curPage,
			@RequestParam(value = "pageSize", required = false) Integer pageSize) {
		ReturnMsg rm = new ReturnMsg();
		try {
			rm.setData(service.queryAdminByNameHazily(name, curPage, pageSize));
			rm.setCode(Constants.RETURN_MSG_SUCCESS);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			rm.setCode(Constants.RETURN_MSG_FAILURE);
		} finally {
			return rm;
		}

	}

}
