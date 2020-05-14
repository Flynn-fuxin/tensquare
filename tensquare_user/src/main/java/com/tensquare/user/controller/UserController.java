package com.tensquare.user.controller;
import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import com.tensquare.user.po.User;
import com.tensquare.user.service.UserService;
import dto.PageResultDTO;
import dto.ResultDTO;
import constants.StatusCode;
import utils.JwtUtil;
import javax.servlet.http.HttpServletRequest;

/**
 * user控制器层
 * @author BoBoLaoShi
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	
	
	/**
	 * 增加
	 * @param user
	 */
	@PostMapping
	public ResultDTO add(@RequestBody User user  ){
		userService.saveUser(user);
		return new ResultDTO(true,StatusCode.OK,"增加成功");
	}

	/**
	 * 用户注册
	 * @param user
	 */
	@PostMapping("/register/{checkcode}")
	public ResultDTO register(@RequestBody User user ,@PathVariable String checkcode ){
		userService.saveUser(user,checkcode);
		return new ResultDTO(true,StatusCode.OK,"用户注册成功");
	}

	//注入JwtUtil
	@Autowired
	private JwtUtil jwtUtil;
	/**
	 * 用户登录
	 * @param loginMap
	 * @return
	 */
	@PostMapping("/login")
	public ResultDTO login(@RequestBody Map<String,String> loginMap){
		//调用业务层查询
		User user=userService.findUserByMobileAndPassword(loginMap.get("mobile"),loginMap.get("password"));
		//判断
		if(null!=user){
			//签发token
			String token = jwtUtil.createJWT(user.getId(), user.getNickname(), "user");
			HashMap<String, String> map = new HashMap<>();
			map.put("token",token);
			map.put("name",user.getNickname());
			map.put("avatar",user.getAvatar());
			//登录成功
			return new ResultDTO(true,StatusCode.OK,"登录成功",map);
		}else{
			//登录失败
			return new ResultDTO(false,StatusCode.LOGINERROR,"用户名或密码错误");
		}
	}
	
	/**
	 * 修改
	 * @param user
	 */
	@PutMapping("/{id}")
	public ResultDTO edit(@RequestBody User user, @PathVariable String id ){
		user.setId(id);
		userService.updateUser(user);		
		return new ResultDTO(true,StatusCode.OK,"修改成功");
	}

	@Autowired
	private HttpServletRequest request;
	@Autowired
	private JwtUtil util;
	
	/**
	 * 删除
	 * @param id
	 */
	@DeleteMapping("/{id}")
	public ResultDTO remove(@PathVariable String id ){
		//必须管路员用户才能删除普通用户
		//获取包含token的头信息
		/*String authorizationHeader = request.getHeader("JwtAuthorization");
		if(null==authorizationHeader){
			return new ResultDTO(false,StatusCode.ACCESSERROR,"权限不足1");
		}
		//判断授权头信息中是否是以“Bearer ”开头
		if(!authorizationHeader.startsWith("Bearer ")){
			return new ResultDTO(false,StatusCode.ACCESSERROR,"权限不足2");
		}
		//获取载荷
		Claims claims=null;
		try {
			//获取令牌
			String token=authorizationHeader.substring(7);
			claims = util.parseJWT(token);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultDTO(false,StatusCode.ACCESSERROR,"权限不足3");
		}
		//判断载荷是否为空
		if(null==claims){
			return new ResultDTO(false,StatusCode.ACCESSERROR,"权限不足4");
		}
		//判断令牌中的自定义载荷中的角色是否是admin
		if(!"admin".equals(claims.get("roles"))){
			return new ResultDTO(false,StatusCode.ACCESSERROR,"权限不足5");
		}*/

		//1.先鉴权，必须是普通用户角色才能发布问题
		Claims user_claims = (Claims) request.getAttribute("user_claims");
		if (null== user_claims){
			return new ResultDTO(false,StatusCode.ACCESSERROR,"权限不足");
		}
		//补全发布问题的用户id

		//2.删除用户
		userService.deleteUserById(id);
		return new ResultDTO(true,StatusCode.OK,"删除成功");
	}

	/**
	 * 查询全部数据
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ResultDTO list(){
		return new ResultDTO(true,StatusCode.OK,"查询成功",userService.findUserList());
	}
	
	/**
	 * 根据ID查询
	 * @param id ID
	 * @return
	 */
	@GetMapping("/{id}")
	public ResultDTO listById(@PathVariable String id){
		return new ResultDTO(true,StatusCode.OK,"查询成功",userService.findUserById(id));
	}

	/**
     * 根据条件查询
     * @param searchMap
     * @return
     */
    @PostMapping("/search")
    public ResultDTO list( @RequestBody Map searchMap){
        return new ResultDTO(true,StatusCode.OK,"查询成功",userService.findUserList(searchMap));
    }

	/**
	 * 分页+多条件查询
	 * @param searchMap 查询条件封装
	 * @param page 页码
	 * @param size 页大小
	 * @return 分页结果
	 */
	@PostMapping("/search/{page}/{size}")
	public ResultDTO listPage(@RequestBody Map searchMap , @PathVariable int page, @PathVariable int size){
		Page<User> pageResponse = userService.findUserListPage(searchMap, page, size);
		return  new ResultDTO(true,StatusCode.OK,"查询成功",  new PageResultDTO<User>(pageResponse.getTotalElements(), pageResponse.getContent()) );
	}
	
}
