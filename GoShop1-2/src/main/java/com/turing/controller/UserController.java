package com.turing.controller;



import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.turing.bean.SysMenuRole;
import com.turing.bean.SysMenuRoleExample;
import com.turing.bean.SysMenus;
import com.turing.bean.SysMenusExample;
import com.turing.bean.SysRoles;
import com.turing.bean.SysRolesExample;
import com.turing.bean.SysUserRole;
import com.turing.bean.SysUserRoleExample;
import com.turing.bean.SysUsers;
import com.turing.bean.SysUsersExample;
import com.turing.bean.SysUsersExample.Criteria;
import com.turing.mapper.SysMenuRoleMapper;
import com.turing.mapper.SysMenusMapper;
import com.turing.mapper.SysRolesMapper;
import com.turing.mapper.SysUserRoleMapper;
import com.turing.mapper.SysUsersMapper;
import com.turing.uitl.TreeUtil;


 

@Controller
public class UserController {

	@Autowired 
	//用户接口
	private SysUsersMapper usedao;
	
	//角色菜单接口
	@Autowired
     private SysUserRoleMapper  srdao;
	
	@Autowired
	//菜单接口
	private SysMenusMapper menusdao;
	
	@Autowired
	//菜单角色接口
	private SysMenuRoleMapper mrdao;
	
	//处理登录
	/**
	 * 
	 * @param user  用户
	 * @param model 
	 * @param session
	 * @return
	 */
	@PostMapping("/tologin")
	public String login(SysUsers user,RedirectAttributes model ,HttpSession session) {
		
		if(user!=null) {
			SysUsersExample c=new SysUsersExample();
			Criteria a= c.createCriteria();
          a.andLoginIdEqualTo(user.getLoginId());
          a.andPasswordEqualTo(user.getPassword());
		 List<SysUsers> users = usedao.selectByExample(c);
		 if(users.get(0).getStatus().equals("可用")) {
			//登录成功
			session.setAttribute("user", users.get(0));
			return "index";
		}else {
			//失败
			model.addFlashAttribute("msg", "登录失败");
			return "redirect:/";
		}
		}
		return "redirect:/";
	}
	
	//获取所有菜单信息(json集合格式数据)
		@GetMapping("/menus")
		@ResponseBody
		public List<SysMenus> findAll(HttpSession session){
			//获取用户信息
			SysUsers use= (SysUsers) session.getAttribute("user");
			//获取用户对应角色信息
           SysUserRoleExample ur=new SysUserRoleExample();
           ur.createCriteria().andUserIdEqualTo(use.getId());
		   List<SysUserRole> role=srdao.selectByExample(ur);
		   //获取角色对应的菜单信息
		   SysMenuRoleExample mr=new SysMenuRoleExample();
		   mr.createCriteria().andRoleIdEqualTo(role.get(0).getRoleId());
		   List<SysMenuRole>  urloe  =mrdao.selectByExample(mr);
		   //对应菜单信息
		   long i=urloe.get(0).getMenuId();
			//1,查询用户的菜单信息	
			List<SysMenus> allList = menusdao.selectByExample(null);	
			List<SysMenus> parents = TreeUtil.findParent(i,allList);
			return parents;
		}
		
		
}