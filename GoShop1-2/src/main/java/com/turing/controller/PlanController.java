package com.turing.controller;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.turing.bean.Employee;
import com.turing.bean.EmployeeExample;
import com.turing.bean.IdMapping;
import com.turing.bean.Material;
import com.turing.bean.Orders;
import com.turing.bean.OrdersExample;
import com.turing.bean.OrdersExample.Criteria;
import com.turing.bean.SysUsers;
import com.turing.mapper.EmployeeMapper;
import com.turing.mapper.IdMappingMapper;
import com.turing.mapper.MaterialMapper;
import com.turing.mapper.OrdersMapper;

@Controller
public class PlanController {

	// 物资接口
	@Autowired
	private MaterialMapper MMdao;

	// 计划需求
	@Autowired
	private OrdersMapper odao;

	// 关系对应表
	@Autowired
	private IdMappingMapper idao;

	// 员工表
	@Autowired
	private EmployeeMapper empdao;

	@GetMapping("/add")
	@ResponseBody
	/**
	 * 
	 * @param id    物资编号
	 * @param num   预购数量
	 * @param time  交货时间
	 * @param money 预单价
	 * @param desc  备注
	 * @return
	 */
	public String inserPlan(long id, String num, String time, int money, String desc) {
		//总金额
		int conmut=Integer.parseInt(num)*money;
		BigDecimal s = new BigDecimal(conmut);
		// 整型转BigDecimal
		BigDecimal b = new BigDecimal(money);
		// 物资信息
		Material ma = MMdao.selectByPrimaryKey(id);
		// 获取当前时间
		Date date = new Date();
		SimpleDateFormat sdf = (SimpleDateFormat) DateFormat.getDateTimeInstance();
		// 物资计划表
		String lsh = addOrd();
		Orders o = new Orders();
		o.setMaterialCode(ma.getMaterialNum());
		o.setMaterialName(ma.getMaterialName());
		o.setAmount(num);
		o.setMeasureUnit(ma.getMaterialUnit());
		o.setUnitPrice(b);
		o.setEndDate(time);
		o.setSumPrice(s);
		o.setOrderNum(lsh);
		o.setStartDate(sdf.format(date));
		o.setRemark(desc);
		int i = odao.insert(o);
		if (i == 1) {

			return "保存成功";
		} else {

			return "保存失败";
		}

	}

	@GetMapping("/addID")
	@ResponseBody
	public String inserOrd() {
		Orders o = odao.selnum(addOrdlast());
		IdMapping im = new IdMapping();
		im.setOrderId(o.getId());
		im.setStatus("C001-10");
		int i = idao.insert(im);
		if (i == 1) {

			return "保存成功";
		} else {

			return "保存失败";
		}

	}

	public String addOrdlast() {
		List<Orders> list = odao.selectByExample(null);

		// 获取编号
		String no = "100";
		Date d = new Date(System.currentTimeMillis());
		SimpleDateFormat simple = new SimpleDateFormat("yyyyMMdd");
		String dd = simple.format(new Date());
		StringBuilder s = new StringBuilder(no);

		// 流水号
		String lsh = null;
		String zlsh = null;
		if (list.size() > 0) {
			lsh = list.get(list.size() - 1).getOrderNum();
			int n = Integer.parseInt(lsh.substring(11)) + 1;
			if (n < 99999 & n > 9999) {
				zlsh = "" + n;
			} else if (n < 9999 & n > 999) {
				zlsh = "0" + n;
			} else if (n < 999 & n > 99) {
				zlsh = "00" + n;
			} else if (n < 99 & n > 9) {
				zlsh = "000" + n;
			} else {
				zlsh = "0000" + n;
			}

		}

		if (zlsh == null) {
			zlsh = "00001";
		}

		// 新的一年
		String bdd = dd.substring(4);
		if (bdd.equals("0101")) {
			zlsh = "00001";
		}
		// 追加
		s.append(dd);
		s.append(zlsh);

		return list.get(list.size() - 1).getOrderNum();

	}

	public String addOrd() {
		List<Orders> list = odao.selectByExample(null);

		// 获取编号
		String no = "100";
		Date d = new Date(System.currentTimeMillis());
		SimpleDateFormat simple = new SimpleDateFormat("yyyyMMdd");
		String dd = simple.format(new Date());
		StringBuilder s = new StringBuilder(no);

		// 流水号
		String lsh = null;
		String zlsh = null;
		if (list.size() > 0) {
			lsh = list.get(list.size() - 1).getOrderNum();
			int n = Integer.parseInt(lsh.substring(11)) + 1;
			if (n < 99999 & n > 9999) {
				zlsh = "" + n;
			} else if (n < 9999 & n > 999) {
				zlsh = "0" + n;
			} else if (n < 999 & n > 99) {
				zlsh = "00" + n;
			} else if (n < 99 & n > 9) {
				zlsh = "000" + n;
			} else {
				zlsh = "0000" + n;
			}

		}

		if (zlsh == null) {
			zlsh = "00001";
		}

		// 新的一年
		String bdd = dd.substring(4);
		if (bdd.equals("0101")) {
			zlsh = "00001";
		}
		// 追加
		s.append(dd);
		s.append(zlsh);

		return s.toString();

	}

	@GetMapping("/findPlan")
	@ResponseBody
	public Map findAll(int page, int rows, String status, String materialCode, String materialName) {
		// 设置分页的条件
		PageHelper.startPage(page, rows);
		String s = status;
		List listid = odao.selectAll(materialCode, materialName, s);

		PageInfo pageinfo = new PageInfo<>(listid);
		Map data = new HashMap();
		data.put("total", pageinfo.getTotal());
		data.put("rows", pageinfo.getList());

		return data;

	}

	@GetMapping("lookdetails")
	public String details(long id, HttpServletRequest req, Model model) {
		// 查看计划需求
		Orders order = odao.selectByPrimaryKey(id);
		model.addAttribute("order", order);
		// 获取当前用户
		SysUsers use = (SysUsers) req.getSession().getAttribute("user");

		// 通过登录用户查到具体员工
		EmployeeExample emp = new EmployeeExample();
		com.turing.bean.EmployeeExample.Criteria c = emp.createCriteria();
		if (use != null) {
			c.andUserIdEqualTo(use.getId());
		}
		List<Employee> ls = empdao.selectByExample(emp);
		// 获取第一个员工
		Employee em = ls.get(0);
		model.addAttribute("em", em);

		return "planman/print_order_detail.html";
	}

	@PostMapping("delOrders")
	@ResponseBody
	public String delOrders(long id) {
		//俩表连删
		int s = idao.Del(id);
		int i = odao.deleteByPrimaryKey(id);
		return "succ";

	}

	@PostMapping("/UpStatus")
	@ResponseBody
	public String  UpStatus(long id) {
		System.out.println("哈哈哈哈哈"+id);
		//修改状态
		int i =idao.upsta(id);
	    if(i==1) {
	    	return "修改成功";
     	 }else {
     		 
     		 return "修改失败";
     	 }


	}
    @GetMapping("/Updetails")
    /**
     * 
     * 查询物资信息
     * @param id 物资id
     * @param model
     * @return
     */
    public String Updetails(long id,Model model) {
        Orders order=odao.selectByPrimaryKey(id);
    	model.addAttribute("order",order);
		return  "planman/update_xuqiujihua.html";
    }
    @PostMapping("/toup")
    public String  toup(Orders o) {
    	int i=odao.updateByPrimaryKeySelective(o);
		return "planman/Order_ytb_list.htm";
    	
    	
    }
    @GetMapping("/wbxjfa")
    @ResponseBody
    public Map wbxjfa(int page, int rows) {
		// 设置分页的条件
		PageHelper.startPage(page, rows);
	    List<Orders> s=	odao.selAll();
		PageInfo pageinfo = new PageInfo<>(s);
		Map data = new HashMap();
		data.put("total", pageinfo.getTotal());
		data.put("rows", pageinfo.getList());
		return data;

	}

	

}
