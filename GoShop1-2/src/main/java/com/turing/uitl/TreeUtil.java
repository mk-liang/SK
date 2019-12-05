package com.turing.uitl;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.message.config.ServerAuthContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.turing.bean.SysMenuRoleExample;
import com.turing.bean.SysMenuRoleExample.Criteria;
import com.turing.bean.SysMenus;
import com.turing.bean.SysRoles;
import com.turing.bean.SysUserRoleExample;
import com.turing.controller.UserController;
import com.turing.mapper.SysMenuRoleMapper;
import com.turing.mapper.SysUserRoleMapper;


public class TreeUtil {

	
	
	
	
	//找所有一级菜单
	 public static List<SysMenus> findParent(long id,List<SysMenus> allList){
		List<SysMenus> parents = new ArrayList<>();
		for(SysMenus menu:allList) {
			if(menu.getParentId() == null) {
				
					menu.setText(menu.getName());
					menu.setChildren(findSons1(id,allList));
				    parents.add(menu);
				    
			}
			
			}
		return parents;
	}

	
	
//	public static List<SysMenus> findSons(long id,long pid,List<SysMenus> allList){
//		List<SysMenus> sons = new ArrayList<>();
//		for(SysMenus menu:allList) {
//			if(menu.getParentId() == null) {
//				continue;
//			}
//		 
//			if(menu.getParentId() == pid) {//务资采购电子商务系统
//				//继续再找该菜单下子节点
//				//递归算法
//			
//					
//					menu.setText(menu.getName());
//					menu.setChildren(findSons(id,menu.getId(), allList));
//					//添加到集合
//					sons.add(menu);	
//			
//						
//			}
//			
//			
//		}
//		return sons;
//	}
	
	public static List<SysMenus> findSons1(long pid,List<SysMenus> allList){
		List<SysMenus> sons = new ArrayList<>();
		for(SysMenus menu:allList) {
			if(menu.getParentId() == null) {
				continue;
			}
			if(menu.getParentId()== pid) {//务资采购电子商务系统
				//继续再找该菜单下子节点
				//递归算法
					menu.setText(menu.getName());
					menu.setChildren(findSons1(menu.getId(),allList));
					//添加到集合
					sons.add(menu);	
						
			}
			
			
		}
		return sons;
	}
	
	
	
}
