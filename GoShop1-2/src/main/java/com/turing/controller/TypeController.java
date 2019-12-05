package com.turing.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.turing.bean.Material;
import com.turing.bean.MaterialType;
import com.turing.bean.MaterialTypeExample;
import com.turing.bean.MaterialTypeExample.Criteria;

import com.turing.mapper.MaterialMapper;
import com.turing.mapper.MaterialTypeMapper;

import javassist.expr.NewArray;

@Controller
public class TypeController {

	@Autowired
	// 物资分类表
	private MaterialTypeMapper MTdao;
	@Autowired
	// 物资表
	private MaterialMapper MMdao;
	
	//保存id
	List<Material> listid=new ArrayList<>();
	//保存物资详情
	List<Material> listMA=new ArrayList<>();
	
	
	@GetMapping("/findMM")
	@ResponseBody
	/**
	 * 加载表格
	 * @return
	 */
	public Map findAll() {
		
		PageInfo<Material> pageinfo = new PageInfo<>(listid);
		
		Map data = new HashMap();
		data.put("total", pageinfo.getTotal());
		data.put("rows", pageinfo.getList());
		data.put("row", "未完成");
		return data;

	}
	
	@GetMapping("/findMa")
	@ResponseBody
	/**
	 * 加载表格
	 * @return
	 */
	public Map findma() {
		
		PageInfo<Material> pageinfo = new PageInfo<>(listMA);
		Map data = new HashMap();
		data.put("total", pageinfo.getTotal());
		data.put("rows", pageinfo.getList());
		return data;

	}

	//查看物资详情
	@GetMapping("/findmater")
	@ResponseBody
	public Material findmater(long id) {
		//清楚原集合存的东西
		listMA.clear();	
		Material Material =MMdao.selectByPrimaryKey(id);
		listMA.add(Material);
		return Material;
	}
	
	@GetMapping("/findAll")
	@ResponseBody
	// page :页码 rows: 大小
	public Map findAll(int page, int rows, String className, String productid) {
		// 设置分页的条件
		PageHelper.startPage(page, rows);
		MaterialTypeExample s = new MaterialTypeExample();
		Criteria j = s.createCriteria();
		if (className != null && className != "") {
			j.andTypeUnitEqualTo(className);
		}
		if (productid != null && productid != "") {
			String name = "%" + productid + "%";
			j.andTypeNameLike(name);
		}
		List<MaterialType> list = MTdao.selectByExample(s);
		PageInfo<MaterialType> pageinfo = new PageInfo<>(list);
		Map data = new HashMap();
		data.put("total", pageinfo.getTotal());
		data.put("rows", pageinfo.getList());
		return data;

	}

	// 查询物资信息
	@PostMapping("/sel")
	@ResponseBody
	/**
	 * 
	 * @param id
	 * @return 物资信息
	 */
	public  Map<String, Object> sel1(String id) {
		 List<Material> ma = MMdao.selectByUitl(id);
		 Map<String, Object> map = new HashMap<String, Object>();
	        map.put("replies", ma);
	        return map;
	}


	// 查询物资信息
	@PostMapping("/togo")
	@ResponseBody
	/**
	 * 
	 * @param id
	 * @return 物资id
	 */
	public  String  togo(@RequestParam("arr[]") int[] arr) {
		listid.clear();		
		for (int i = 0; i < arr.length; i++) {
			long a=arr[i];
			Material m=MMdao.selectByPrimaryKey(a);
			listid.add(m);
		}
		return "od";
	 
	}

	
	
	
	// 加载下拉列表数据
	@RequestMapping("/loadCategory")
	@ResponseBody
	public List<Map<String, Object>> loadCategory(HttpServletResponse response) {
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8"); // 响应编码
		List<MaterialType> namelist = MTdao.find();
		// list存放map，map存放kv值（json），namelist取需要的字段
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (MaterialType category : namelist) {
			Map<String, Object> map = new HashMap<String, Object>();
			// 只取classid、ClassName即可。
			String className = category.getTypeUnit().toString();
			String classid = category.getId().toString();
			// map存放键值对
			map.put("classid", classid);
			map.put("className", className);
			// list存放map
			list.add(map);
		}
		return list;
	}
    
}
