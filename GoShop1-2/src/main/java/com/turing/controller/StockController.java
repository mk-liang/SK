package com.turing.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.turing.bean.Employee;
import com.turing.bean.IdMapping;
import com.turing.bean.Orders;
import com.turing.bean.Stock;
import com.turing.bean.SysUsers;
import com.turing.mapper.EmployeeMapper;
import com.turing.mapper.IdMappingMapper;
import com.turing.mapper.OrdersMapper;
import com.turing.mapper.StockMapper;
import com.turing.mapper.StockSupplierMapper;

@Controller
public class StockController {
	
	@Autowired
	private IdMappingMapper idao;
	
	@Autowired
	private StockMapper sdao;
	
	@Autowired
	private EmployeeMapper onedao;
	
	@Autowired
	private OrdersMapper odao;
	
	@Autowired
	private StockSupplierMapper ssdao;
	
	@PostMapping("/UpSts")
	@ResponseBody
	public String UpStart(long id) {
		int i=0;
	List<IdMapping> sto= idao.findStock(id);
	for (IdMapping idMapping : sto) {
		idMapping.setStatus("C001-40");
		i=idao.updateByPrimaryKeySelective(idMapping);
	}
	
		return "";
		
	}
	

	@GetMapping("/SeeDest")
	public String SeeDest(long id,String s,Model model) {
	    //采购计划
		Stock  lit=sdao.Stocklook(id);
		
		//通过stockid查看物资
		List<Orders> list=odao.findid(id);
		model.addAttribute("list",list);
		model.addAttribute("lit",lit);
		model.addAttribute("stud",s);
		return "planman/xjfatz_xjfamx2";
		
	}
	@GetMapping("/SeeDest1")
	public String SeeDest1(long id,Model model) {
	    //采购计划
		Stock  lit=sdao.Stocklook(id);
		
		//通过stockid查看物资
		List<Orders> list=odao.findid(id);
		model.addAttribute("list",list);
		model.addAttribute("lit",lit);
		return "planman/xjfatz_xjfamx4";
		
	}
	
	
	@GetMapping("/SeeDest2")
	public String SeeDest2(long id,Model model,HttpSession session) {
	    //采购计划
		Stock  lit=sdao.Stocklook(id);
		//通过stockid查看物资
		List<Orders> list=odao.findid(id);
		model.addAttribute("list",list);
		model.addAttribute("lit",lit);
		 SysUsers use= (SysUsers) session.getAttribute("user");
		 Employee  em= onedao.find(use.getId());
		 model.addAttribute("nid",em.getId());
		 model.addAttribute("name",em.getEmpName());	
		 Date date=new Date();
		 SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	     String time= sdf.format(date);
	     model.addAttribute("time",time);
		return "planman/xjfatz_xjfamx5";
		
	}
	   @GetMapping("/ftock")
		@ResponseBody
		public Map findAll(int page, int rows) {
			// 设置分页的条件
			PageHelper.startPage(page, rows);
			List liston = sdao.findTwo();
			PageInfo pageinfo = new PageInfo<>(liston);
			Map data = new HashMap();
			data.put("total", pageinfo.getTotal());
			data.put("rows", pageinfo.getList());
			return data;

		}

	   @PostMapping("/del")
	   @ResponseBody
	   public String del(long id) {
		 Stock to=sdao.selectByPrimaryKey(id);
		 if(to.getStockType()=="C000-2") {
			 ssdao.del(id);
		 }
		 //删除废弃采购计划
		List<IdMapping> sto= idao.findStock(id);
		for (IdMapping idMapping : sto) {
			idao.deleteByPrimaryKey(idMapping.getId());
		}
		int i=   sdao.deleteByPrimaryKey(id);
		if(i==1) {
			return "删除成功！";
			
		}else {
			return "删除失败！";
		}
		   
		   
	   }
}
